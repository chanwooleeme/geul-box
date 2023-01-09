package com.bestbranch.geulboxapi.common.config.swagger;

import com.bestbranch.geulboxapi.common.exception.model.ErrorCode;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static springfox.documentation.schema.ResolvedTypes.modelRefFactory;
import static springfox.documentation.spi.schema.contexts.ModelContext.returnValue;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.httpStatusCode;
import static springfox.documentation.spring.web.readers.operation.ResponseMessagesReader.message;
import static springfox.documentation.swagger.annotations.Annotations.resolvedTypeFromOperation;
import static springfox.documentation.swagger.readers.operation.ResponseHeaders.headers;
import static springfox.documentation.swagger.readers.operation.ResponseHeaders.responseHeaders;

@Component
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1)
public class CustomSwaggerResponseMessageReader implements OperationBuilderPlugin {

    private final TypeNameExtractor typeNameExtractor;
    private final TypeResolver typeResolver;

    @Autowired
    public CustomSwaggerResponseMessageReader(TypeNameExtractor typeNameExtractor, TypeResolver typeResolver) {
        this.typeNameExtractor = typeNameExtractor;
        this.typeResolver = typeResolver;
    }

    @Override
    public void apply(OperationContext context) {
        context.operationBuilder().responseMessages(read(context));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    protected Set<ResponseMessage> read(OperationContext context) {
        ResolvedType defaultResponse = context.getReturnType();
        Optional<ApiOperation> operationAnnotation = context.findAnnotation(ApiOperation.class);
        Optional<ResolvedType> operationResponse =
                operationAnnotation.transform(resolvedTypeFromOperation(typeResolver, defaultResponse));
        Optional<ResponseHeader[]> defaultResponseHeaders = operationAnnotation.transform(responseHeaders());
        Map<String, Header> defaultHeaders = newHashMap();
        if (defaultResponseHeaders.isPresent()) {
            defaultHeaders.putAll(headers(defaultResponseHeaders.get()));
        }

        List<ApiResps> allApiResponses = context.findAllAnnotations(ApiResps.class);
        Set<ResponseMessage> responseMessages = newHashSet();

        Map<Integer, ApiResp> seenResponsesByCode = newHashMap();
        for (ApiResps apiResponses : allApiResponses) {
            ApiResp[] apiResponseAnnotations = apiResponses.value();
            Map<Integer, List<ApiResp>> categorizedApiResps = new HashMap<>();

            // categorizing
            for (ApiResp apiResponse : apiResponseAnnotations) {
                int code = apiResponse.errorCode() == ErrorCode.DUMMY ? apiResponse.code() : apiResponse.errorCode().getStatusCode();
                List<ApiResp> categorizedResponses = categorizedApiResps.getOrDefault(code, new ArrayList<>());
                categorizedResponses.add(apiResponse);
                categorizedApiResps.put(code, categorizedResponses);
            }

            for (int code : categorizedApiResps.keySet()) {
                List<ApiResp> categorizedResponses = categorizedApiResps.get(code)
                        .stream()
                        .sorted(Comparator.comparingInt(it -> it.errorCode().getErrorCode()))
                        .collect(Collectors.toList());

                ApiResp representativeResponse = categorizedResponses.get(0);

                StringBuilder messages = new StringBuilder();
                if (representativeResponse.errorCode() != ErrorCode.DUMMY) {
                    messages.append(
                            String.format("(%d) %s", representativeResponse.errorCode().getErrorCode(), representativeResponse.errorCode().getMessage())
                    );
                } else {
                    messages.append(representativeResponse.message());
                }
                for (int i = 1; i < categorizedResponses.size(); ++i) {
                    ApiResp apiResp = categorizedResponses.get(i);
                    if (apiResp.errorCode() != ErrorCode.DUMMY) {
                        messages.append(String.format("<br /> (%d) %s", apiResp.errorCode().getErrorCode(), apiResp.errorCode().getMessage()));
                    }
                }

                seenResponsesByCode.put(representativeResponse.code(), representativeResponse);
                ModelContext modelContext = returnValue(
                        context.getGroupName(), representativeResponse.response(),
                        context.getDocumentationType(),
                        context.getAlternateTypeProvider(),
                        context.getGenericsNamingStrategy(),
                        context.getIgnorableParameterTypes());
                Optional<ModelReference> responseModel = Optional.absent();
                Optional<ResolvedType> type = resolvedType(null, representativeResponse);
                if (isSuccessful(representativeResponse.code())) {
                    type = type.or(operationResponse);
                }
                if (type.isPresent()) {
                    responseModel = Optional.of(modelRefFactory(modelContext, typeNameExtractor)
                            .apply(context.alternateFor(type.get())));
                }
                Map<String, Header> headers = newHashMap(defaultHeaders);
                headers.putAll(headers(representativeResponse.responseHeaders()));

                responseMessages.add(new ResponseMessageBuilder()
                        .code(code)
                        .message(messages.toString())
                        .responseModel(responseModel.orNull())
                        .headersWithDescription(headers)
                        .build());
            }
        }
        if (operationResponse.isPresent()) {
            ModelContext modelContext = returnValue(
                    context.getGroupName(),
                    operationResponse.get(),
                    context.getDocumentationType(),
                    context.getAlternateTypeProvider(),
                    context.getGenericsNamingStrategy(),
                    context.getIgnorableParameterTypes());
            ResolvedType resolvedType = context.alternateFor(operationResponse.get());

            ModelReference responseModel = modelRefFactory(modelContext, typeNameExtractor).apply(resolvedType);
            context.operationBuilder().responseModel(responseModel);
            ResponseMessage defaultMessage = new ResponseMessageBuilder()
                    .code(httpStatusCode(context))
                    .message(message(context))
                    .responseModel(responseModel)
                    .build();
            if (!responseMessages.contains(defaultMessage) && !"void".equals(responseModel.getType())) {
                responseMessages.add(defaultMessage);
            }
        }
        return responseMessages;
    }


    static boolean isSuccessful(int code) {
        try {
            return HttpStatus.Series.SUCCESSFUL.equals(HttpStatus.Series.valueOf(code));
        } catch (Exception ignored) {
            return false;
        }
    }

    private Optional<ResolvedType> resolvedType(
            ResolvedType resolvedType,
            ApiResp apiResponse) {
        return fromNullable(resolvedTypeFromResponse(typeResolver, resolvedType).apply(apiResponse));
    }

    public static Function<ApiResp, ResolvedType> resolvedTypeFromResponse(final TypeResolver typeResolver,
                                                                           final ResolvedType defaultType) {

        return annotation -> getResolvedType(annotation, typeResolver, defaultType);
    }

    @SuppressWarnings("Duplicates")
    @VisibleForTesting
    static ResolvedType getResolvedType(ApiResp annotation,
                                        TypeResolver resolver,
                                        ResolvedType defaultType) {
        if (null != annotation) {
            Class<?> response = annotation.response();
            String responseContainer = annotation.responseContainer();
            if (resolvedType(resolver, response, responseContainer).isPresent()) {
                return resolvedType(resolver, response, responseContainer).get();
            }
        }
        return defaultType;
    }

    private static Optional<ResolvedType> resolvedType(TypeResolver resolver,
                                                       Class<?> response,
                                                       String responseContainer) {
        if (!isNotVoid(response)) {
            return Optional.absent();
        }

        if ("List".compareToIgnoreCase(responseContainer) == 0) {
            return Optional.of(resolver.resolve(List.class, response));
        } else if ("Set".compareToIgnoreCase(responseContainer) == 0) {
            return Optional.of(resolver.resolve(Set.class, response));
        } else {
            return Optional.of(resolver.resolve(response));
        }
    }

    private static boolean isNotVoid(Class<?> response) {
        return Void.class != response && void.class != response;
    }
}



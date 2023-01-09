package com.bestbranch.geulboxapi.common.utils.jwt;

import com.bestbranch.geulboxapi.client.apple.AppleClient;
import com.bestbranch.geulboxapi.client.apple.model.ApplePublicKeyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class AppleJwtUtils implements JwtUtils {
    @Autowired
    private AppleClient appleClient;

    @Value("${apple.client.id}")
    private String clientId;
    @Value("${apple.team.id}")
    private String teamId;
    @Value("${apple.key.id}")
    private String keyId;

    @Override
    public Claims getClaimsBy(String token) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        ApplePublicKeyResponse response = appleClient.getAppleAuthPublicKey();

        String headerOfToken = token.substring(0, token.indexOf("."));
        Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfToken), "UTF-8"), Map.class);
        ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

        byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    }

    public String makeClientSecret() throws IOException {
        Date exprirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("alg", "ES256")
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(exprirationDate)
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("BBGB-AuthKey_Y6HZ2TQ7DG.p8");
        String privateKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }
}

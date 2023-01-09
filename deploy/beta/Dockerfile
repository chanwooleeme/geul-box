FROM adoptopenjdk/openjdk8

ARG HOST_JAR_FILE_PATH=./ROOT.jar
COPY ${HOST_JAR_FILE_PATH} ./

CMD ["java", "-Dspring.profiles.active=beta", "-jar", "./ROOT.jar"]
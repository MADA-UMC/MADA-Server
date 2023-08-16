FROM openjdk:11-jdk
EXPOSE 8080
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar","-Dspring.config.location=classpath:/application.properties,classpath:/application-DB.properties,classpath:/application-jwt.properties,classpath:/application-oauth2.properties","/app.jar"]


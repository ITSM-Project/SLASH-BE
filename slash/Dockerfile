FROM bellsoft/liberica-openjdk-alpine:17 as builder

# JAR 파일의 위치에 따른 변수 설정
ARG JAR_FILE=./slash/build/libs/*.jar

ARG PROFILES
ARG ENV
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-Dserver.env=${ENV}", "-jar", "app.jar"]

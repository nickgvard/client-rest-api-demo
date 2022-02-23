FROM openjdk:11

RUN mkdir -p /usr/src/app/
WORKDIR /usr/src/app/

ARG JAR=build/libs/iexcloud-api-demo-0.0.1.jar

ADD ${JAR} iexcloud-api.jar

CMD ["java", "-jar", "iexcloud-api.jar"]

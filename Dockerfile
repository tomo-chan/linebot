FROM openjdk:8u151-jdk-slim

ADD build/libs/linebot-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar

FROM openjdk:21-jdk

LABEL authors="sailex"

WORKDIR clips-notifier

COPY /build/libs/twitch-clips-notifier-*.jar .

CMD [ "java", "-jar", "twitch-clips-notifier-1.0.0.jar"]
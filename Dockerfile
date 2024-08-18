FROM ubuntu:latest
LABEL authors="sailex"

WORKDIR clips-notifier

CMD [ "java", "-jar", "twitch-clips-notifier-1.0.0.jar"]
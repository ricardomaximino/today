FROM openjdk:8-jre-alpine

# Maintainer
# ----------
LABEL maintainer="Ricardo Maximino - BrasaJava"

VOLUME /tmp


RUN addgroup brasajava \
    && adduser -s /bin/sh -D -G brasajava brasajava \
    && mkdir /logs \
    && chown brasajava:brasajava /logs \
    && chmod a+rw /logs
USER brasajava

# Required build arguments
ARG NAME
ARG VERSION

# Set environment variables
ENV ARTIFACT_NAME=$NAME \
    SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=local \
    JAVA_TOOL_OPTIONS=-Duser.timezone="Europe/Madrid" \
    TZ=CET-1CEST,M3.5.0,M10.5.0/3


EXPOSE ${SERVER_PORT} ${MANAGEMENT_PORT}

#Add EXECUTABLE JAR
COPY target/$NAME-$VERSION.jar $NAME.jar

#Add external file
#ADD path/file file


ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Xms64m -Xmx512m -jar /$ARTIFACT_NAME.jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE
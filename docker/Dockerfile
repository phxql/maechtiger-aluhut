FROM adoptopenjdk/openjdk11:alpine-slim

# Update system
RUN apk update && apk upgrade && rm -rf /var/apk/cache

# Create deployment dir
RUN mkdir /app
# Copy dependencies
COPY lib /app/lib
# Copy application
COPY maechtiger-aluhut-*.jar /app/app.jar

CMD ["java", "-XX:+ExitOnOutOfMemoryError", "-XX:+UseSerialGC", "-Xmx128M", "-jar", "/app/app.jar"]

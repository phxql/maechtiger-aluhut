FROM adoptopenjdk/openjdk11:alpine-slim
# Server runs on 8080
EXPOSE 8080
# Create deployment dir
RUN mkdir /app

# Exit on OOM, use 128M and the serial GC
CMD ["java", "-XX:+ExitOnOutOfMemoryError", "-XX:+UseSerialGC", "-Xms16M", "-Xmx128M", "-jar", "/app/app.jar"]

# Update system
RUN apk update && apk upgrade && rm -rf /var/apk/cache

# Copy dependencies
COPY lib /app/lib
# Copy application
COPY maechtiger-aluhut-*.jar /app/app.jar

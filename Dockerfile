FROM openjdk:17-jdk-slim
WORKDIR /memberService
COPY . .
RUN ./gradlew build
CMD ["java", "-jar", "build/libs/authService-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
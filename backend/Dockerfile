FROM openjdk:21-ea
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn/ .mvn/
COPY src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/online-flashcards-api.jar"]
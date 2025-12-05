FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:17

# Runtime environment variables
ENV SPRING_PROFILES_ACTIVE=dev
ENV APP_VERSION=1.0.0
ENV DB_URL=jdbc:postgresql://postgres-sql-bsn:5432/book_social_network

WORKDIR /app
COPY --from=build /build/target/book-network-*.jar /app/

EXPOSE 8088

# Use ENV variables directly, no ARG needed
CMD ["sh", "-c", "java -jar -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -Dspring.datasource.url=$DB_URL /app/book-network-*.jar"]

FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw -B -ntp dependency:go-offline

COPY src/ src/

RUN ./mvnw -B -ntp -DskipTests package


FROM eclipse-temurin:21-jre-jammy AS runtime

WORKDIR /app

RUN useradd --system --create-home spring

COPY --from=build /app/target/vartaos-backend-0.0.1-SNAPSHOT.jar /app/app.jar

USER spring

EXPOSE 8080

CMD ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]

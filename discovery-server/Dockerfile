# ---- Build Stage ----
FROM eclipse-temurin:21-jdk as build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Set JAVA_HOME
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy Maven wrapper and configuration first (for better layer caching)
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
COPY pom.xml ./
#RUN ./mvnw dependency:go-offline
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# ---- Run Stage ----
FROM eclipse-temurin:21-jre as runtime
WORKDIR /app

# Set JAVA_HOME
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]


FROM public.ecr.aws/docker/library/maven:3.8.6-amazoncorretto-11 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean install

FROM amazoncorretto:11
COPY --from=build /usr/src/app/target/spring-docker-1.0-SNAPSHOT.jar /usr/app/spring-docker-1.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/app/spring-docker-1.0-SNAPSHOT.jar"]

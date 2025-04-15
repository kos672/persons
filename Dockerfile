FROM openjdk:17-jdk-alpine
COPY build/libs/persons.jar persons.jar
ENTRYPOINT ["java","-jar","/persons.jar", "--spring.profiles.active=production"]
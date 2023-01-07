FROM adoptopenjdk/openjdk11
WORKDIR /app/monkey_automation_service
EXPOSE 8010
COPY target/monkey_automation_service-0.0.1.jar /app/monkey_automation_service/monkey_automation_service-0.0.1.jar
ENTRYPOINT ["java", "-jar", "/app/monkey_automation_service/monkey_automation_service-0.0.1.jar" ]
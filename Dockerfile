FROM openjdk:11
WORKDIR /app
EXPOSE 8080
ENV DB_PASS=diamante21
COPY ./prometheus.yaml /etc/prometheus
ADD ./target/moneymaster-0.0.1-SNAPSHOT.jar money-master.jar
ENTRYPOINT ["java", "-jar", "/app/money-master.jar"]

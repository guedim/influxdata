#Docker client to the Docker daemon, please set:
#    export DOCKER_CERT_PATH=c:\Users\SONY\.docker\machine\certs\
#    export DOCKER_TLS_VERIFY=1
#    export DOCKER_HOST=tcp://192.168.99.100:2376


FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/influxdata-0.0.1-SNAPSHOT.jar  app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
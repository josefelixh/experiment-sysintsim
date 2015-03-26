FROM dockerfile/java:oracle-java8
MAINTAINER Paolo Ambrosio <dockerfile@paolo.ambrosio.name>

RUN mkdir -p /opt/downstream
COPY build/libs/downstream-spring-*.jar /opt/downstream/downstream-spring.jar
WORKDIR /opt/downstream

EXPOSE 8080
ENTRYPOINT java \
  -Dgraphite.host=$GRAPHITE_PORT_2003_TCP_ADDR -Dgraphite.port=$GRAPHITE_PORT_2003_TCP_PORT \
  -Dservice.upstream.url=http://$UPSTREAM_PORT_9000_TCP_ADDR:$UPSTREAM_PORT_9000_TCP_PORT/ \
  -jar downstream-spring.jar

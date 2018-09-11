FROM anapsix/alpine-java:8

ADD target/user-consumer-0.0.1-SNAPSHOT.jar user-consumer.jar

ENTRYPOINT ["java","-jar","/user-consumer.jar"]
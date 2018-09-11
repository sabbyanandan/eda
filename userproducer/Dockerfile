FROM anapsix/alpine-java:8

ADD target/user-producer-0.0.1-SNAPSHOT.jar user-producer.jar

ENTRYPOINT ["java","-jar","/user-producer.jar"]
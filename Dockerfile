FROM loraneo/docker-java:10.0.2a

COPY target/cdr-gen-1.0-SNAPSHOT-jar-with-dependencies.jar cdr-gen-1.0-SNAPSHOT-jar-with-dependencies.jar
CMD java -jar cdr-gen-1.0-SNAPSHOT-jar-with-dependencies.jar


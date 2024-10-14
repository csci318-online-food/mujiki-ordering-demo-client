VERSION=0.0.1-SNAPSHOT

./mvnw package
java -jar target/demo-$VERSION.jar --events

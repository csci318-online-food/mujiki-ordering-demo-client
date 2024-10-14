# Demo Client

A mock client for testing Mujiki microservices.

## Building

This project is a nomral Maven Java project and does not work like the microservices, which are
Spring Boot projects.

To build the client, run:

```sh
./mvnw package
```

Or, on Windows:

```cmd
mvnw.cmd package
```

The resulting binary will be stored at `target/demo-0.0.1-SNAPSHOT.jar`.

## Use Case Demonstration Mode

Executing the binary with the `--use-cases` switch will launch the client in use cases demonstration
mode.

```sh
java -jar target/demo*.jar --use-cases
```

Or, on Windows:

```cmd
java -jar target\demo-0.0.1-SNAPSHOT.jar --use-cases
```

Alternatively, the `run_use_cases` script can be used.

In this mode, the client will try out all implemented use cases of Mujiki, printing out the
sample URLs, requset bodies, and responses.

## Event Seeding Mode

Executing the binary otherwise launches the client in event stream demonstration mode.

```sh
java -jar target/demo*.jar
```

Or, on Windows:

```cmd
java -jar target\demo-0.0.1-SNAPSHOT.jar
```

Alternatively, the `run_events` script can be used.

In this mode, the mock client regularly sends requests from a pool of users and restaurants to
complete processes that eventually lead to Feedback and Order events to be fired.

@echo off

setlocal
set VERSION=0.0.1-SNAPSHOT

mvnw.cmd package
java -jar target/demo-%VERSION%.jar --events
endlocal

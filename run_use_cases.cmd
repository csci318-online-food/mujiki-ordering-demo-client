@echo off

setlocal
set VERSION=0.0.1-SNAPSHOT

pushd %~dp0

call mvnw.cmd package
%JAVA_HOME%\bin\java.exe -jar target\demo-%VERSION%.jar --use-cases

popd

endlocal

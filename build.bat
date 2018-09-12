@echo off

pushd classes-model
call ..\gradlew.bat clean publishToMavenLocal
if ERRORLEVEL 1 exit /b 1
popd

call gradlew.bat clean build

 

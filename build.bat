@echo off
echo Do not forget to create build.sh

echo Removing all build folders
rd /S /Q .gradle build api-diff\build classes-model\build
echo Remove done

pushd classes-model
call ..\gradlew.bat clean publishToMavenLocal
if ERRORLEVEL 1 exit /b 1
popd

call gradlew.bat clean build

 

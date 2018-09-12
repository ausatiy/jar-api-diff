#!/bin/bash
pushd classes-model
/bin/bash ./gradlew clean publishToMavenLocal
if [ $? -ne 0 ] ;
then
  echo Could not build classes-model
  exit 1
fi
popd
/bin/bash gradlew clean build
echo Build done



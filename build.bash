#!/bin/bash

pushd `dirname $1`
#gradle wrapper
#./gradlew clean compileJava
gradle clean compileJava
popd

#!/bin/bash

./gradlew clean
./gradlew copyDependencies
cp build/dependencies/*.jar WebContent/WEB-INF/lib/

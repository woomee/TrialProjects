#!/bin/bash

gradle clean
gradle copyDependencies
cp build/dependencies/*.jar WebContent/WEB-INF/lib/
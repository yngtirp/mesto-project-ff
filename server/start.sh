#!/bin/bash

echo "Building Mesto Server..."
mvn clean package

if [ $? -eq 0 ]; then
    echo "Starting server..."
    java -jar target/mesto-server-1.0-SNAPSHOT.jar
else
    echo "Build failed!"
    exit 1
fi


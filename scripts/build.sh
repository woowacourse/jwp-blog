#!/bin/bash
./gradlew clean build

# Remove existing image
docker rmi laterality/myblog:latest

docker build -t laterality/myblog:latest .
docker push laterality/myblog:latest
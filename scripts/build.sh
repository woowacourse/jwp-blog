#!/bin/bash
./gradlew clean build

# Remove existing image
docker rmi laterality/myblog:latest

docker-compose build blog
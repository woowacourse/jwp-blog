#!/bin/bash
./gradlew clean build
docker build -t laterality/myblog:latest .
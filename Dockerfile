FROM openjdk:8

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN ./gradlew clean build

CMD java -jar /usr/src/app/build/libs/myblog-0.0.1-SNAPSHOT.jar

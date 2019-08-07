FROM openjdk:8

COPY ./build/libs/myblog-0.0.1-SNAPSHOT.jar /usr/src/app

WORKDIR /usr/src/app

CMD java -jar /usr/src/app/myblog-0.0.1-SNAPSHOT.jar

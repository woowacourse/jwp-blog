FROM openjdk:8

COPY ./build/libs/myblog-0.0.1-SNAPSHOT.jar /usr/src/app

WORKDIR /usr/src/app

CMD ["java" "-jar" "myblog-0.0.1-SNAPSHOT.jar"]
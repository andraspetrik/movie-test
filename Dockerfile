FROM gradle:8-jdk21

WORKDIR /usr/src/app

COPY MovieInfoService/src ./src
COPY MovieInfoService/build.gradle ./build.gradle
COPY gradle ./gradle
COPY settings.gradle ./
COPY gradlew ./
RUN sed -i.bak 's/\r$//' gradlew
RUN ./gradlew --no-daemon --info :bootJar

FROM openjdk:23-jdk

COPY --from=0 /usr/src/app/build/libs/*.jar ./MovieInfoService.jar

CMD java -jar MovieInfoService.jar
# As Scrapy runs on Python, I choose the official Python 3 Docker image.
# cd /Users/farrukhellahi/Dropbox/ApriCart/Server/java/apricart-spring-boot-backend
# docker build -t apricart-backend:latest .
# docker run apricart-backend


# clear && cd /Users/farrukhellahi/Dropbox/ApriCart/Server/java/apricart-spring-boot-backend && docker build -t apricart-backend:latest . && docker run apricart-backend

FROM ubuntu:20.04

# Create app directory
WORKDIR /usr/src/app

COPY . .

RUN apt update
RUN apt install -y maven
RUN mvn clean package

ENTRYPOINT [ "nohup", "java", "-jar", "target/apricart-spring-boot-SNAPSHOT.jar" ]

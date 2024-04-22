FROM debian:bullseye

WORKDIR /workspace

RUN apt-get update \
    && apt-get -y install openjdk-11-jre-headless wget \
    && wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.20/bin/apache-tomcat-10.1.20.tar.gz \
    && tar zxvf apache-tomcat-10.1.20.tar.gz \
    && mv apache-tomcat-10.1.20 /usr/libexec/tomcat10 \
    && useradd -M -d /usr/libexec/tomcat10 tomcat \
    && chown -R tomcat. /usr/libexec/tomcat10

COPY ./tomcat10.service /usr/lib/systemd/system/
COPY ./schedule.xml /usr/libexec/tomcat10/conf/Catalina/localhost/

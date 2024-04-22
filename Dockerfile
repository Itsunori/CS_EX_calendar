FROM tomcat:9-jdk11

WORKDIR /workspace

RUN apt-get update 

COPY ./tomcat10.service /usr/lib/systemd/system/
COPY ./schedule.xml /usr/local/tomcat/conf/Catalina/localhost/

EXPOSE 8080
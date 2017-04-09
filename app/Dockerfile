FROM openjdk:8

WORKDIR /tmp

RUN mkdir /opt/webapp
RUN wget -O /tmp/master.zip https://github.com/Kajo0/softax_reporter/archive/master.zip
RUN unzip master.zip \
    && cd softax_reporter-master/webapp \
    && ./gradlew build

RUN cp softax_reporter-master/webapp/build/libs/*jar /opt/webapp/

WORKDIR /opt

RUN mkdir /tmp/softax
RUN mkdir /tmp/softax/status
RUN mkdir /tmp/softax/pdf

EXPOSE 8080

CMD ["java", "-jar", "webapp/reporter-webapp-1.0.jar", "-Dorg.apache.activemq.SERIALIZABLE_PACKAGES=*"]


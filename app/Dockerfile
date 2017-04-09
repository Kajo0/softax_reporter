FROM openjdk:8

ENV WEBAPP_VERSION 1.0

WORKDIR /opt

ADD jars/reporter-webapp-${WEBAPP_VERSION}.jar ./webapp.jar

RUN mkdir /tmp/softax
RUN mkdir /tmp/softax/status
RUN mkdir /tmp/softax/pdf

EXPOSE 8080

CMD ["java", "-jar", "webapp.jar", "-Dorg.apache.activemq.SERIALIZABLE_PACKAGES=*"]


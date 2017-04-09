FROM openjdk:8

ENV ACTIVEMQ_VERSION 5.14.4
ENV ACTIVEMQ_OPTS "${ACTIVEMQ_OPTS} -Dorg.apache.activemq.SERIALIZABLE_PACKAGES=*"

RUN cd /opt \
    && wget -O activemq.tar.gz http://www.apache.org/dyn/closer.cgi?filename=/activemq/${ACTIVEMQ_VERSION}/apache-activemq-${ACTIVEMQ_VERSION}-bin.tar.gz\&action=download
RUN cd /opt \
    && mkdir activemq \
    && tar -xvf activemq.tar.gz -C activemq --strip-components=1

COPY conf/* /opt/activemq/conf/

EXPOSE 8161
EXPOSE 61616

CMD ["/opt/activemq/bin/activemq", "console"]


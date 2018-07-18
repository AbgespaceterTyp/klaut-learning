FROM  phusion/baseimage:0.9.17

RUN apt-get update \
    && apt-get install -y wget \
    && JAVA_VERSION_MAJOR=8 \
    && JAVA_VERSION_MINOR=181 \
    && JAVA_VERSION_BUILD=13 \
    && JAVA_DOWNLOAD_HASH=96a7b8442fe848ef90c96a2fad6ed6d1 \
    && mkdir -p /usr/lib/jvm \
    && cd /usr/lib/jvm \
    && wget -nv --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-b${JAVA_VERSION_BUILD}/${JAVA_DOWNLOAD_HASH}/jdk-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz \
    && tar xf jdk-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz \
    && rm jdk-${JAVA_VERSION_MAJOR}u${JAVA_VERSION_MINOR}-linux-x64.tar.gz \
    && update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.${JAVA_VERSION_MAJOR}.0_${JAVA_VERSION_MINOR}/bin/java" 1

EXPOSE 8080
COPY target/*jar backend.jar

ARG AMAZON_ACCESSKEY
ARG AMAZON_SECRETKEY
ARG REDIS_PASSWORD

ENV AMAZON_ACCESSKEY $AMAZON_ACCESSKEY
ENV AMAZON_SECRETKEY $AMAZON_SECRETKEY
ENV REDIS_PASSWORD $REDIS_PASSWORD
CMD java -jar -Dspring.profiles.active=prod -Damazon.aws.accesskey=$AMAZON_ACCESSKEY -Damazon.aws.secretkey=$AMAZON_SECRETKEY -Dspring.redis.password=$REDIS_PASSWORD backend*.jar
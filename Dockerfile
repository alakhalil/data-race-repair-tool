FROM ubuntu:latest

RUN apt-get -y update

#install gradle
RUN apt-get install -y wget
RUN apt-get install unzip
RUN rm -rf /var/lib/apt/lists/*
RUN apt-get update
RUN apt-get install ca-certificates
RUN apt-get install -y curl
RUN apt-get install -y gnupg
RUN apt-get install -y lsb-release
RUN apt-get install -y software-properties-common
RUN apt-get install -y opam
RUN add-apt-repository ppa:deadsnakes/ppa -y 

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install python3.8 -y 

RUN apt-get install -y pkg-config
RUN apt-get install -y openjdk-11-jdk

RUN wget https://downloads.gradle-dn.com/distributions/gradle-6.3-bin.zip
RUN unzip gradle-6.3-bin.zip
ENV GRADLE_HOME /gradle-6.3
ENV PATH $PATH:/gradle-6.3/bin

#compile and run app
RUN ./gradlew build --stacktrace 
RUN ./gradlew run --args='./out ./in'

#install and run infer
RUN mkdir infer && cd infer
RUN apt-get -y update
RUN apt-get -y install git
RUN git clone https://github.com/facebook/infer.git
RUN apt-get install tar
RUN apt-get install xz-utils
RUN VERSION=1.1.0; \
        curl -sSL "https://github.com/facebook/infer/releases/download/v$VERSION/infer-linux64-v$VERSION.tar.xz" \
        | tar -C /opt -xJ && \
        ln -s "/opt/infer-linux64-v$VERSION/bin/infer" /usr/local/bin/infer

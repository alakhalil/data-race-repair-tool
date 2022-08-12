FROM ubuntu:latest
MAINTAINER nehalmrashed@aol.com
ENV DEBIAN_FRONTEND noninteractive
ENV http_proxy 'http://mainproxy.ucm.conti.de:8980'
ENV https_proxy 'https://mainproxy.ucm.conti.de:8980'
RUN : \
    && apt-get -y update \
    && apt-get -y install git \
    && git clone https://github.com/facebook/infer.git \
    && apt-get install  ca-certificates \
    && apt-get install -y  curl \
    && apt-get install -y gnupg \
    && apt-get install -y lsb-release \
    && apt-get install -y software-properties-common \
    && echo \
        "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
        $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null \
    && apt-get install -y opam \
    && add-apt-repository ppa:deadsnakes/ppa -y 

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install python3.8 -y 
RUN : \
    && apt-get install -y pkg-config \
    && apt install -y default-jdk \
    && apt install -y default-jre \
    && VERSION=1.1.0; \
        curl -sSL "https://github.com/facebook/infer/releases/download/v$VERSION/infer-linux64-v$VERSION.tar.xz" \
        | tar -C /opt -xJ && \
        ln -s "/opt/infer-linux64-v$VERSION/bin/infer" /usr/local/bin/infer

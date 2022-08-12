FROM ubuntu:latest

RUN apt-get -y update
RUN apt-get -y install git
RUN git clone https://github.com/facebook/infer.git
RUN apt-get install  ca-certificates
RUN apt-get install -y gnupg
RUN apt-get install -y lsb-release
RUN apt-get install -y software-properties-common
RUN echo \
        "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
        $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
RUN apt-get install -y opam
RUN add-apt-repository ppa:deadsnakes/ppa -y 

ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get install python3.8 -y 
RUN apt-get install -y pkg-config
RUN apt install -y default-jdk
RUN apt install -y default-jre
RUN VERSION=1.1.0; \
        curl -sSL "https://github.com/facebook/infer/releases/download/v$VERSION/infer-linux64-v$VERSION.tar.xz" \
        | tar -C /opt -xJ && \
        ln -s "/opt/infer-linux64-v$VERSION/bin/infer" /usr/local/bin/infer

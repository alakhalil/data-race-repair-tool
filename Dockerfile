FROM openjdk:8-jdk

#install git
RUN apt-get install -y git
RUN git clone https://github.com/alakhalil/data-race-repair-tool.git

#install gradle
RUN wget https://downloads.gradle-dn.com/distributions/gradle-6.5-bin.zip
RUN unzip gradle-6.5-bin.zip
ENV GRADLE_HOME /gradle-6.5
ENV PATH $PATH:/gradle-6.5/bin

#compile and run app
WORKDIR yurl
RUN ./gradlew run --args='path1 path2'
ENTRYPOINT ["java", "-jar", "/yurlapp.jar"]
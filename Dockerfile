FROM ubuntu:16.10

RUN apt-get update -y
RUN apt-get dist-upgrade -y
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install oracle-java8-installer -y
RUN apt-get install -y maven

WORKDIR /app

ADD texts /app/bin/texts

# Prepare by downloading dependencies
ADD pom.xml /app/pom.xml
ADD assembly-zip.xml /app/assembly-zip.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package
ADD src /app/src
RUN ["mvn", "package"]

#ENV APP_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:2.2:evaluate -Dexpression=project.version|grep -Ev '(^\[|Download\w+:)')

EXPOSE 8080

WORKDIR /app/bin

#CMD java -jar words-counter-$APP_VERSION.jar
CMD java -jar words-counter-1.1.0.jar
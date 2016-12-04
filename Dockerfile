FROM ubuntu:16.10

RUN apt-get update -y
RUN apt-get dist-upgrade -y
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
RUN apt-get install oracle-java8-installer -y
RUN apt-get install -y maven

COPY . /app
COPY texts /app/bin/texts

WORKDIR /app

RUN APP_VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\[|Download\w+:)')

RUN mvn clean install

EXPOSE 8080

WORKDIR /app/bin

ENTRYPOINT ["java", "-jar", "bin/words-counter-$APP_VERSION.jar"]
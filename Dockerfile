FROM public.ecr.aws/docker/library/maven:3.9.9-amazoncorretto-23-al2023 AS build
WORKDIR /code

COPY pom.xml pom.xml
RUN mvn dependency:resolve --batch-mode

COPY src src
COPY assembly-zip.xml assembly-zip.xml
RUN mvn clean package -DskipTests --batch-mode

FROM build AS tests

COPY testTexts testTexts
RUN mvn test --batch-mode

FROM public.ecr.aws/docker/library/maven:3.9.9-amazoncorretto-23-al2023 AS development

WORKDIR /spring-boot-quartz-cluster-example

# Install development dependencies
RUN yum install -y git

ENTRYPOINT []
CMD []
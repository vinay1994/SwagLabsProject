# Use an existing Maven image as the base image
FROM maven:3.8.3-openjdk-17
MAINTAINER saal

RUN microdnf update \
 && microdnf install --nodocs wget unzip \
 && microdnf clean all \
 && rm -rf /var/cache/yum

# Set the Allure version
ARG ALLURE_VERSION=2.28.0

# Download and install Allure command-line tool
RUN wget -O allure.zip https://github.com/allure-framework/allure2/releases/download/${ALLURE_VERSION}/allure-${ALLURE_VERSION}.zip && \
    unzip allure.zip && \
    rm allure.zip && \
    mv allure-${ALLURE_VERSION} /opt/allure-${ALLURE_VERSION} && \
    ln -s /opt/allure-${ALLURE_VERSION}/bin/allure /usr/bin/allure

# Set the Allure command-line tool to the PATH
ENV PATH="/opt/allure-${ALLURE_VERSION}/bin:${PATH}"

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and src directory to the working directory
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline

# Expose the Allure report port
EXPOSE 8080

# Copy the source code
COPY src ./src
COPY testng.xml ./testng.xml

# Default group for tests
ENV TEST_GROUPS=smoke
ENV ENV_PATH=beta
ENV executionMode=server

# Run the Maven clean test command with the specified groups and environment
CMD ["sh", "-c", "mvn test -Dgroups=${TEST_GROUPS} -Denv.PATH=${ENV_PATH} allure:report"]
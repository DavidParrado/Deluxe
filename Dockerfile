# Use a base image with Debian Linux
FROM debian:bullseye

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  PATH="/usr/lib/jvm/java-21-openjdk-amd64/bin:${PATH}"

# Install wget to download the JDK
RUN apt-get update && \
  apt-get install -y wget && \
  rm -rf /var/lib/apt/lists/*

# Download and install Java 21
RUN wget -q https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb && \
  dpkg -i jdk-21_linux-x64_bin.deb && \
  rm jdk-21_linux-x64_bin.deb

# Install X11 dependencies
RUN apt-get update && apt-get install -y \
    xorg \
    xauth \
    && rm -rf /var/lib/apt/lists/*

# Set the DISPLAY environment variable
ENV DISPLAY=$DISPLAY

# Copy the application JAR file into the container
COPY out/artifacts/Deluxe_jar/Deluxe.jar /app/

# Set the working directory
WORKDIR /app

# Command to run the application
CMD ["java", "-jar", "Deluxe.jar"]
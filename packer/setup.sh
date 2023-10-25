#!/bin/bash

# Set the -e option
set -e

# Update package list
sudo apt-get update
sudo apt-get upgrade -y

# Install OpenJDK 17
sudo apt-get install -y openjdk-17-jdk

# Create user for app
sudo useradd csye6225
sudo chown csye6225:csye6225 /opt/webapp.jar
sudo chmod 0700 /opt/webapp.jar

sudo systemctl daemon-reload
sudo systemctl enable webapp.service

echo "Server setup completed."

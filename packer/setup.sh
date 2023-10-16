#!/bin/bash

# Set the -e option
set -e

#Setting enviornment variables
USER_CSV_FILE=/opt/user.csv

export MYSQL_DB
export MYSQL_USER
export MYSQL_PASSWORD
export USER_CSV_FILE

# Update package list
sudo apt-get update
sudo apt-get upgrade -y

# Install MariaDB server
sudo apt install -y mariadb-server

# Secure MariaDB installation
sudo mysql_secure_installation <<EOF

y
password
password
y
y
y
y
EOF

# Install unzip
sudo apt-get install -y unzip

# Install OpenJDK 17
sudo apt-get install -y openjdk-17-jdk

# Start MariaDB service
sudo systemctl start mariadb

# Enable MariaDB to start on boot
sudo systemctl enable mariadb

# Log into MariaDB and grant privileges
sudo mysql <<EOF
GRANT ALL ON *.* TO 'admin'@'localhost' IDENTIFIED BY 'password' WITH GRANT OPTION;
FLUSH PRIVILEGES;
exit
EOF

# Check MariaDB status
sudo systemctl status mariadb

# Check MariaDB version
sudo mysqladmin version

sudo mysql <<EOF
CREATE DATABASE $MYSQL_DB;
GRANT ALL PRIVILEGES ON $MYSQL_DB.* TO '$MYSQL_USER'@'localhost' IDENTIFIED BY '$MYSQL_PASSWORD';
FLUSH PRIVILEGES;
exit
EOF

echo "Server setup completed."

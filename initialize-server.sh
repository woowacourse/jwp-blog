#!/bin/bash

# Update repository
echo -e "Update packages...\n"
sudo apt update

# Install git
echo -e "\nInstall git...\n"
sudo apt install git

# Install openjdk java 8
echo -e "\nInstall openjdk java 8...\n"
sudo apt install openjdk-8-jdk

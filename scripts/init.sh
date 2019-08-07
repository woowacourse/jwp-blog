sudo snap install docker

# Run mysql container
sudo docker run \
    --name my-db \
    -itd \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_DATABASE=myblog \
    mysql:8.0.16

# Run jenkins container
sudo docker run \
    --name jenkins \
    -itd \
    -e JENKINS_USER=$(id -u) \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v $(pwd)/jenkins_home:/var/jenkins_home \
    -p 8000:8080 -p 50000:50000 \
    -u root \
    laterality/jenkins

# Show jenkins initial password
echo $(sudo docker exec -it jenkins cat /var/jenkins_home/secrets/initialAdminPassword)

sudo snap install docker

mkdir app
cd app

# Download docker-compose.yml
wget -O - https://raw.githubusercontent.com/Laterality/jwp-blog/step4/docker-compose.yml > docker-compose.yml
wget -O - https://raw.githubusercontent.com/Laterality/jwp-blog/step4/scripts/nginx.conf > nginx.conf

sudo docker-compose up -d

# Show jenkins initial password
echo Jenkins initial password: $(sudo docker-compose exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword)

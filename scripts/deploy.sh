#!/bin/bash
sudo docker stop $(sudo docker ps -a -q --filter name=elastic_hypatia)
sudo docker rm $(sudo docker ps -a -q --filter name=elastic_hypatia)
docker start -itd \
    --name myblog \
    -p 8080:8080 \
    --link my-db:db \
    laterality/myblog:latest
#!/bin/bash
container_id_to_stop = $(sudo docker ps -q --filter name=myblog)

if ${#container_id_to_stop}=0 then
    sudo docker stop $container_id_to_stop
fi

container_id_to_remove = $(sudo docker ps -a -q --filter name=myblog)

if ${#container_id_to_remove}=0 then
    sudo docker rm $container_id_to_remove
fi

docker start -itd \
    --name myblog \
    -p 8080:8080 \
    --link my-db:db \
    laterality/myblog:latest
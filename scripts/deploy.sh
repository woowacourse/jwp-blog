#!/bin/bash

container_id_stop=$(docker ps -q --filter name=blog)

if [ ${#container_id_stop}!=0 ] then
    docker stop blog
fi

container_id_remove=$(docker ps -a -q -filter name=blog)

if [ ${#container_id_remove}!=0 ] then
    docker rm blog
fi

docker restart blog

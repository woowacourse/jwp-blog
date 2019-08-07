#!/bin/bash
docker stop myblog || true && docker rm myblog || true
docker start -itd \
    --name myblog \
    -p 8080:8080 \
    --link my-db:db \
    laterality/myblog:latest
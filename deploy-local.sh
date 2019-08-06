#!/usr/bin/zsh

git push origin jwp-blog-step4
ssh -i "~/KEY-TRAINING-green4469-1.pem" ubuntu@13.125.71.112 '~/jwp-blog/deploy.sh'
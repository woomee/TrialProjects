#!/bin/bash

#
# SSL認証用のファイルを作成する
#
#  ./make_ssl_cert.sh  [IPアドレス]

#read -p "Host IP Address: " host
host=$1

# 環境変数を.envファイルへ出力
echo HOST_NAME=$host | tee .env

# DockerでCentOS7を起動してentrypoint.shにて実行する
docker-compose up centos7

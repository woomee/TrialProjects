#!/bin/bash

# 環境変数の読み込み
source .env

# 認証局CAの作成
if [ ! -e myCA.key ]; then
    echo Creating myCA.key
    openssl genrsa -out myCA.key 2048
fi
if [ ! -e myCA.cer ]; then
    openssl req -x509 -sha256 -new -key myCA.key -out myCA.cer -days 730 -subj /CN="My Custom CA"
fi

# サーバ証明書の作成
echo subjectAltName = DNS:$HOST_NAME, IP:$HOST_NAME | tee extrafile.txt
openssl genrsa -out mycert.key 2048
openssl req -new -out mycert.req -key mycert.key -subj /CN=$HOST_NAME
openssl x509 -req -sha256 -in mycert.req -out mycert.cer \
 -CAkey myCA.key -CA myCA.cer \
 -days 365 -CAcreateserial -CAserial serial \
 -extfile extrafile.txt &&

rm extrafile.txt mycert.req serial .env
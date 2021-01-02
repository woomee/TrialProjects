FROM node:15.5.0-alpine3.12

# Angularとfirebaseのcliをインストール
RUN npm install -g @angular/cli \
 # ng newするとgitでエラーになるため追加
 && apk add git \
 # pushd/popdなどが使えないためbashを追加
 && apk add bash \
 # コードフォーマッタ prettierを追加
 && yarn add prettier \
 # FireBase向けのcliをインストール
 && npm install -g firebase-tools


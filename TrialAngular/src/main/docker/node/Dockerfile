# Firebase FunctionはNode.jsは10 or 12
FROM node:12.20.1-alpine3.12

# Angularとfirebaseのcliをインストール
RUN \
 # ng newするとgitでエラーになるため追加
 apk add git \
 # pushd/popdなどが使えないためbashを追加
 && apk add bash \
 # Angularのクライアント
 && npm install -g @angular/cli@11.0.6 \
 # FireBase向けのcliをインストール
 && npm install -g firebase-tools@9.1.1 \
 # Firebaseのエミュレータで必要なjava
 && apk add openjdk11-jre-headless \
 # Firebase のエミュレータ
 && firebase setup:emulators:firestore 

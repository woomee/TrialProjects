FROM node:15.5.0-alpine3.12

# Angularとfirebaseのcliをインストール
RUN yarn add @angular/cli \
 # ng newするとgitでエラーになるため追加
 && apk add git \
 # pushd/popdなどが使えないためbashを追加
 && apk add bash \
 # コードフォーマッタ prettierを追加
 && yarn add prettier --dev --exact \
 && yarn add lint-staged \
 # FireBase向けのcliをインストール
 && yarn add firebase-tools


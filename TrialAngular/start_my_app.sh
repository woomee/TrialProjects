#!/bin/bash

# カレントへ移動
pushd `dirname $0` > /dev/null

# テスト用のRESTサーバの起動
pushd src/main/web/rest-test
node index.js &
popd

# アプリ本体を起動
pushd src/main/web/my-app
ng serve --host 0.0.0.0 --port 8080
popd

popd > /dev/null

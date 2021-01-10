#!/bin/bash

# カレントへ移動
pushd `dirname $0` > /dev/null

# 必要なファイルの入手
npm install 
# 日付ライブラリをインストール
npm install moment --save

ng add @ng-bootstrap/ng-bootstrap

# Firebase関連 (ログインしてから行う)
firebase login
ng add @angular/fire@next
# ReduxのAngular版
ng add @ngrx/schematics
npm install @ngrx/store @ngrx/effects @ngrx/store-devtools --save

popd
#!/bin/bash

# カレントへ移動
pushd `dirname $0` > /dev/null

# アプリ本体を起動
ng serve --host 0.0.0.0

popd
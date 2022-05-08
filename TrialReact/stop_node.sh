#/bin/bash

# カレントへ移動
pushd `dirname $0` > /dev/null

docker-compose down

popd > /dev/null
#!/bin/bash

# カレントへ移動
pushd `dirname $0` > /dev/null


docker-compose up --detach node

docker-compose exec node \
 bash -c "cd my-app && npm start"

popd > /dev/null

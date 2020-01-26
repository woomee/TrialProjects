
# 概要
Webを使った開発のトライアル。内容とポイントは以下のとおり。

## Dockerを使ってTomcatを起動
- volumesでWebContentを割り当てる
    ```yml:docker-compose.yml
    volumes: 
        - "./docker/tomcat/conf:/usr/local/tomcat/conf"
        - "./WebContent:/usr/local/tomcat/webapps/TrialWeb" 
    ```
- デバックできるようにjpdaで起動して8000番を開ける
    - 特にJava9以降はJPDA_ADDRESSの環境変数指定の方法が変更となっている
    ```yml:docker-compose.yml
    ports:
        - "8000:8000"
    environment: 
        - JPDA_ADDRESS=0.0.0.0:8000
    command: >
        bash -c "cd /usr/local/tomcat/bin &&
        catalina.sh jpda run"
    ```
## trial/auth
- Tomcatの認証について
- Basic認証とForm認証を試す
- Basic認証だとFireFoxの場合にログアウトはブラウザを閉じないと実現できない
- Form認証はセッション破棄によってログアウトを実現できる
    

## frame_reload
 - iframe中のページから外のフレームをリロードする
 - location.hrefではなくtop.locaton.hrefを使うことで外のフレームをリロード


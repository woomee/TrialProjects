
# 概要
Pentaho Data Integrationを使った開発のトライアル。サンプルは以下のとおり。
- 「ユーザ定義Java」のステップを利用したサンプル


# ポイント
-Pentaho DIのlibクラスから以下をクラスパスに追加
-- kettele-core-xxxx.jar
-- kettel-engine-xxxx.jar


# 参照
- http://type-exit.org/adventures-with-open-source-bi/2010/10/the-user-defined-java-class-step


# メモ
- Carteの起動・停止
-- 起動 (ポート番号
```carte localhost 9080

-- 停止
```carte localhost 9080 -s -u cluster -p cluster
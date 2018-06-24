
# 概要
Pentaho Data Integrationを使った開発のトライアル。サンプルは以下のとおり。
- 「ユーザ定義Java」のステップを利用したサンプル


# ポイント
- Pentaho DIのlibクラスから以下をクラスパスに追加
 - kettele-core-xxxx.jar
 - kettel-engine-xxxx.jar
  - 現状は7.1.12を利用
  - ソースは以下より取得
   - https://github.com/pentaho/pentaho-kettle/tree/7.1.0.13
- 以下のTransformClassBaseを継承してprocessRow()メソッドを実装する
 org.pentaho.di.trans.steps.userdefinedjavaclass.TransformClassBase
- pentaho以外のimportは追記する必要あり。ただ、pentahoパッケージでも追記する必要があるもの有り。
 - 例. org.pentaho.di.core.row.value.ValueMetaStringなど
 - 以下の*を利用して追記するとよい。
```java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.pentaho.di.core.row.value.*;
```

# 参照
- ユーザ定義Java (udjc)の使い方
 - http://type-exit.org/adventures-with-open-source-bi/2010/10/the-user-defined-java-class-step
- ElasticsearchにPDI(Spoon)を使って一括登録を行う方法
 - https://qiita.com/windows222/items/b9226e1b39549b0a59e4


# メモ
## Carte
- Carteの起動・停止
 - 起動 (ポート番号 9080)
  `> carte localhost 9080`
 - 停止
 `> carte localhost 9080 -s -u cluster -p cluster`

## Derby
- DerbyによるJDBCの接続
 - url: jdbc:derby://localhost/testDB;create=true
 - driver: org.apache.derby.jdbc.ClientDriver
-ij.batがSQL実行のコマンドインタフェース
 - 接続: `> connect 'jdbc:derby://localhost/testDB;create=true';`
- PDIに入っているDerbyのJDBCドライバはderby-10.2で古いため文字列変換でエラーとなってしまう。
 - ⇒derby-10.14に変更すると解決

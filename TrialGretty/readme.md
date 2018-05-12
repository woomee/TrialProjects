
# 概要
Grettyを使ってWebアプリの開発、テストを行う
JUnitだけでなく、Seleniumを使ったテストも行っている

# 実行
- gradle copyDependencies  必要なjarをWEB-INF/libに取得
- gradle appRun  アプリ実行
-- http://localhost:8080/TrialGretty/ にて接続
- gradle build   ビルド
- gradle test

# ポイント
- Eclipseのプロジェクトファセットで「動的Webプロジェクト」にする
-- コンテキストをsrc/main/webapp/WEB-INF/libとする

## 問題ポイント
- Tomcatが立ち上がらない。Grettyの問題。

# 参照
https://guides.gradle.org/building-java-web-applications/
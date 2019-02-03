# SpringBootを使ったパス変更
## 概要
- application.ymlの`app: {アプリ名}`で指定しアプリ名のURLへリクエストを転送する
- アプリ名 common,  customとして2つサーブレットがあり、通常のリクエストで返却される内容を以下に示す
    1.  /common/greeting
        ```
        $ curl -s http://localhost:8080/common/greeting
        {"id":1,"content":"Common: Hello, World!"}
            ⇒ "Common"の内容
        ```
    1.  /custom/greeting
        ```
        $ curl -s http://localhost:8080/custom/greeting
        {"id":15,"content":"Custom: Hello, World!","dateTime":"Mon Feb 04 09:41:00 JST 2019"}
            ⇒ "Customの内容"
        ```
- さらにhtmlファイルのフォルダ構造で、common, customで分かれて以下のようになっている
    - customに関してはhtmlのみ変更している

    ```
    webapp
     ├─common
     │      hello.js
     │      index.html
     │
     └─custom
            index.html      ⇒ customはhtmlのみ変更
    ```

## 例 app = "custom"の時
- commonサーブレットの呼び出し
```
$ curl -s http://localhost:1234/common/greeting
{"id":15,"content":"Custom: Hello, World!","dateTime":"Mon Feb 04 00:41:00 JST 2019"}
 ⇒ リクエストがcommonでも”Custom"が返る!!
```

- index.htmlの呼び出し
```
$ curl -s http://localhost:1234/common/index.html
<!doctype html>
<html ng-app="demo">
        <head>
                <title>Hello AngularJS</title>
                <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular.min.js"></script>
        <script src="hello.js"></script>
        </head>

        <body>
                <h1>CUSTOM</h1>
                <div ng-controller="Hello">
                        <p>The ID is {{greeting.id}}</p>
                        <p>The content is {{greeting.content}}</p>
                </div>
        </body>
</html>
 ⇒ リクエストがcommon/index.htmlでもcustom/index.htmlが返る!!
```

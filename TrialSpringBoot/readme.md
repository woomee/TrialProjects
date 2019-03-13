# Spring Frameworkのサンプル
- 参考: https://spring.io/guides/gs/rest-service/#initial
- http://localhost:8080/greeting にアクセスすると以下のようなJSONを返却
    - ` {"id":11,"content":"Hello, World!"} `
- JUnitとWebTestClientを使ったテストも実施

# ビルドと実行方法
```
gradle build
gradle bootRun
```
- 又は、EclipseでApplication.javaを起動する。デバック起動すればブレークポイントで止まる
    - http://javatechnology.net/spring/spring-boot-eclipse-debug/


# SpringをGradleで利用するまで
-参考:
    - https://qiita.com/kentfordev/items/af5298f58a505882f1e7

1. Gradleのバージョンを確認。ここでは5.1.1を想定
    ```
    >gradle --version
    ------------------------------------------------------------
    Gradle 5.1.1
    ------------------------------------------------------------
    ```
1. --type=java-applicationでプロジェクトを作成
    ```
    mkdir xxx
    cd xxx
    gradle init --type java-application --dsl groovy --test-framework junit
    ```
1. 利用する開発ツールとしてid 'eclipse'のpluginを追加
    ```
    plugins {
        (省略)....
    id 'eclipse'
    }
    ```
    1. gradle eclipseを実行してプロジェクトファイルを生成してEclipse上からインポート
        ```
        > gradle eclipse
        ```
    1. インポート後にプロジェクトにGradleのネイチャーを追加
1. リポジトリにmavenを追加
    ```
    repositories {
        // Use jcenter for resolving your dependencies.
        // You can declare any Maven/Ivy/file repository here.
        jcenter()
        mavenCentral()
    }
    ```
1. build.gradleに先頭(pluginの前）に追記
    - pluginの前に追記しないといけないので注意
	```
	// Spring Boot
	buildscript {
	    def springBootVersion = '2.0.3.RELEASE'
	    repositories {
	        mavenCentral()
	    }
	    dependencies {
	        classpath 'org.springframework.boot:spring-boot-gradle-plugin:' + springBootVersion
	    }
	}
	plugins {
	    ...
	```
1. 更にSpring用の設定を追加する。springBootApplicationNameは開発する名前に合わせる
    ```
    // For SpringBoot
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'

    def springBootApplicationName = 'TrialSpring'
    def applicationVersion = '0.0.1'
    bootJar {
        baseName = springBootApplicationName
        version = applicationVersion
    }
    bootJar {
        baseName = springBootApplicationName
        version = applicationVersion
    }


    dependencies {
        compile 'org.springframework.boot:spring-boot-starter-web'
        compile 'org.springframework.boot:spring-boot-devtools'
        testCompile 'org.springframework.boot:spring-boot-starter-test'
    }
    ```
1. 更にテストでWebTestClientを利用するには以下をbuild.gradleに追記する
    ```
        dependencies {
            ...
            // WebTestClient
            testCompile('io.projectreactor:reactor-test')
            testCompile('org.springframework.boot:spring-boot-starter-webflux')
            ...
    ```

# SpringBootをTomcatで利用するには
- @SpringBootApplicationのクラスはSpringBootServletInitializerクラスを継承すること
- configureメソッドをoverrideすること
- (参考) https://qiita.com/NagaokaKenichi/items/3f191aeb6f161101d5f6

# その他
- Spring Boot 使い方メモ
	- https://qiita.com/opengl-8080/items/05d9490d6f0544e2351a
		- DBでの利用
		- WebJarsを利用するとJSもGradleで取得できる
- Springにおけるapplication.properties(yml)での設定値
    - https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html



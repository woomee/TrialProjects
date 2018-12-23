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

# SpringをGradleで利用するまで
-参考: https://qiita.com/kentfordev/items/af5298f58a505882f1e7

1. --type=java-applicationでプロジェクトを作成
    ```
    mkdir xxx
    cd xxx
    gradle init --type java-application --dsl groovy --test-framework junit
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
        id 'java-library'
        ...
    ```
1. 更にSpring用の設定を追加する
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

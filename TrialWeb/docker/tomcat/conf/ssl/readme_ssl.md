# 概要
- https通信においてブラウザでエラーにならない信頼されて証明書を作成する
- ブラウザでエラーにならない条件
    - 鍵長が2048bit以上
    - SHA2のハッシュアルゴリズムを利用
    - CommonName(CA)とSubject Alternative Name(SAN)にホスト名が設定されている
    - サーバの有効期限は825日以下であること
    - (参考) https://support.apple.com/ja-jp/HT210176
- 証明書の作成方法は以下のTip #5をベースに実行 
    - https://blog.httpwatch.com/2013/12/12/five-tips-for-using-self-signed-ssl-certificates-with-ios/

# OpenSSLがインストールされたCentOS7を準備
docker-compose.exe up -d
docker-compose.exe exec centos7 bash
cd /mnt/host

# 認証局(CA)の準備
- 認証局の鍵(myCA.key)を2048bitで作成して証明書(myCA.cer)を作成する
```
openssl genrsa -out myCA.key 2048
openssl req -x509 -sha256 -new -key myCA.key -out myCA.cer -days 730 -subj /CN="My Custom CA"
```

# サーバの証明書の作成
## Subject Alternative Name(SAN)用のファイルを準備
- extrafile.txtを作成
- DNSにはドメイン名、IPアドレスをIPを指定する (xxx.xxx.xxx.xxxは環境に応じて書き換える)
    ```
    subjectAltName = DNS:test.com, IP:xxx.xxx.xxx.xxx 
    ```
## 証明書の作成
- 認証局に証明書要求(mycert1.req)を与えて証明書(mycert1.cer)を得る
```
openssl genrsa -out mycert1.key 2048
openssl req -new -out mycert1.req -key mycert1.key -subj /CN=xxx.xxx.xxx.xxx
openssl x509 -req -sha256 -in mycert1.req -out mycert1.cer \
 -CAkey myCA.key -CA myCA.cer -days 365 \
 -CAcreateserial -CAserial serial \
 -extfile extfile.txt
```
## 証明書の中身を確認
openssl x509 -text -in mycert1.cer 


# Tomcatへ設定
- server.xmlに以下を記載する
    - certificateKeyFileにサーバのキー
    - certificateFileにサーバの証明書
    - ファイルパスはCATALINA_BASEからの相対パスかルートからの絶対パス
    ```
    <Connector port="8443" protocol="org.apache.coyote.http11.Http11AprProtocol"
    maxThreads="150" SSLEnabled="true" >
    <UpgradeProtocol className="org.apache.coyote.http2.Http2Protocol" />
    <SSLHostConfig>
        <Certificate certificateKeyFile="conf/ssl/trial_03/mycert1.key"
                     certificateFile="conf/ssl/trial_03/mycert1.cer"
                     type="RSA" />
    </SSLHostConfig>
    </Connector>
    ```

# Windowsへの設定
1. myCA.cerを右クリックして「証明書のインストール」
2. 保存場所を 現在のユーザ/ローカルコンピュータを洗濯
3. 「証明書をすべて次のストアに配置する」を選び、「参照」->「信頼されたルート証明機関」

# iPhone(iOS13)の場合
1. myCA.cerをダウンロードできる位置に置く
    - WebContent/vr 以下など
2. SafariにてmyCA.cerにアクセスするとダウンロードされる
3. 「設定」にて「プロファイルがダウンロードされました」が出ているので選択して「インストール」
    - パスコードを求められたら入力
4. 「設定」->「一般」->「情報」->「証明書信頼設定」よりインストールした証明書をONにする
- (参考): https://www.ibm.com/support/knowledgecenter/ja/SSHS8R_7.0.0/com.ibm.worklight.installconfig.doc/admin/t_installing_root_CA_iOS.html
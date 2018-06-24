
@rem Derbyを起動します。
@rem 環境変数DERBY_HOMEが設定されていること。
@rem DERBY_HOMEのひとつ上の階層のdataディレクトリを利用します。

set DERBY_DATA=%DERBY_HOME%\..\data

mkdir %DERBY_DATA%
cd /d %DERBY_DATA%

call %DERBY_HOME%\bin\startNetworkServer.bat


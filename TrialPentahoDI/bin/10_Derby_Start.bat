
@rem Derby���N�����܂��B
@rem ���ϐ�DERBY_HOME���ݒ肳��Ă��邱�ƁB
@rem DERBY_HOME�̂ЂƂ�̊K�w��data�f�B���N�g���𗘗p���܂��B

set DERBY_DATA=%DERBY_HOME%\..\data

mkdir %DERBY_DATA%
cd /d %DERBY_DATA%

call %DERBY_HOME%\bin\startNetworkServer.bat



@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Primitive_SocketClient.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

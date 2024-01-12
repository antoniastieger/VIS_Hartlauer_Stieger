
@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Udp_SocketClient.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

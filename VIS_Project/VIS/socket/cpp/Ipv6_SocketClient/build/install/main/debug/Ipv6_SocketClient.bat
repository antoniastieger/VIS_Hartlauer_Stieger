
@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Ipv6_SocketClient.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

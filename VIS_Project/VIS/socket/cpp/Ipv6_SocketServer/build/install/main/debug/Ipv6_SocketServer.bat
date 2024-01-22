
@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Ipv6_SocketServer.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

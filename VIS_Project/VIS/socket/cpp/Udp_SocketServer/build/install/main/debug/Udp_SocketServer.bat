
@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Udp_SocketServer.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL


@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Http_SocketServer.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL


@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Environment_SocketServer.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

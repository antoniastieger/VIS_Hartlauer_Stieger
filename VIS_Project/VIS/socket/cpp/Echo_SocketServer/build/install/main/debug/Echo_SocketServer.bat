
@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Echo_SocketServer.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

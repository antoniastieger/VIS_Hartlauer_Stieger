
@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\Primitive_SocketServer.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

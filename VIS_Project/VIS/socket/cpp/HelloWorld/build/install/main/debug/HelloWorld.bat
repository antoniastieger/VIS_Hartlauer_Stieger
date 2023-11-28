
@echo off
SETLOCAL
SET PATH=%PATH%
CALL "%~dp0lib\HelloWorld.exe" %*
EXIT /B %ERRORLEVEL%
ENDLOCAL

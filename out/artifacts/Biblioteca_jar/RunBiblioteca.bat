@echo off
chcp 65001 > nul
set SCRIPT_DIR=%~dp0
java -Dfile.encoding=UTF-8 -jar "%SCRIPT_DIR%\Biblioteca.jar"

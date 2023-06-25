@echo off
chcp 65001 > nul
set jarPath=
for /r %%i in (*.jar) do (
    if /I "%%~nxi"=="Biblioteca.jar" (
        set "jarPath=%%i"
        goto :runJar
    )
)
echo Biblioteca.jar not found.
goto :eof

:runJar
java -Dfile.encoding=UTF-8 -cp "jakarta.activation-2.0.1.jar;jakarta.activation-api-2.1.1.jar;jakarta.mail-2.0.1.jar;jakarta.mail-api-2.1.1.jar;%jarPath%" com.mycompany.biblioteca.Biblioteca
pause
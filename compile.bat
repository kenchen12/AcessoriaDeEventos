@ECHO OFF
SET comp=javac -encoding utf-8 -s src/ -d bin

FOR /R src\ %%F in (*.java) DO CALL :concat %%F
ECHO Compiling source files...
CALL %comp%
EXIT /B 0

:concat
SET comp=%comp% %~1
EXIT /B
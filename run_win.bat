@ECHO OFF
SET r=java -cp bin;ojdbc14.jar assessoria.Main

CALL %r%
EXIT /B 0
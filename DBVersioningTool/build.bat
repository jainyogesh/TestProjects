@echo off
set CUR_DIR=%cd%
call mvn clean install 
@rem dependency:copy-dependencies -DoutputDirectory=%CUR_DIR%\lib
pause

:end
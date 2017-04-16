@echo off

echo DB Versioning Tool - Extract SQLs

set CUR_DIR=%cd%
call :clean

echo Extracting SQLs from : %APSF_HOME%
echo.

pushd %APSF_HOME%
call mvn org.apache.maven.plugins:maven-dependency-plugin:2.3:tree -Dverbose=true -DoutputFile=dependency.txt 
popd
goto process

:process 
pushd %CUR_DIR%

setlocal ENABLEDELAYEDEXPANSION
if defined CLASSPATH (set CLASSPATH=%CLASSPATH%;%CUR_DIR%\conf;%CUR_DIR%\dbDependencies;.) else (set CLASSPATH=%CUR_DIR%\conf;%CUR_DIR%\dbDependencies;.)
FOR /R .\lib %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G

java com.aciworldwide.utility.dbdependency.ProcessCompleteProject %APSF_HOME% %CUR_DIR%\dbDependencies
echo SQLs extracted to : %CUR_DIR%\dbDependencies
pause
goto end


:clean
if exist %CUR_DIR%\dbDependencies rmdir /S /Q %CUR_DIR%\dbDependencies
mkdir %CUR_DIR%\dbDependencies

if exist %CUR_DIR%\log rmdir /S /Q %CUR_DIR%\log
mkdir %CUR_DIR%\log

:end
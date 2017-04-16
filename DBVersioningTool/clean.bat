@echo off

echo Cleaning the database


:main
echo ------------Requesting to clean the following DB------------
For /F "tokens=1* delims==" %%A IN (conf/jdbc.properties) DO (
	IF "%%A"=="flyway.url" echo JDBC URL : %%B
	IF "%%A"=="flyway.user" echo JDBC USERNAME : %%B
	IF "%%A"=="flyway.password" echo JDBC PASSWORD : %%B
	IF "%%A"=="flyway.schemas" echo SCHEMA NAME : %%B
	)
echo ----------------------------------
SET /P ANSWER=This operation will format the database. Do you want to continue (Yes/No)?
set CUR_DIR=%cd%
if /i {%ANSWER%}=={yes} (goto :yes)
if /i {%ANSWER%}=={no} (goto :no)
goto :main

:yes
cls
echo ----------------------------------
echo ---------VERIFY DB DETAILS--------
echo ----------------------------------
For /F "tokens=1* delims==" %%A IN (conf/jdbc.properties) DO (
	IF "%%A"=="flyway.url" echo JDBC URL : %%B
	IF "%%A"=="flyway.user" echo JDBC USERNAME : %%B
	IF "%%A"=="flyway.password" echo JDBC PASSWORD : %%B
	IF "%%A"=="flyway.schemas" echo SCHEMA NAME : %%B
	)
echo ----------------------------------
echo This operation will format the database and completely erase all the data in it. This operation CANNOT be reversed. 
SET /P ANSWER=Are you sure you want to continue (Yes/No)?
if /i {%ANSWER%}=={yes} (goto :finalYES)
if /i {%ANSWER%}=={no} (goto :no)
goto :yes

:finalYES

setlocal ENABLEDELAYEDEXPANSION
if defined CLASSPATH (set CLASSPATH=%CLASSPATH%;%CUR_DIR%\conf;%CUR_DIR%\dbDependencies;.) else (set CLASSPATH=%CUR_DIR%\conf;%CUR_DIR%\dbDependencies;.)
FOR /R .\lib %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G

java com.aciworldwide.utility.dbdependency.FlywayWrapper clean %CUR_DIR%\dbDependencies no
pause....
exit /b 0




:no
pause....
exit /b 1
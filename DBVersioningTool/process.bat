@echo off

echo DB Versioning Tool

set CUR_DIR=%cd%
call :clean
echo ----------------------------------------------------
echo ------------------VERIFY DB DETAILS-----------------
echo ----------------------------------------------------
For /F "tokens=1* delims==" %%A IN (conf/jdbc.properties) DO (
	IF "%%A"=="flyway.url" echo JDBC URL : %%B
	IF "%%A"=="flyway.user" echo JDBC USERNAME : %%B
	IF "%%A"=="flyway.password" echo JDBC PASSWORD : %%B
	IF "%%A"=="flyway.schemas" echo SCHEMA NAME : %%B
	IF "%%A"=="flyway.schemas" set SCHEMA_USER_NAME=%%B
	)
echo ----------------------------------------------------
echo.
echo.

echo Current Codebase is at : %APSF_HOME%
echo.
echo.
echo ----------------------------Generate Dependency Tree----------------------------
echo This operation generates the dependency structure for all the modules.
echo This operation MUST be performed if new modules have added to the APSF Codebase
echo If not sure, continue with this operation
echo --------------------------------------------------------------------------------
echo.
SET /P ANSWER=Generate New Dependency Tree? (Yes/No)?
if /i {%ANSWER%}=={yes} ( 
	pushd %APSF_HOME%
	call mvn dependency:tree -Dverbose=true -DoutputFile=dependency.txt 
	popd
	goto process
)
if /i {%ANSWER%}=={no} (
	goto process
)



:process 
pushd %CUR_DIR%

setlocal ENABLEDELAYEDEXPANSION
if defined CLASSPATH (set CLASSPATH=%CLASSPATH%;%CUR_DIR%\conf;%CUR_DIR%\dbDependencies;.) else (set CLASSPATH=%CUR_DIR%\conf;%CUR_DIR%\dbDependencies;.)
FOR /R .\lib %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G

java com.aciworldwide.utility.dbdependency.ProcessCompleteProject %APSF_HOME% %CUR_DIR%\dbDependencies

if defined SCHEMA_USER_NAME (
	FOR /R .\dbDependencies %%G IN (sql\*V1.sql) DO (
		echo alter session set current_schema = %SCHEMA_USER_NAME%;>tempFile
		type %%G>>tempFile
		type tempFile>%%G
		del tempfile
	)

)

if [%1]==[] goto normal
if ""%1"" == ""skiptests"" goto skiptests

:normal
java com.aciworldwide.utility.dbdependency.FlywayWrapper apply %CUR_DIR%\dbDependencies no
pause
goto end

:skiptests
echo.
echo SKIPPING TESTS
echo.
java com.aciworldwide.utility.dbdependency.FlywayWrapper apply %CUR_DIR%\dbDependencies yes
pause
goto end


:clean
if exist %CUR_DIR%\dbDependencies rmdir /S /Q %CUR_DIR%\dbDependencies
mkdir %CUR_DIR%\dbDependencies

if exist %CUR_DIR%\log rmdir /S /Q %CUR_DIR%\log
mkdir %CUR_DIR%\log

:end
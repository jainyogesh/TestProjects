@echo off
if [%4]==[] goto usage

if ""%4"" == ""jar"" goto processJar
if ""%4"" == ""war"" goto processWar
goto usage

:processJar
echo In processJar
set CUR_DIR=%cd%
call mvn dependency:tree -Dverbose=true -DoutputFile=%CUR_DIR%\dependency.txt 
@rem dependency:unpack-dependencies -Dmdep.unpack.includes="**\*.sql" -DoutputDirectory="%CUR_DIR%\dbDependencies" -DoverWriteSnapshots=true -Dmdep.useSubDirectoryPerArtifact=true -Ddependency.groupId=%1 -Ddependency.artifactId=%2 -Ddependency.version=%3 -Ddependency.type=%4
set CLASSPATH=%CLASSPATH%;%CUR_DIR%\target\db-dependency-1.0-SNAPSHOT.jar;%CUR_DIR%\lib\FileUtil-0.0.1-SNAPSHOT.jar
java com.aciworldwide.utility.dbdependency.ProcessModule %CUR_DIR%\dependency.txt %CUR_DIR%\dbDependencies
goto end

:processWar
echo In processWar
set CUR_DIR=%cd%
call mvn dependency:unpack-dependencies -Dmdep.unpack.includes="**\%1\**\pom.xml,**\*.sql" -Dmdep.unpack.excludes="**\FileUtil*" -DoutputDirectory="%CUR_DIR%\dbDependencies" -DoverWriteSnapshots=true -Dmdep.useSubDirectoryPerArtifact=true -Ddependency.groupId=%1 -Ddependency.artifactId=%2 -Ddependency.version=%3 -Ddependency.type=%4
echo  %CUR_DIR%\dbDependencies\%2-%3-%4\META-INF\maven\%1\%2
rename %CUR_DIR%\dbDependencies\%2-%3-%4 temp
mkdir %CUR_DIR%\dbDependencies\temp1
xcopy /e %CUR_DIR%\dbDependencies\temp %CUR_DIR%\dbDependencies\temp1
pushd  %CUR_DIR%\dbDependencies\temp\META-INF\maven\%1\%2
call mvn dependency:tree -DoutputFile=%CUR_DIR%\dependency.txt dependency:unpack-dependencies -Dmdep.unpack.includes="**\*.sql" -DoutputDirectory="%CUR_DIR%\dbDependencies" -DoverWriteSnapshots=true -Dmdep.useSubDirectoryPerArtifact=true
popd %CUR_DIR%
rename %CUR_DIR%\dbDependencies\temp1 %2-%3-%4
set CLASSPATH=%CLASSPATH%;%CUR_DIR%\target\db-dependency-1.0-SNAPSHOT.jar;%CUR_DIR%\lib\FileUtil-0.0.1-SNAPSHOT.jar
java com.aciworldwide.utility.dbdependency.ProcessModule %CUR_DIR%\dependency.txt %CUR_DIR%\dbDependencies
rmdir /S /Q %CUR_DIR%\dbDependencies\temp
goto end

:usage
echo Invalid number of Input Arguments!!
echo Usage: 
echo extractDBScripts.bat dependency.groupId dependency.artifactId dependency.version dependency.type
echo sample:
echo extractDBScripts.bat com.aciworldwide.aps.adf.emf EMF-Report-Impl 1.1.0.1-SNAPSHOT jar
goto :eof

:end
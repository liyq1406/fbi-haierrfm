@ECHO OFF

    REM …Ë÷√CLASSPATH
    SET PRJPATH=F:\fbi-brzfdc\out\artifacts\brzfdc

    SET classpath=%JAVA_HOME%\jre\lib\alt-rt.jar;%JAVA_HOME%\jre\lib\charsets.jar;%JAVA_HOME%\jre\lib\rt.jar

    SET classpath=%CLASSPATH%;%PRJPATH%\WEB-INF\classes

    FOR %%i IN ("%PRJPATH%\WEB-INF\lib\*.jar") DO SET CLASSPATH=!CLASSPATH!;%%~fi
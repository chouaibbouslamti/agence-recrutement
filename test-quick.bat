@echo off
echo ========================================
echo Test Rapide du Projet
echo ========================================
echo.

echo [1/3] Verification de Java...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Java n'est pas installe ou pas dans le PATH
    pause
    exit /b 1
)
echo OK - Java detecte
echo.

echo [2/3] Compilation du projet...
call mvn clean compile -q
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: La compilation a echoue
    pause
    exit /b 1
)
echo OK - Compilation reussie
echo.

echo [3/3] Verification de MySQL...
mysql --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ATTENTION: MySQL n'est pas detecte dans le PATH
    echo Mais l'application peut fonctionner si MySQL est installe
) else (
    echo OK - MySQL detecte
)
echo.

echo ========================================
echo Tests preliminaires termines!
echo ========================================
echo.
echo Pour lancer l'application, executez:
echo   mvn spring-boot:run
echo.
echo Ou double-cliquez sur: test-application.bat
echo.
pause


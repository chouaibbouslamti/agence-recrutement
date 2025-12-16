@echo off
echo ========================================
echo Test de l'application Spring Boot + JavaFX
echo ========================================
echo.
echo Compilation en cours...
call mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: La compilation a echoue
    pause
    exit /b 1
)
echo.
echo Compilation reussie!
echo.
echo Demarrage de l'application...
call mvn spring-boot:run
pause


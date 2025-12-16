@echo off
echo ========================================
echo Demarrage de l'Application
echo Agence de Recrutement
echo ========================================
echo.

REM Vérifier que nous sommes dans le bon répertoire
if not exist "pom.xml" (
    echo ERREUR: Le fichier pom.xml n'est pas trouve
    echo Assurez-vous d'etre dans le dossier du projet
    pause
    exit /b 1
)

REM Vérifier Java
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Java n'est pas installe ou pas dans le PATH
    pause
    exit /b 1
)

REM Vérifier Maven
mvn --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Maven n'est pas installe ou pas dans le PATH
    pause
    exit /b 1
)

echo [1/2] Verification des prerequis...
echo   - Java: OK
echo   - Maven: OK
echo.

echo [2/2] Demarrage de l'application...
echo.
echo ========================================
echo L'application va demarrer...
echo Une fenetre JavaFX va s'afficher
echo Pour arreter, fermez la fenetre ou appuyez sur Ctrl+C
echo ========================================
echo.

REM Lancer l'application
mvn spring-boot:run

echo.
echo Application arretee.
pause


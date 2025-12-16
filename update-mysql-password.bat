@echo off
echo ========================================
echo Mise a jour du mot de passe MySQL
echo ========================================
echo.
set /p NEW_PASSWORD="Entrez le nouveau mot de passe MySQL root: "
echo.
echo Mise a jour de application.properties...
echo.

powershell -Command "(Get-Content 'src\main\resources\application.properties') -replace 'spring.datasource.password=.*', 'spring.datasource.password=%NEW_PASSWORD%' | Set-Content 'src\main\resources\application.properties'"

echo.
echo Mot de passe mis a jour dans application.properties
echo.
echo Test de connexion MySQL...
mysql -u root -p%NEW_PASSWORD% -e "SELECT 'Connexion reussie!' AS Status;" 2>nul
if %ERRORLEVEL% EQU 0 (
    echo.
    echo SUCCESS: La connexion MySQL fonctionne!
    echo Vous pouvez maintenant lancer l'application avec: mvn spring-boot:run
) else (
    echo.
    echo ERREUR: La connexion a echoue. Verifiez que:
    echo   1. MySQL est demarre
    echo   2. Le mot de passe est correct
    echo   3. L'utilisateur root existe
)
echo.
pause



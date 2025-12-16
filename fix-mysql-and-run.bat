@echo off
echo ========================================
echo Fix MySQL Password and Run Project
echo ========================================
echo.

REM Try to reset MySQL root password
echo [1/4] Attempting to reset MySQL root password...
echo.

REM Create SQL file to reset password
echo ALTER USER 'root'@'localhost' IDENTIFIED BY 'root123'; > reset_password.sql
echo FLUSH PRIVILEGES; >> reset_password.sql

REM Try to connect and reset (this might fail if we can't access)
echo Attempting to reset password to 'root123'...
mysql -u root --password="" -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root123'; FLUSH PRIVILEGES;" 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Password reset successful!
    set MYSQL_PASSWORD=root123
    goto :update_config
)

REM Try with current password
mysql -u root -pPpvf5608? -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'root123'; FLUSH PRIVILEGES;" 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Password reset successful!
    set MYSQL_PASSWORD=root123
    goto :update_config
)

REM If both fail, try creating a new user
echo.
echo [2/4] Creating new MySQL user 'agence_user'...
mysql -u root --password="" -e "CREATE USER IF NOT EXISTS 'agence_user'@'localhost' IDENTIFIED BY 'password123'; GRANT ALL PRIVILEGES ON agence_recrutement.* TO 'agence_user'@'localhost'; FLUSH PRIVILEGES;" 2>nul
if %ERRORLEVEL% EQU 0 (
    echo New user created successfully!
    set MYSQL_USER=agence_user
    set MYSQL_PASSWORD=password123
    goto :update_config
)

echo.
echo WARNING: Could not automatically reset MySQL password.
echo Please manually reset MySQL password or update application.properties
echo.
echo Options:
echo 1. If using XAMPP, try empty password: spring.datasource.password=
echo 2. Reset MySQL password manually
echo 3. Create new user: CREATE USER 'agence_user'@'localhost' IDENTIFIED BY 'password123';
echo.
pause
exit /b 1

:update_config
echo.
echo [3/4] Updating application.properties...
if defined MYSQL_USER (
    powershell -Command "(Get-Content 'src\main\resources\application.properties') -replace 'spring.datasource.username=.*', 'spring.datasource.username=%MYSQL_USER%' | Set-Content 'src\main\resources\application.properties'"
)
powershell -Command "(Get-Content 'src\main\resources\application.properties') -replace 'spring.datasource.password=.*', 'spring.datasource.password=%MYSQL_PASSWORD%' | Set-Content 'src\main\resources\application.properties'"
echo Configuration updated!
echo.

:run_project
echo [4/4] Starting the project...
echo.
call demarrer.bat



@echo off
title FastCar Location - Démarrage Client
color 0A
cls

echo ========================================
echo      FASTCAR LOCATION - CLIENT
echo ========================================
echo.

echo [1/4] Vérification de Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java n'est pas installé !
    echo.
    echo Installation automatique de Java...
    start https://www.java.com/fr/download/
    echo.
    echo Après installation de Java, relancez ce programme.
    pause
    exit
)

echo ✅ Java est installé
echo.

echo [2/4] Démarrage du système...
cd backend
start "FastCar Backend" /MIN cmd /c "mvn spring-boot:run"

echo [3/4] Attente du démarrage (30 secondes)...
echo Veuillez patienter...
timeout /t 30 /nobreak >nul

echo [4/4] Ouverture de l'interface...
cd ..
start "" frontend\index.html

echo.
echo ========================================
echo      ✅ APPLICATION PRÊTE !
echo ========================================
echo.
echo L'application est maintenant ouverte.
echo.
echo Pour fermer : Fermez simplement le navigateur.
echo.
pause
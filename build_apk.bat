@echo off
echo NetSpeed Meter APK Builder
echo ==========================
echo.

REM Check if gradle-wrapper.jar exists
if not exist "gradle\wrapper\gradle-wrapper.jar" (
    echo gradle-wrapper.jar not found!
    echo.
    echo Please download it manually:
    echo 1. Open browser to: https://github.com/gradle/gradle/raw/v8.4/wrapper/gradle-wrapper.jar
    echo 2. Save as: gradle\wrapper\gradle-wrapper.jar
    echo 3. Run this script again
    echo.
    echo OR use Android Studio to build:
    echo 1. Open Android Studio
    echo 2. Open project folder
    echo 3. Build -> Build APK
    echo.
    pause
    exit /b 1
)

echo Found gradle-wrapper.jar
echo.

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    echo Java not found! Please install Java JDK 17+
    pause
    exit /b 1
)

echo Building APK...
echo This may take several minutes...
echo.

REM Run gradle build
call gradlew.bat assembleDebug

if errorlevel 1 (
    echo.
    echo Build failed!
    echo.
    echo Possible solutions:
    echo 1. Use Android Studio (recommended)
    echo 2. Ensure Android SDK is installed
    echo 3. Check internet connection for dependencies
    echo.
    echo See BUILD_APK_GUIDE.md for detailed instructions
) else (
    echo.
    echo Build successful!
    echo.
    echo APK location:
    echo   app\build\outputs\apk\debug\app-debug.apk
    echo.
    echo To install on connected device:
    echo   adb install app\build\outputs\apk\debug\app-debug.apk
)

echo.
pause
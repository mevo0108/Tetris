@echo off
echo Creating directories...
if not exist bin mkdir bin
if not exist target mkdir target

echo Compiling Java files...
javac -d bin src/*.java
if errorlevel 1 (
    echo Error: Compilation failed!
    pause
    exit /b 1
)

echo Creating JAR file...
cd bin
jar cfe ..\target\tetris.jar TetrisGame .
cd ..
if errorlevel 1 (
    echo Error: JAR creation failed!
    pause
    exit /b 1
)

echo Testing the JAR file...
java -jar target/tetris.jar
if errorlevel 1 (
    echo Error: JAR execution failed!
    pause
    exit /b 1
)

echo Done! The JAR file is in the target directory.
pause 
@echo off
echo Creating release package...

REM Create release directory
if not exist release mkdir release

REM Build the game
call build.bat

REM Copy necessary files to release directory
copy run.bat release\
copy target\tetris.jar release\
copy README.md release\

echo Release package created in the 'release' directory
pause 
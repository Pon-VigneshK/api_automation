bcdedit /delete {current}
if %errorlevel% neq 0 (
    pause
    exit /b %errorlevel%
)
shutdown /r /t 5

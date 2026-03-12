# =====================================================
# Apricart Spring Boot - Local Dev Startup Script
# Usage: .\run.ps1
# =====================================================

$env:JAVA_HOME = "C:\Program Files\Java\jdk-21.0.10"
$env:PATH = "$env:JAVA_HOME\bin;" + $env:PATH

# Kill any old process using port 8081
$proc = Get-NetTCPConnection -LocalPort 8081 -State Listen -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess
if ($proc) {
    Stop-Process -Id $proc -Force
    Write-Host "Killed old process on port 8081 (PID $proc)" -ForegroundColor Yellow
    Start-Sleep -Seconds 1
}

# Load all variables from .env into the current process
Get-Content .env | Where-Object { $_ -notmatch '^\s*#' -and $_ -match '=' } | ForEach-Object {
    $key, $value = $_.Split('=', 2)
    [Environment]::SetEnvironmentVariable($key.Trim(), $value.Trim(), "Process")
}

Write-Host "Environment vars loaded from .env" -ForegroundColor Green
Write-Host "Starting Apricart Spring Boot on port 8081..." -ForegroundColor Cyan

mvn spring-boot:run

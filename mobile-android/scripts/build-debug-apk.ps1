param(
    [string]$ApiBaseUrl = "http://10.0.2.2:8080/"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$apkPath = Join-Path $projectRoot "app\build\outputs\apk\debug\app-debug.apk"

Push-Location $projectRoot
try {
    $env:HAZELGYM_API_BASE_URL = $ApiBaseUrl
    Write-Host "Building Hazel Gym APK with API base URL: $env:HAZELGYM_API_BASE_URL"
    .\gradlew.bat assembleDebug

    if (Test-Path $apkPath) {
        Write-Host "APK generated:"
        Write-Host $apkPath
    } else {
        throw "Gradle finished but APK was not found at $apkPath"
    }
} finally {
    Pop-Location
}

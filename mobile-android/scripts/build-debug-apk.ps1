param(
    [string]$ApiBaseUrl = "https://k7edn14r3k.execute-api.eu-west-1.amazonaws.com/"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
$apkPath = Join-Path $projectRoot "app\build\outputs\apk\debug\HazelGym-1.0-debug.apk"
$legacyApkPath = Join-Path $projectRoot "app\build\outputs\apk\debug\app-debug.apk"
$deliveryApkPath = Join-Path $projectRoot "app\build\outputs\apk\debug\HazelGym-demo-aws.apk"

Push-Location $projectRoot
try {
    $env:HAZELGYM_API_BASE_URL = $ApiBaseUrl
    Write-Host "Building Hazel Gym APK with API base URL: $env:HAZELGYM_API_BASE_URL"
    .\gradlew.bat assembleDebug
    if ($LASTEXITCODE -ne 0) {
        throw "Gradle build failed with exit code $LASTEXITCODE. Open Android Studio, sync Gradle, and run Build APKs if dependencies are not cached yet."
    }

    if (-not (Test-Path $apkPath) -and (Test-Path $legacyApkPath)) {
        Copy-Item -LiteralPath $legacyApkPath -Destination $apkPath -Force
    }

    if (Test-Path $apkPath) {
        Copy-Item -LiteralPath $apkPath -Destination $deliveryApkPath -Force
        Write-Host "APK generated:"
        Write-Host $apkPath
        Write-Host "Delivery copy:"
        Write-Host $deliveryApkPath
    } else {
        throw "Gradle finished but APK was not found at $apkPath"
    }
} finally {
    Pop-Location
}

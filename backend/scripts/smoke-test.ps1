param(
    [string]$BaseUrl = "http://localhost:8080",
    [string]$DemoPassword = "admin123"
)

$ErrorActionPreference = "Stop"

function Invoke-Json {
    param(
        [string]$Method,
        [string]$Path,
        [object]$Body = $null,
        [hashtable]$Headers = @{}
    )

    $params = @{
        Uri = "$BaseUrl$Path"
        Method = $Method
        Headers = $Headers
    }

    if ($null -ne $Body) {
        $params.Body = ($Body | ConvertTo-Json -Depth 10)
        $params.ContentType = "application/json"
    }

    Invoke-RestMethod @params
}

function Assert-Status {
    param(
        [scriptblock]$Call,
        [int]$ExpectedStatus,
        [string]$Label
    )

    try {
        & $Call | Out-Null
        throw "$Label should have returned HTTP $ExpectedStatus"
    } catch {
        $response = $_.Exception.Response
        if ($null -eq $response -or [int]$response.StatusCode -ne $ExpectedStatus) {
            throw
        }
        Write-Host "[OK] $Label returned HTTP $ExpectedStatus"
    }
}

Write-Host "Running HazelGym smoke test against $BaseUrl"

Invoke-Json -Method "GET" -Path "/api-docs" | Out-Null
Write-Host "[OK] OpenAPI docs are available"

$suffix = Get-Date -Format "yyyyMMddHHmmss"
$clientEmail = "smoke-$suffix@hazelgym.com"
$clientAuth = Invoke-Json -Method "POST" -Path "/api/auth/register" -Body @{
    nombre = "Smoke Test Client"
    email = $clientEmail
    password = "hazel123"
}

if ([string]::IsNullOrWhiteSpace($clientAuth.token)) {
    throw "Register did not return a JWT token"
}

$clientHeaders = @{ Authorization = "Bearer $($clientAuth.token)" }
Write-Host "[OK] Client registration returned token"

$me = Invoke-Json -Method "GET" -Path "/api/auth/me" -Headers $clientHeaders
if ($me.email -ne $clientEmail) {
    throw "Authenticated user email mismatch"
}
Write-Host "[OK] Client token authenticates /api/auth/me"

$machines = Invoke-Json -Method "GET" -Path "/api/machines" -Headers $clientHeaders
Write-Host "[OK] Client can list machines ($($machines.Count))"

$attendance = Invoke-Json -Method "POST" -Path "/api/attendances" -Headers $clientHeaders -Body @{
    usuarioId = $clientAuth.userId
    qrCodeId = 1
}
Write-Host "[OK] Client can register attendance id=$($attendance.id)"

Assert-Status -Label "Client access to admin users endpoint" -ExpectedStatus 403 -Call {
    Invoke-Json -Method "GET" -Path "/api/users" -Headers $clientHeaders
}

$adminAuth = Invoke-Json -Method "POST" -Path "/api/auth/login" -Body @{
    email = "admin@hazelgym.com"
    password = $DemoPassword
}

if ([string]::IsNullOrWhiteSpace($adminAuth.token)) {
    throw "Admin login did not return a JWT token. Run database/06_fix_seed_passwords.sql if this fails."
}

$adminHeaders = @{ Authorization = "Bearer $($adminAuth.token)" }
Write-Host "[OK] Admin login returned token"

$users = Invoke-Json -Method "GET" -Path "/api/users" -Headers $adminHeaders
Write-Host "[OK] Admin can list users ($($users.Count))"

$createdMachine = Invoke-Json -Method "POST" -Path "/api/machines" -Headers $adminHeaders -Body @{
    nombre = "Smoke Test Machine"
    descripcion = "Temporary machine created by smoke test"
    grupoMuscular = "General"
    instrucciones = "Use only for API verification"
    nivel = "Principiante"
    advertenciaSeguridad = "Temporary test record"
    imagenUrl = $null
    estado = "ACTIVA"
}
Write-Host "[OK] Admin can create machine id=$($createdMachine.id)"

Invoke-Json -Method "DELETE" -Path "/api/machines/$($createdMachine.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete test machine"

Write-Host "Smoke test completed successfully"

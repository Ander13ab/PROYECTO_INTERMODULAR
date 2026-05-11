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
    throw "Admin login did not return a JWT token. Check the seeded admin credentials in database/03_seed.sql."
}

$adminHeaders = @{ Authorization = "Bearer $($adminAuth.token)" }
Write-Host "[OK] Admin login returned token"

$users = Invoke-Json -Method "GET" -Path "/api/users" -Headers $adminHeaders
Write-Host "[OK] Admin can list users ($($users.Count))"

$classes = Invoke-Json -Method "GET" -Path "/api/classes" -Headers $clientHeaders
Write-Host "[OK] Client can list classes ($($classes.Count))"

$routines = Invoke-Json -Method "GET" -Path "/api/routines" -Headers $clientHeaders
Write-Host "[OK] Client can list routines ($($routines.Count))"

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

$createdFee = Invoke-Json -Method "POST" -Path "/api/membership-fees" -Headers $adminHeaders -Body @{
    nombre = "Smoke Test Fee"
    descripcion = "Temporary membership fee"
    precio = 19.99
}
Write-Host "[OK] Admin can create membership fee id=$($createdFee.id)"

$createdClass = Invoke-Json -Method "POST" -Path "/api/classes" -Headers $adminHeaders -Body @{
    nombre = "Smoke Test Class"
    descripcion = "Temporary class for API verification"
    duracion = 45
    entrenadorId = 3
    activa = $true
}
Write-Host "[OK] Admin can create class id=$($createdClass.id)"

$createdSession = Invoke-Json -Method "POST" -Path "/api/class-sessions" -Headers $adminHeaders -Body @{
    gymClassId = $createdClass.id
    fecha = "2026-12-31"
    horaInicio = "10:00:00"
    horaFin = "10:45:00"
}
Write-Host "[OK] Admin can create class session id=$($createdSession.id)"

$createdClassQr = Invoke-Json -Method "POST" -Path "/api/qr-codes" -Headers $adminHeaders -Body @{
    tipo = "CLASS_SESSION"
    esEntradaGimnasio = $false
    maquinaId = $null
    sesionClaseId = $createdSession.id
}
Write-Host "[OK] Admin can create class QR id=$($createdClassQr.id)"

$createdRoutine = Invoke-Json -Method "POST" -Path "/api/routines" -Headers $adminHeaders -Body @{
    nombre = "Smoke Test Routine"
    descripcion = "Temporary routine for API verification"
    entrenadorId = 3
}
Write-Host "[OK] Admin can create routine id=$($createdRoutine.id)"

$createdAssignment = Invoke-Json -Method "POST" -Path "/api/routine-assignments" -Headers $adminHeaders -Body @{
    routineId = $createdRoutine.id
    clientId = $clientAuth.userId
}
Write-Host "[OK] Admin can assign routine id=$($createdAssignment.id)"

Assert-Status -Label "Client access to routine assignments endpoint" -ExpectedStatus 403 -Call {
    Invoke-Json -Method "GET" -Path "/api/routine-assignments" -Headers $clientHeaders
}

Invoke-Json -Method "GET" -Path "/api/routine-assignments" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can list routine assignments"

Invoke-Json -Method "GET" -Path "/api/qr-codes" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can list QR codes"

Invoke-Json -Method "GET" -Path "/api/membership-fees" -Headers $clientHeaders | Out-Null
Write-Host "[OK] Client can list membership fees"

Invoke-Json -Method "DELETE" -Path "/api/routine-assignments/$($createdAssignment.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete routine assignment"

Invoke-Json -Method "DELETE" -Path "/api/qr-codes/$($createdClassQr.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete class QR"

Invoke-Json -Method "DELETE" -Path "/api/class-sessions/$($createdSession.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete class session"

Invoke-Json -Method "DELETE" -Path "/api/classes/$($createdClass.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete class"

Invoke-Json -Method "DELETE" -Path "/api/routines/$($createdRoutine.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete routine"

Invoke-Json -Method "DELETE" -Path "/api/membership-fees/$($createdFee.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete membership fee"

Invoke-Json -Method "DELETE" -Path "/api/machines/$($createdMachine.id)" -Headers $adminHeaders | Out-Null
Write-Host "[OK] Admin can delete test machine"

Write-Host "Smoke test completed successfully"

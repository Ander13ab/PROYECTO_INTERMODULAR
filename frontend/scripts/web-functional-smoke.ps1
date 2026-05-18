param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"
$stamp = Get-Date -Format "yyyyMMddHHmmss"

function Invoke-HazelGymJson {
    param(
        [Parameter(Mandatory = $true)][string]$Method,
        [Parameter(Mandatory = $true)][string]$Path,
        [string]$Token,
        [object]$Body
    )

    $headers = @{}
    if ($Token) {
        $headers.Authorization = "Bearer $Token"
    }

    $params = @{
        Method = $Method
        Uri = "$BaseUrl$Path"
        Headers = $headers
        TimeoutSec = 15
    }

    if ($null -ne $Body) {
        $params.ContentType = "application/json"
        $params.Body = ($Body | ConvertTo-Json -Depth 8)
    }

    Invoke-RestMethod @params
}

function Login {
    param([Parameter(Mandatory = $true)][string]$Email)

    Invoke-HazelGymJson -Method "POST" -Path "/api/auth/login" -Body @{
        email = $Email
        password = "admin123"
    }
}

Write-Host "Running HazelGym web functional smoke test against $BaseUrl"

$accounts = @(
    @{ Label = "ADMIN"; Email = "admin@hazelgym.com" },
    @{ Label = "TRAINER"; Email = "laura@hazelgym.com" },
    @{ Label = "CLIENT"; Email = "carlos@hazelgym.com" }
)

foreach ($account in $accounts) {
    $auth = Login -Email $account.Email
    if (-not $auth.token) {
        throw "Login did not return token for $($account.Label)"
    }

    $me = Invoke-HazelGymJson -Method "GET" -Path "/api/auth/me" -Token $auth.token
    Write-Host "[OK] Login $($account.Label): $($me.email) role=$($me.role)"
}

$adminToken = (Login -Email "admin@hazelgym.com").token
$trainerToken = (Login -Email "laura@hazelgym.com").token
$clientToken = (Login -Email "carlos@hazelgym.com").token

$adminPaths = @(
    "/api/users",
    "/api/machines",
    "/api/classes",
    "/api/class-sessions",
    "/api/qr-codes",
    "/api/attendances",
    "/api/membership-fees"
)

foreach ($path in $adminPaths) {
    $data = Invoke-HazelGymJson -Method "GET" -Path $path -Token $adminToken
    Write-Host "[OK] Admin can load $path ($($data.Count))"
}

$trainerPaths = @(
    "/api/users",
    "/api/routines",
    "/api/routine-assignments",
    "/api/classes",
    "/api/class-sessions"
)

foreach ($path in $trainerPaths) {
    $data = Invoke-HazelGymJson -Method "GET" -Path $path -Token $trainerToken
    Write-Host "[OK] Trainer can load $path ($($data.Count))"
}

$clientPaths = @(
    "/api/attendances",
    "/api/routines",
    "/api/routine-assignments",
    "/api/machines",
    "/api/classes",
    "/api/users"
)

foreach ($path in $clientPaths) {
    $data = Invoke-HazelGymJson -Method "GET" -Path $path -Token $clientToken
    Write-Host "[OK] Client can load $path ($($data.Count))"
}

$machine = Invoke-HazelGymJson -Method "POST" -Path "/api/machines" -Token $adminToken -Body @{
    nombre = "Smoke web maquina $stamp"
    descripcion = "Temporal"
    grupoMuscular = "Test"
    instrucciones = "Temporal"
    advertenciaSeguridad = "Temporal"
    nivel = "BASICO"
    estado = "ACTIVA"
    imagenUrl = $null
}
Write-Host "[OK] Admin can create machine id=$($machine.id)"

Invoke-HazelGymJson -Method "PUT" -Path "/api/machines/$($machine.id)" -Token $adminToken -Body @{
    nombre = "Smoke web maquina editada $stamp"
    descripcion = "Temporal editada"
    grupoMuscular = "Test"
    instrucciones = "Temporal"
    advertenciaSeguridad = "Temporal"
    nivel = "BASICO"
    estado = "FUERA_DE_SERVICIO"
    imagenUrl = $null
} | Out-Null
Write-Host "[OK] Admin can update machine"

Invoke-HazelGymJson -Method "DELETE" -Path "/api/machines/$($machine.id)" -Token $adminToken | Out-Null
Write-Host "[OK] Admin can delete machine"

$user = Invoke-HazelGymJson -Method "POST" -Path "/api/users" -Token $adminToken -Body @{
    nombre = "Smoke Web User $stamp"
    email = "smoke-web-$stamp@hazelgym.com"
    password = "admin123"
    roleName = "CLIENT"
    activo = $true
}
Write-Host "[OK] Admin can create user id=$($user.id)"

Invoke-HazelGymJson -Method "PUT" -Path "/api/users/$($user.id)" -Token $adminToken -Body @{
    nombre = "Smoke Web User Editado $stamp"
    email = "smoke-web-$stamp@hazelgym.com"
    password = $null
    roleName = "CLIENT"
    activo = $false
} | Out-Null
Write-Host "[OK] Admin can update user"

Invoke-HazelGymJson -Method "DELETE" -Path "/api/users/$($user.id)" -Token $adminToken | Out-Null
Write-Host "[OK] Admin can delete user"

$routine = Invoke-HazelGymJson -Method "POST" -Path "/api/routines" -Token $trainerToken -Body @{
    nombre = "Smoke web rutina $stamp"
    descripcion = "Temporal"
    entrenadorId = 3
}
Write-Host "[OK] Trainer can create routine id=$($routine.id)"

Invoke-HazelGymJson -Method "PUT" -Path "/api/routines/$($routine.id)" -Token $trainerToken -Body @{
    nombre = "Smoke web rutina editada $stamp"
    descripcion = "Temporal editada"
    entrenadorId = 3
} | Out-Null
Write-Host "[OK] Trainer can update routine"

$assignment = Invoke-HazelGymJson -Method "POST" -Path "/api/routine-assignments" -Token $trainerToken -Body @{
    routineId = $routine.id
    clientId = 2
}
Write-Host "[OK] Trainer can create assignment id=$($assignment.id)"

Invoke-HazelGymJson -Method "DELETE" -Path "/api/routine-assignments/$($assignment.id)" -Token $trainerToken | Out-Null
Write-Host "[OK] Trainer can delete assignment"

Invoke-HazelGymJson -Method "DELETE" -Path "/api/routines/$($routine.id)" -Token $trainerToken | Out-Null
Write-Host "[OK] Trainer can delete routine"

$classes = Invoke-HazelGymJson -Method "GET" -Path "/api/classes" -Token $trainerToken
$trainerClass = @($classes | Where-Object { $_.entrenadorId -eq 3 })[0]
if ($null -eq $trainerClass) {
    throw "No class found for trainer id=3"
}

$session = Invoke-HazelGymJson -Method "POST" -Path "/api/class-sessions" -Token $trainerToken -Body @{
    gymClassId = $trainerClass.id
    fecha = "2026-05-30"
    horaInicio = "10:00"
    horaFin = "11:00"
}
Write-Host "[OK] Trainer can create class session id=$($session.id)"

Invoke-HazelGymJson -Method "PUT" -Path "/api/class-sessions/$($session.id)" -Token $trainerToken -Body @{
    gymClassId = $trainerClass.id
    fecha = "2026-05-30"
    horaInicio = "10:30"
    horaFin = "11:30"
} | Out-Null
Write-Host "[OK] Trainer can update class session"

Invoke-HazelGymJson -Method "DELETE" -Path "/api/class-sessions/$($session.id)" -Token $trainerToken | Out-Null
Write-Host "[OK] Trainer can delete class session"

Write-Host "Web functional smoke test completed successfully"

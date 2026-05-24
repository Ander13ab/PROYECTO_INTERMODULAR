param(
    [string]$Endpoint = "hazelgym-db.cpsq2kmkisyt.eu-west-1.rds.amazonaws.com",
    [string]$Username = "admin_hazelgym",
    [string]$Database = "hazelgym",
    [int]$Port = 3306,
    [string]$MysqlPath = ""
)

$ErrorActionPreference = "Stop"

if (-not $MysqlPath) {
    $candidates = @(
        "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe",
        "C:\Program Files\MySQL\MySQL Workbench 8.0\mysql.exe"
    )

    $MysqlPath = $candidates | Where-Object { Test-Path -LiteralPath $_ } | Select-Object -First 1
}

if (-not $MysqlPath) {
    throw "No se ha encontrado mysql.exe. Indica la ruta con -MysqlPath."
}

Write-Host "Probando conexion con RDS: ${Endpoint}:$Port / base $Database"
Write-Host "Usuario: $Username"

$securePassword = Read-Host "Introduce la password de RDS" -AsSecureString
$passwordPtr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)

try {
    $plainPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($passwordPtr)
    $env:MYSQL_PWD = $plainPassword

    & $MysqlPath `
        --host=$Endpoint `
        --port=$Port `
        --user=$Username `
        --default-character-set=utf8mb4 `
        --database=$Database `
        --execute="SELECT VERSION() AS mysql_version, DATABASE() AS selected_database;"

    if ($LASTEXITCODE -ne 0) {
        throw "La conexion con RDS ha fallado. Revisa security group, public access, usuario y password."
    }

    $sqlFiles = @(
        "02_schema.sql",
        "03_seed.sql",
        "05_demo_machine_media.sql",
        "04_verify.sql"
    )

    foreach ($file in $sqlFiles) {
        $path = Join-Path $PSScriptRoot $file
        Write-Host "Ejecutando $file..."
        Get-Content -LiteralPath $path -Raw | & $MysqlPath `
            --host=$Endpoint `
            --port=$Port `
            --user=$Username `
            --default-character-set=utf8mb4 `
            --database=$Database

        if ($LASTEXITCODE -ne 0) {
            throw "Fallo ejecutando $file."
        }
    }

    Write-Host "RDS cargado y verificado correctamente."
}
finally {
    if ($passwordPtr -ne [IntPtr]::Zero) {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($passwordPtr)
    }

    Remove-Item Env:MYSQL_PWD -ErrorAction SilentlyContinue
}

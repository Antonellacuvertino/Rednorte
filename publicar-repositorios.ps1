param(
    [string]$GitHubUser = "Antonellacuvertino",
    [switch]$Private
)

$ErrorActionPreference = "Stop"

$repositories = [ordered]@{
    "frontend" = "rednorte-frontend"
    "bff-rednorte" = "rednorte-bff"
    "ms-pacientes" = "rednorte-ms-pacientes"
    "ms-citas" = "rednorte-ms-citas"
    "ms-lista-espera" = "rednorte-ms-lista-espera"
    "ms-reasignacion" = "rednorte-ms-reasignacion"
    "ms-auditoria" = "rednorte-ms-auditoria"
    "ms-notificaciones" = "rednorte-ms-notificaciones"
}

$ghCommand = Get-Command gh -ErrorAction SilentlyContinue
$ghPath = if ($ghCommand) {
    $ghCommand.Source
} elseif (Test-Path "C:\Program Files\GitHub CLI\gh.exe") {
    "C:\Program Files\GitHub CLI\gh.exe"
} else {
    throw "GitHub CLI no esta instalado. Instale con: winget install --id GitHub.cli"
}

& $ghPath auth status
if ($LASTEXITCODE -ne 0) {
    throw "GitHub CLI no esta autenticado. Ejecute: gh auth login"
}

foreach ($entry in $repositories.GetEnumerator()) {
    $branch = "export/$($entry.Value)"
    $url = "https://github.com/$GitHubUser/$($entry.Value).git"
    $visibility = if ($Private) { "--private" } else { "--public" }

    & $ghPath repo view "$GitHubUser/$($entry.Value)" --json name | Out-Null
    if ($LASTEXITCODE -ne 0) {
        & $ghPath repo create "$GitHubUser/$($entry.Value)" $visibility --description "Componente RedNorte Evaluacion 3"
        if ($LASTEXITCODE -ne 0) {
            throw "No fue posible crear $($entry.Value)."
        }
    }

    git show-ref --verify --quiet "refs/heads/$branch"
    if ($LASTEXITCODE -eq 0) {
        throw "La rama $branch ya existe. Eliminela manualmente despues de verificar su contenido."
    }

    git subtree split --prefix $entry.Key -b $branch
    if ($LASTEXITCODE -ne 0) {
        throw "No fue posible separar $($entry.Key)."
    }

    git push $url "$branch`:main"
    if ($LASTEXITCODE -ne 0) {
        throw "No fue posible publicar $url. Compruebe que el repositorio existe y que tiene acceso."
    }
}

Write-Host "Componentes publicados. Configure Rulesets en cada repositorio." -ForegroundColor Green

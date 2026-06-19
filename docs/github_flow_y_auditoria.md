# GitHub Flow y auditoria de repositorios

## Flujo obligatorio

La rama estable es `main`. Cada requerimiento se implementa en una rama
`feature/*`; las correcciones usan `fix/*`. La integracion se realiza mediante
pull request con CI aprobada, revision de codigo y squash merge.

La implementacion de calidad y seguridad de Evaluacion 3 se desarrollo en:

```text
feature/evaluacion3-calidad-seguridad
```

## Controles de nivel auditoria

- CI separada para BFF, seis microservicios y frontend.
- JaCoCo y Vitest bloquean cobertura inferior a 90%.
- CodeQL analiza Java y JavaScript en PR, `main` y semanalmente.
- Dependency Review rechaza dependencias nuevas de severidad alta.
- Dependabot revisa NPM y los siete proyectos Maven cada semana.
- `CODEOWNERS`, plantilla de PR e issues estructurados.
- `SECURITY.md` y `CONTRIBUTING.md`.
- secretos fuera del repositorio mediante variables de entorno.

## Configuracion remota requerida

En GitHub, configurar una regla para `main` con:

1. Require a pull request before merging.
2. Require at least one approval.
3. Dismiss stale approvals.
4. Require status checks: todos los jobs de CI, CodeQL y Dependency Review.
5. Require conversation resolution.
6. Block force pushes and deletions.
7. Include administrators.

Esta configuracion no se versiona en Git; debe comprobarse en
`Settings > Rules > Rulesets` de cada repositorio antes de la auditoria.

## Separacion de repositorios

Cada componente incluye su propio `.github/workflows/ci.yml` y Dependabot. El
script requiere GitHub CLI autenticado, crea los repositorios faltantes y
publica cada componente conservando historial:

```powershell
winget install --id GitHub.cli
gh auth login
.\publicar-repositorios.ps1
```

El script crea ramas temporales `export/*` mediante `git subtree split` y las
publica como `main` en cada repositorio.

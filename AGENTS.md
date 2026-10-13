# Instructions for AI coding assistants

<!--
Many AI coding tools read this file automatically (some use CLAUDE.md or
.github/copilot-instructions.md: you can symlink or copy it). Keep it short and up to date.
-->

## Project
<!-- One paragraph: what the system does. -->

## Stack and versions
<!-- Exact versions matter: assistants often mix APIs from different versions. -->
- Backend:
- Web:
- Database / ORM:
- Mobile:
- Test framework:

## Structure
- `src/` — backend and web
- `mobile/` — mobile app
- `docs/` — documentation; `docs/api/openapi.yaml` is the API contract
- `api-tests/` — HTTP request files

## Commands
- Run: 
- Test: 
- Lint/format: 

## Rules
- Keep `docs/api/openapi.yaml` in sync with every API change.
- Business logic goes in the service layer, not in controllers/endpoints.
- Use DTOs for API input/output, never database entities.
- Every endpoint enforces authorization on the server (role **and** ownership).
- Never hard-code secrets; read configuration from the environment.
- Add or update tests for every behaviour change; never weaken a test to make it pass.
- Use schema changes only through migrations.

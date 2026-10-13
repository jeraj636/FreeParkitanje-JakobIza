# Architecture

## Components

```mermaid
flowchart LR
    subgraph Clients
        Web[Web app]
        Mobile[Mobile app]
        AgentUI[Agent chat / MCP client]
    end
    subgraph Cloud
        API[REST API]
        Agent[Agent]
        DB[(Managed database)]
    end
    LLM[LLM provider]

    Web -->|HTTPS| API
    Mobile -->|HTTPS + bearer token| API
    AgentUI --> Agent
    Agent -->|tools, user's token| API
    Agent --> LLM
    API --> DB
    API --> LLM
```

Update this diagram as the system evolves.

## Layers (backend)
<!-- Presentation → business logic → data access. Name the main projects/modules. -->

## Authentication & authorization
<!-- How users log in, which token/cookie, which roles, where checks happen. -->

## Deployment
<!-- Cloud services used, how CI/CD deploys, where secrets live, where logs are. -->

```mermaid
flowchart LR
    Dev[Push to main] --> CI[GitHub Actions: build + test]
    CI --> Reg[Container registry]
    Reg --> Run[Cloud runtime]
    Run --> DB[(Managed DB)]
```

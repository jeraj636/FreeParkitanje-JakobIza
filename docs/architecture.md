# Architecture

## Components


flowchart LR
    subgraph Clients["Odjemalci"]
        Web["Web app<br/>TypeScript + DaisyUI"]
        Mobile["Mobilna aplikacija"]
    end

    subgraph Backend["Strežnik"]
        API["API<br/>PHP"]
        DB[("MariaDB")]
        AI["AI<br/>Claude"]
    end

    Web -->|HTTPS| API
    Mobile -->|HTTPS| API
    API --> DB
    API --> AI


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

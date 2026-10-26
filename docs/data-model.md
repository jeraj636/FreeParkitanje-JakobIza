# Data Model

Replace the example with your own model. Requirements: at least 3 entities, one 1:N and one M:N relationship.

```mermaid
erDiagram
    USER ||--o{ USER_AVTO : owns
    AVTO ||--o{ USER_AVTO : assigned_to
    USER ||--o{ REZERVACIJA : makes
    AVTO ||--o{ REZERVACIJA : uses
    PARKING ||--o{ REZERVACIJA : reserved_for

    USER {
        int id PK
        string email UK
        string role
    }

    USER_AVTO {
        int userID PK, FK
        int avtoID PK, FK
    }

    AVTO {
        int id PK
        string registrska UK
    }

    REZERVACIJA {
        int id PK
        datetime zac
        datetime kon
        int parkingID FK
        int uporabnikID FK
        int avtoID FK
    }

    PARKING {
        int id PK
        int x
        int y
    }
```

## Entities

| Entity | Description | Important rules / constraints |
|--------|-------------|-------------------------------|
| | | |

## Business rules
<!-- e.g. "A room cannot have two bookings that overlap in time." -->

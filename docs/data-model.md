# Data Model

Replace the example with your own model. Requirements: at least 3 entities, one 1:N and one M:N relationship.

```mermaid
erDiagram
    USER ||--o{ ORDER : places
    ORDER ||--|{ ORDER_ITEM : contains
    PRODUCT ||--o{ ORDER_ITEM : "appears in"

    USER {
        int id PK
        string email UK
        string role
    }
    ORDER {
        int id PK
        int userId FK
        datetime createdAt
    }
    ORDER_ITEM {
        int orderId PK, FK
        int productId PK, FK
        int quantity
    }
    PRODUCT {
        int id PK
        string name
        decimal price
    }
```

## Entities

| Entity | Description | Important rules / constraints |
|--------|-------------|-------------------------------|
| | | |

## Business rules
<!-- e.g. "A room cannot have two bookings that overlap in time." -->

# API tests

Put `.http` files (VS Code REST Client / JetBrains HTTP Client), a Postman or Bruno collection
here. Cover every endpoint, including error cases (`400`, `401`, `403`, `404`, `409`).

```http
@baseUrl = http://localhost:8080
@token = <paste a token for local testing — never commit a real one>

GET {{baseUrl}}/api/items
Authorization: Bearer {{token}}
```

# Test layout

Java tests follow Maven conventions under `src/test/java/`.

| Standard path | Java location | Purpose |
|---|---|---|
| `tests/unit/` | `src/test/java/com/apricart/consumer/unit/` | Isolated business logic, validators, mappers |
| `tests/integration/` | `src/test/java/com/apricart/consumer/integration/` | API, database, security, external dependencies |

Run tests:
```bash
make test-unit
make test-integration
make verify
```

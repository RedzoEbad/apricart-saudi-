# Testing Skill (Apricart Backend)
Identify business logic, validation, APIs, persistence, external integrations and failure modes.
Write unit tests for isolated logic and integration tests for component boundaries.
Always consider happy path, invalid input, missing data, boundaries, authorization (401/403), tenant/resource isolation, dependency failure, retries and duplicates.
Run targeted tests first, then the full suite:
```bash
mvn -B test -Dtest="**/unit/**"
mvn -B test -Dtest="**/integration/**"
mvn -B verify
```

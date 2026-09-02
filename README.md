# Apricart Saudi — Backend API (Spring Boot)

Saudi Arabia consumer backend API for Apricart.

**Engineering standard:** This project follows [Engineering Development & Security Standard v1.0](STANDARD.md).

## Core rules
- No Test = Not Done
- No Security Review = Not Done
- CI Failure = Cannot Merge

## Stack
- Java 8, Spring Boot 2.3.3
- Spring Security + JWT
- PostgreSQL, Redis
- MapStruct, Lombok
- Swagger

## Project structure

```text
.cursor/          # Cursor rules and skills for AI-assisted development
.github/          # CI workflows and PR template
docs/             # Jira workflow, RBAC, database access, developer checklist
plans/            # Feature plans (plans/<JIRA-ID>-<feature>.md)
tests/            # Test layout documentation (see README inside)
src/
  main/java/com/apricart/consumer/
    controller/   # REST API endpoints
    service/      # Business logic
    repository/   # JPA and Elasticsearch repositories
    security/     # JWT, authentication, authorization
    configuration/
  test/java/com/apricart/consumer/
    unit/         # Unit tests
    integration/  # Integration tests
```

## Getting started

```bash
# Configure environment (never commit secrets)
cp .env.example .env   # if available

./run.sh
# or
mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Quality checks

```bash
make check            # mvn verify
make test             # all tests
make test-unit        # unit tests only
make test-integration # integration tests only
```

## Documentation
- [STANDARD.md](STANDARD.md) — Engineering & security standard
- [docs/DEVELOPER_CHECKLIST.md](docs/DEVELOPER_CHECKLIST.md)
- [docs/JIRA_WORKFLOW.md](docs/JIRA_WORKFLOW.md)
- [docs/RBAC_MATRIX.md](docs/RBAC_MATRIX.md)
- [docs/DATABASE_ACCESS_MATRIX.md](docs/DATABASE_ACCESS_MATRIX.md)
- [plans/PLAN_TEMPLATE.md](plans/PLAN_TEMPLATE.md)

## Security
- JWT-based authentication
- `@PreAuthorize` for admin endpoints
- Customer resource ownership validation required
- Secrets via environment variables (`.env`), never in source control

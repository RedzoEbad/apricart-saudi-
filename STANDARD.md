# Engineering Development & Security Standard v1.0

**Status:** Mandatory engineering standard
**Applies to:** Developers, QA, DevOps, technical leads, reviewers and contractors
**Primary stack:** Python + Poetry + Ruff + Black + Pytest
**AI tools:** Cursor and Claude

## 1. Purpose

Establish one repeatable engineering process that produces maintainable, tested, secure and reviewable software.

## 2. Non-negotiable principles

1. Plan before non-trivial implementation.
2. Follow existing architecture unless an approved design change is documented.
3. Every feature must have appropriate automated tests.
4. Backend authorization is mandatory; frontend controls are not security controls.
5. Least privilege applies to users, services, databases and infrastructure.
6. Secrets must never be committed to source control.
7. Security testing is part of development, not a final phase.
8. CI quality gates must pass before merge.
9. Human reviewers remain accountable for AI-generated code.
10. Production access is restricted, attributable and auditable.

## 3. Jira Definition of Ready

A ticket must contain:
- objective and business context
- acceptance criteria
- dependencies
- API/UI requirements where applicable
- data/database impact
- security/privacy considerations
- expected error and edge-case behaviour

## 4. Standard feature lifecycle

Jira -> Plan -> Design/AI review -> Implement -> Unit tests -> Integration tests -> Security checks -> Lint/format/type checks -> PR -> CI -> Human review -> Merge -> QA/UAT -> Done.

## 5. Planning

Non-trivial work must create `plans/<JIRA-ID>-<feature>.md`.
The plan records architecture, APIs, data changes, security, tests, risks and implementation steps.

## 6. Coding standards

Use:
- Poetry for dependency/environment management
- Ruff for linting
- Black for formatting
- type hints for application code
- Pytest for automated tests

Required local checks:
```bash
poetry run ruff check .
poetry run black --check .
poetry run pytest
```

## 7. Testing standard

At minimum, test applicable:
- happy paths
- validation failures
- boundary conditions
- exceptions
- authorization
- tenant/resource isolation
- database behaviour
- external dependency failures
- duplicate/retry/idempotency behaviour

Unit tests isolate business logic. Integration tests verify component boundaries such as API/database/authentication. E2E tests are used for critical end-to-end journeys.

## 8. Security vulnerability prevention

Developers must consider at least:

### Authentication
- weak authentication flows
- broken session handling
- token leakage
- missing token expiry/revocation
- account enumeration
- credential stuffing/brute force
- MFA bypass where MFA is required

### Authorization
- IDOR/BOLA
- missing function-level authorization
- privilege escalation
- horizontal access violations
- vertical privilege escalation
- cross-tenant data access
- insecure direct object references
- mass assignment

### Input and injection
- SQL/NoSQL injection
- command injection
- OS injection
- LDAP injection
- template injection
- XSS
- SSRF
- path traversal
- unsafe deserialization
- malicious file upload

### API security
- missing rate limits
- excessive data exposure
- weak schema validation
- unrestricted pagination/filtering
- unsafe HTTP methods
- CORS misconfiguration
- replay/duplicate requests
- missing idempotency for sensitive operations

### Secrets and cryptography
- hardcoded secrets
- secrets in logs
- weak algorithms
- insecure key storage
- improper certificate/TLS validation
- predictable tokens
- sensitive data without encryption at rest/in transit

### Database/data
- excessive DB privileges
- shared admin credentials
- missing tenant filters
- sensitive data exposure
- unsafe migrations
- backup exposure
- overly broad queries
- audit-log tampering

### Files and documents
- unrestricted upload types
- executable uploads
- path traversal
- archive bombs
- malware scanning gaps
- unsafe parsing
- public object-storage exposure

### Dependencies/supply chain
- vulnerable dependencies
- unpinned or uncontrolled versions
- malicious packages
- compromised CI actions
- dependency confusion
- secrets in build logs

### Cloud/infrastructure
- public storage
- overly permissive IAM
- exposed management ports
- insecure security groups
- container running as root
- leaked environment variables
- missing network segmentation
- missing audit logging

### Business logic
- bypassing approval workflows
- replaying transactions
- manipulating amounts/limits
- race conditions
- TOCTOU
- duplicate processing
- workflow/state-machine bypass

### Logging/monitoring
- insufficient audit trails
- sensitive data in logs
- missing security alerts
- missing correlation/request IDs
- inability to reconstruct privileged actions

## 9. Security requirements for protected APIs

Every protected endpoint must define:
- authentication requirement
- permission required
- tenant/resource ownership rules
- validation rules
- rate limiting where appropriate
- audit requirement where sensitive

Tests must include:
- unauthenticated -> 401
- authenticated but unauthorized -> 403
- authorized -> expected success
- cross-tenant/resource access -> denied
- privilege escalation attempt -> denied

## 10. Database access

Separate identities by environment and purpose. Application runtime accounts must not have schema-administration privileges. Production developer access is restricted and should be read-only or time-bound when operationally necessary.

Credentials must come from approved secret management mechanisms.

## 11. RBAC

Use permission-based RBAC:
`resource:action`, e.g. `case:read`, `case:approve`.

Roles aggregate permissions. Authorization is enforced server-side. Sensitive permission changes must be auditable.

## 12. Code review

Reviewers must assess:
- requirements/acceptance criteria
- architecture
- correctness
- tests
- security
- authorization
- database impact
- performance
- observability
- maintainability
- migration/rollback risk

## 13. CI/CD quality gates

A PR cannot merge when required checks fail. Minimum checks:
- dependency install
- Ruff
- Black check
- tests
- coverage threshold
- type checking if configured
- dependency/security scan where configured

## 14. Vulnerability severity and response

Use a consistent severity model:

**Critical:** active/exploitable compromise, authentication/authorization bypass, remote code execution, mass sensitive-data exposure. Immediate containment and remediation.

**High:** serious exploit with meaningful confidentiality/integrity/availability impact. Prioritize for immediate sprint remediation.

**Medium:** limited impact or harder exploitation. Remediate in planned sprint.

**Low:** defense-in-depth or low-impact issue. Track and remediate according to risk.

Do not rely only on scanner severity. Consider exploitability, exposure, affected assets, data sensitivity, business impact and compensating controls.

## 15. Definition of Done

### Requirements
- [ ] Acceptance criteria satisfied
- [ ] Documentation updated if needed

### Engineering
- [ ] Architecture followed
- [ ] Code reviewed
- [ ] No debug/dead code
- [ ] Errors handled
- [ ] Logging/metrics appropriate

### Testing
- [ ] Unit tests
- [ ] Integration tests where applicable
- [ ] Edge cases
- [ ] Authorization tests
- [ ] Full test suite passes

### Security
- [ ] Authentication checked
- [ ] Authorization/RBAC checked
- [ ] Tenant/resource isolation checked
- [ ] Input/injection risks checked
- [ ] Secrets checked
- [ ] File handling checked where applicable
- [ ] Dependency/security risks checked
- [ ] Audit logging checked where applicable

### Quality
- [ ] Ruff passes
- [ ] Black passes
- [ ] Type checking passes where configured
- [ ] CI passes

## 16. AI usage

Cursor/Claude may accelerate planning, implementation, testing and review. Developers must understand and verify generated code.

Never provide credentials, production secrets, private keys, customer PII or regulated data to AI tools unless explicitly approved under the organization's data-handling policy.

AI-generated tests do not replace human review.

## 17. Exceptions

Exceptions require:
- documented reason
- risk assessment
- compensating control
- owner
- expiry/review date
- technical lead/security approval where applicable

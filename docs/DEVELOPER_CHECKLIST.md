# Developer Checklist — Apricart Backend (Spring Boot)

## Before coding
- [ ] Jira ticket meets Definition of Ready
- [ ] Relevant code/tests/docs inspected
- [ ] Plan created for non-trivial work (`plans/<JIRA-ID>-<feature>.md`)
- [ ] Security, RBAC and database impact identified

## During coding
- [ ] Existing architecture/patterns followed
- [ ] Input validated at API boundaries
- [ ] Authorization enforced server-side (`@PreAuthorize`, JWT)
- [ ] Customer/resource ownership enforced
- [ ] No secrets hardcoded (use `.env` / secret management)
- [ ] Errors handled safely
- [ ] Audit logging added where required

## Testing
- [ ] Unit tests (`src/test/java/.../unit/`)
- [ ] Integration tests (`src/test/java/.../integration/`) where applicable
- [ ] Happy path
- [ ] Negative paths
- [ ] Edge cases
- [ ] 401/403 authorization cases
- [ ] Cross-customer/resource access denied
- [ ] Retry/idempotency cases where applicable

## Before PR
- [ ] `mvn -B verify`
- [ ] AI-assisted review completed for non-trivial changes
- [ ] PR template completed

## Before Done
- [ ] CI passed
- [ ] Human code review completed
- [ ] Jira updated
- [ ] Documentation updated if required

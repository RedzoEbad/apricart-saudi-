# Integration tests

See `src/test/java/com/apricart/consumer/integration/` for Spring Boot integration tests.

Protected API tests must cover:
- Unauthenticated → 401
- Authenticated but unauthorized → 403
- Authorized → expected success
- Cross-customer/resource access → denied

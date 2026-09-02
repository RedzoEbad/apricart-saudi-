# RBAC Matrix — Apricart Backend

Permission-based RBAC enforced server-side via Spring Security and `@PreAuthorize`.

## Roles

| Role | Description |
|---|---|
| USER | Registered customer (mobile app) |
| ADMIN | Portal/admin user with product and order management |
| SUPER_ADMIN | Full system access including role/permission management |

## Permissions

| Permission | USER | ADMIN | SUPER_ADMIN |
|---|---:|---:|---:|
| product:read | ✓ | ✓ | ✓ |
| product:search | ✓ | ✓ | ✓ |
| cart:manage | ✓ | - | - |
| order:create | ✓ | - | - |
| order:read (own) | ✓ | - | - |
| order:read (all) | - | ✓ | ✓ |
| order:update | - | ✓ | ✓ |
| order:cancel | ✓ (own) | ✓ | ✓ |
| customer:profile:read | ✓ | - | - |
| customer:profile:update | ✓ | - | - |
| customer:address:manage | ✓ | - | - |
| wishlist:manage | ✓ | - | - |
| product:create | - | ✓ | ✓ |
| product:update | - | ✓ | ✓ |
| product:delete | - | ✓ | ✓ |
| category:manage | - | ✓ | ✓ |
| coupon:manage | - | ✓ | ✓ |
| warehouse:manage | - | ✓ | ✓ |
| user:manage | - | - | ✓ |
| role:manage | - | - | ✓ |
| permission:manage | - | - | ✓ |
| audit:read | - | ✓ | ✓ |

## Implementation notes
- JWT tokens carry role claims; `@PreAuthorize("hasAuthority('ADMIN')")` protects admin endpoints.
- Customer endpoints must validate resource ownership (customer ID matches authenticated user).
- Deny by default; unauthenticated requests return 401.
- Unauthorized requests return 403.

## Rules
- Permissions are enforced server-side.
- Frontend visibility is not authorization.
- Deny by default.
- Least privilege.
- Separate approval from preparation where feasible.
- Audit sensitive permission and role changes.

# Database Access Matrix

| Identity | Environment | Data Access | Schema/Admin | Notes |
|---|---|---|---|---|
| app_runtime | DEV/UAT/PROD | Required application CRUD only | No | Runtime identity |
| app_readonly | UAT/PROD | Read-only selected data | No | Reporting/diagnostics |
| migration_user | DEV/UAT/PROD | Required migration scope | Yes, controlled | Use only for migrations |
| backup_user | PROD | Backup-specific | No | No application access |
| analytics_user | PROD | Approved read-only datasets | No | Prefer masked/minimized data |
| developer_user | DEV | Development DB | Limited | No production access by default |
| DBA/admin | PROD | Break-glass/admin | Yes | Time-bound, logged, approved |

## Rules
- Separate credentials per environment.
- No shared personal credentials.
- Runtime identities must not be schema administrators.
- Store credentials in approved secret management.
- Production access is attributable and audited.
- Prefer temporary/break-glass access for exceptional production operations.
- Review access regularly.

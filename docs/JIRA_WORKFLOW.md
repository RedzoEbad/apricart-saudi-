# Jira Workflow

## Statuses
BACKLOG -> READY FOR DEVELOPMENT -> IN DEVELOPMENT -> CODE REVIEW -> QA/UAT -> DONE

## Rules
- Tickets entering development must meet Definition of Ready.
- Developer creates plan for non-trivial work.
- PR must reference Jira ticket.
- PR cannot merge while CI is failing.
- Security defects are linked to the originating feature/incident.
- DONE requires Definition of Done.

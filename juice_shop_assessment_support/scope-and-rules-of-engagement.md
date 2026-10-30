# Scope and Rules of Engagement

> Complete and agree this document before testing. This template does not itself grant permission.

## Engagement details

- **Project:** Web Application Security Assessment of OWASP Juice Shop
- **Assessor:** [Name]
- **Authorising person / role:** [Name and role]
- **Assessment start and end:** [Dates and time zone]
- **Environment owner:** [Name / team]
- **Target:** [Local URL, e.g. http://localhost:3000]
- **Application version / image tag:** [Record exact version or image digest]
- **Source / environment notes:** [Local Docker, operating system, Java version]

## In-scope assets

- [ ] Local Juice Shop web interface
- [ ] Local Juice Shop API endpoints
- [ ] Local test accounts and seeded data
- [ ] Local application logs, if available and authorised
- [ ] Assessment tools and generated reports

## Out of scope

- Public Juice Shop instances or other people's systems
- Any network, host, account, or service not explicitly listed above
- Denial-of-service, destructive testing, persistence, or data exfiltration beyond approved lab data
- Real personal data, payment details, or production credentials

## Permitted activities

- Inspecting responses, cookies, and security headers
- Controlled, non-destructive tests in the local lab
- Reviewing available logs and synthetic parser test data
- Recording reproducible evidence
- Applying and retesting approved fixes in the lab

## Safety controls

1. Confirm the target URL and local ownership before each test.
2. Use only dummy accounts and lab data.
3. Stop if the test affects a system outside scope or causes unexpected instability.
4. Keep credentials, tokens, session cookies, and personal data out of committed evidence.
5. Store evidence locally with restricted access.
6. Record any change to scope and obtain approval before proceeding.

## Communication and stop conditions

- **Primary contact:** [Name / contact]
- **Escalation contact:** [Name / contact]
- **Stop immediately if:** [Unexpected external traffic, instability, data exposure, or other trigger]
- **Incident handling procedure:** [Link or description]

## Approval

- **Authorising person:** [Name]
- **Approval date:** [Date]
- **Scope version:** [Version]

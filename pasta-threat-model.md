# PASTA Threat Model — OWASP Juice Shop Lab

This is an initial hypothesis to validate against the actual lab configuration, not a statement that every listed weakness exists.

## 1. Define business and security objectives

- Protect lab user and account data.
- Preserve integrity of baskets, orders, and user actions.
- Maintain availability of the local application.
- Demonstrate a repeatable and authorised security assessment.

## 2. Define technical scope

Confirm the actual architecture before finalising this section.

- Browser-based single-page application
- HTTP API endpoints
- Application server
- Database and file storage used by the selected Juice Shop version
- Test accounts and lab data
- Local container, host, and any logging components

**Out of scope:** public instances, unrelated local services, and production data.

## 3. Decompose the application

Record verified components and trust boundaries. Suggested starting flow:

```text
Tester / browser
      |
      | HTTP requests and responses
      v
Juice Shop web application / API
      |
      | application queries and operations
      v
Database / application storage
```

Add the actual authentication/session flow, external services, and logging path only after verifying them. Mark assumptions clearly.

## 4. Analyse threats

| Threat | Potential security property affected | Example control to examine |
|---|---|---|
| Injection | Integrity, confidentiality | Parameterised queries, input handling, output encoding |
| Cross-site scripting (XSS) | Confidentiality, integrity | Context-aware output encoding, safe DOM APIs, CSP as defence in depth |
| Broken access control | Confidentiality, integrity | Server-side authorisation checks for each object and action |
| Authentication abuse | Confidentiality, availability | Rate limiting, secure session management, monitoring |
| Security misconfiguration | Multiple | Secure defaults, minimal banners, suitable headers, controlled error handling |
| Sensitive data exposure | Confidentiality | Data minimisation, TLS where applicable, safe storage and logging |
| Vulnerable components | Multiple | Inventory, version review, patch management |
| Insufficient logging | Detection and response | Useful security events, time synchronisation, retention, alert review |

These are threat hypotheses. Test only within the approved scope and record evidence for any finding.

## 5. Analyse vulnerabilities

For each hypothesis, record:

- Asset and endpoint
- Preconditions and required privileges
- Test steps and safe test input
- Expected and observed behaviour
- Evidence reference
- Whether the issue was reproduced
- Possible false-positive explanations

Do not label a threat as a confirmed vulnerability until evidence supports it.

## 6. Model attack paths

Example high-level attack tree:

```text
Goal: access data belonging to another lab user
  ├─ Find an object-level authorisation weakness
  │    └─ Test whether the server checks ownership on each request
  ├─ Obtain another test user's session
  │    └─ Review session handling and account protections
  └─ Exploit an injection or client-side issue
       └─ Validate safely with non-sensitive lab data
```

The branches are scenarios, not instructions to attack a real system. Use dummy accounts and stop if testing exceeds scope.

## 7. Rate risk and choose treatment

Use the project-defined likelihood × impact scale in `README.md`. Document assumptions and risk owner. Consider existing controls and the business context. Do not treat the score as CVSS.

## 8. Validate mitigations

For each verified finding:

1. Record the original behaviour and evidence.
2. Implement or document a fix.
3. Repeat the same test under comparable conditions.
4. Record pass/fail, date, evidence, and any residual risk.
5. Obtain risk-owner acceptance for any issue that remains open.

## Assumptions and open questions

- [ ] Confirm actual app version and runtime architecture.
- [ ] Confirm where, if anywhere, access logs are written and their format.
- [ ] Confirm whether the app is HTTP-only locally or configured for HTTPS.
- [ ] Confirm test accounts and roles.
- [ ] Confirm retention and handling rules for evidence.

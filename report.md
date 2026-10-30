# Web Application Security Assessment of OWASP Juice Shop

> **Draft template.** Replace bracketed fields and sample text with evidence from the authorised lab. Do not submit sample rows or synthetic log output as real findings.

## 1. Assessment details

- **Assessor:** [Name]
- **Assessment date and time zone:** [Date]
- **Target:** [Local URL]
- **Application version / image:** [Version or digest]
- **Environment:** [OS, Docker, Java version]
- **Authorisation and scope reference:** [Document / approval]
- **Assessment period:** [Start and end]

## 2. Executive summary

[Summarise scope, methods, verified results, highest risks, remediation priorities, and important limitations. Do not claim the application is secure simply because the tools found no indicators.]

## 3. Scope and rules of engagement

[Summarise included assets, exclusions, permitted activities, test accounts, safety controls, and approval. Refer to `scope-and-rules-of-engagement.md`.]

## 4. Methodology

Describe the actual work completed:

1. Scope and environment confirmation
2. PASTA threat modelling
3. Reconnaissance and header/cookie review
4. Controlled OWASP Top 10 testing
5. Log review, if a suitable source was available
6. Likelihood × impact risk rating
7. Remediation and retesting
8. Reporting and framework mapping

List steps not performed and explain why.

## 5. Tools and execution

| Tool | Version / environment | Purpose | Result / limitation |
|---|---|---|---|
| `HeadersCheck.java` | [Java version] | Selected HTTP headers and cookie attributes | [Result] |
| `LogTriage.java` | [Java version] | Selected indicators in compatible logs | [Result / log source verified?] |
| `FindingsExport.java` | [Java version] | CSV validation, risk score, JSON export | [Result] |

## 6. Results summary

| Rating | Verified findings |
|---|---:|
| Critical | [Count] |
| High | [Count] |
| Medium | [Count] |
| Low | [Count] |
| Informational | [Count] |

Only include verified findings in these counts. Put hypotheses, sample data, and unverified alerts in a separate limitations or observations section.

## 7. Detailed findings

[Copy the content from each `findings/finding-template.md` record, or link to the corresponding finding. Include evidence, risk rationale, owner, treatment, remediation, framework mappings, and retest result.]

## 8. Log triage and detection coverage

- **Log source:** [Source or “No real access-log source available”]
- **Format and validation:** [Format; how validated]
- **Time range:** [Start/end]
- **Alerts and follow-up:** [Summary]
- **Coverage gaps:** [Events not logged, parser limitations, retention, false positives]

Clearly distinguish synthetic parser tests from logs collected from the application.

## 9. Framework mapping and gap analysis

Summarise relevant OWASP categories, NIST CSF 2.0 outcomes, and ISO/IEC 27001 controls/themes. Verify mapping against the applicable framework version and organisational context. A mapping does not imply compliance or certification.

For each gap, state desired outcome, current state, evidence, gap, action, owner, deadline, and status. See `framework-gap-analysis.csv`.

## 10. Remediation plan

| Priority | Action | Owner | Due date | Status |
|---|---|---|---|---|
| [Priority] | [Specific action] | [Owner] | [Date] | [Open] |

For accepted risks, record the approving risk owner, rationale, expiry/review date, and compensating controls.

## 11. Retesting

[Record each finding's retest date, repeated steps, evidence, outcome, and residual risk. Explain any finding that could not be retested.]

## 12. Limitations

[Document test coverage, assumptions, unavailable logs, sample data, false-positive risks, and untested areas.]

## 13. Lessons learned and next review

[Describe process improvements, corrective-action causes, and when the assessment should be repeated.]

## 14. Appendices

- Commands executed
- Sanitised evidence references
- Finding register and generated `report.json`
- Risk-rating definitions
- Scope approval

## Live testing results - 30 October 2026

### Test environment

- Application: OWASP Juice Shop 20.3.0
- Environment: Authorised local lab on Windows
- Target: http://localhost:3000
- Assessment tool: HeadersCheck.java
- Java runtime: Eclipse Temurin OpenJDK 25.0.3
- HTTP protocol: HTTP/1.1

### HTTP security-header assessment

The application returned HTTP 200. The Java security-header
checker identified two present headers and four missing headers.

| Security header | Result |
|---|---|
| Content-Security-Policy | Missing |
| Strict-Transport-Security | Missing |
| X-Content-Type-Options | Present |
| X-Frame-Options | Present |
| Referrer-Policy | Missing |
| Permissions-Policy | Missing |

**Result:** Four of six expected security headers were absent
from the tested HTTP response.

**Interpretation:** The missing CSP, Referrer-Policy and
Permissions-Policy headers indicate potential browser security
hardening opportunities. HSTS is normally applicable to HTTPS,
so its absence on this localhost HTTP endpoint is not by
itself evidence of a security vulnerability.

The test checks header presence, not the effectiveness of
header values or exploitability.

**Related finding:** VUL-007 - Incomplete or permissive
browser security controls.

**Framework mappings:**
- OWASP Top 10 (2021): A05 Security Misconfiguration
- NIST CSF 2.0: PR.PS-01, PR.PS-06
- ISO/IEC 27001:2022 Annex A: 8.9, 8.28

### Java tool execution

| Tool | Execution result | Evidence |
|---|---|---|
| FindingsExport.java | Successful; eight preliminary findings exported | output/report.json |
| LogTriage.java | Successful; suspicious patterns detected in sample logs | samples/sample-access.log |
| HeadersCheck.java | Successful; live HTTP 200 response analysed | evidence/headers-check.txt |

### Evidence limitations

The header assessment is a confirmed live observation,
not proof of successful exploitation.

The log triage used sample data and does not establish that
the detected requests occurred against the running application.

The other seven findings remain preliminary source-code or
configuration observations unless separately validated.

The risk ratings are provisional and do not constitute
CVSS scores or a certification of compliance.


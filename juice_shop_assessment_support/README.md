# Web Application Security Assessment of OWASP Juice Shop

An authorised, lab-only assessment workflow for OWASP Juice Shop. The project supports scoping, threat modelling, selected technical checks, risk rating, remediation tracking, retesting, and reporting. The tooling is written in Java and findings can be mapped to NIST CSF 2.0 and ISO/IEC 27001 themes.

> **Ethics and legal:** Test only your own local Juice Shop instance or systems for which you have explicit written authorisation. Do not use the tools against public or third-party targets.

## Requirements

- Docker, if you use the Docker-based lab setup.
- Java 17 or later recommended. `FindingsExport.java` uses a Java `record`, which requires Java 16 or later.
- The three existing Java files: `HeadersCheck.java`, `LogTriage.java`, and `FindingsExport.java`.

## Quick start

### 1. Start Juice Shop locally

```powershell
docker run --rm -p 3000:3000 bkimminich/juice-shop
```

Leave that terminal open. In a second terminal, move to your project directory.

> **Log note:** Mapping a host folder into a container does not, by itself, guarantee that Juice Shop writes combined-format HTTP access logs there. Do not assume `logs/access.log` exists. Use an access log only after confirming its source, format, and contents. A synthetic sample is included for testing the parser, clearly labelled as synthetic.

### 2. Check headers

```powershell
java HeadersCheck.java http://localhost:3000
```

The default URL is `http://localhost:3000`, so `java HeadersCheck.java` also works if the application is available there.

### 3. Test log triage

```powershell
java LogTriage.java logs/access.log 5
```

The included `logs/access.log` is synthetic test data, not evidence collected from Juice Shop. The optional threshold defaults to 5.

### 4. Export the findings register to JSON

```powershell
java FindingsExport.java findings.csv > report.json
```

The included `findings.csv` contains **illustrative sample rows only**. Replace them with verified observations before using the output as an assessment result.

## Repository layout

```text
README.md
HeadersCheck.java                 # existing file; preserve and test
LogTriage.java                    # existing file; preserve and test
FindingsExport.java               # existing file; preserve and test
findings.csv                      # findings exporter input
logs/access.log                   # synthetic parser test data only
findings/finding-template.md      # copy once per finding
scope-and-rules-of-engagement.md
pasta-threat-model.md
framework-gap-analysis.csv
retesting-checklist.md
report.md                         # final written report template
evidence/README.md                # evidence naming and handling guide
```

## Tools

| Tool | Purpose | NIST CSF 2.0 alignment (indicative) |
|---|---|---|
| `HeadersCheck.java` | Reports selected missing response headers, server banners, and cookie attributes | Protect |
| `LogTriage.java` | Flags selected request patterns, repeated 401/403 responses, and repeated 5xx responses in a compatible access log | Detect |
| `FindingsExport.java` | Validates CSV rows, scores likelihood × impact, assigns a rating, and adds mapping labels | Govern / Identify / Protect / Detect, depending on finding |

The mappings in this project are working references for organising the assessment. They are not a formal certification or a substitute for checking the relevant NIST CSF 2.0 outcomes and ISO/IEC 27001 requirements in context.

## Risk rating

Score = likelihood × impact, with each value from 1 to 5.

| Score | Rating |
|---:|---|
| 15–25 | Critical |
| 10–14 | High |
| 5–9 | Medium |
| 1–4 | Low |

The existing exporter uses thresholds of Critical >= 15, High >= 10, Medium >= 5, otherwise Low. A score of 5–9 is Medium, 10–14 High, and 15–25 Critical. Ensure the report uses the same thresholds. This is a project-defined rating scale, not CVSS.

Every finding should have an owner, treatment, evidence, remediation, and retest status. Record accepted risks explicitly with the approving role and review date.

## Methodology

1. **Scope:** document the target, dates, permissions, exclusions, and safety limits.
2. **Threat model:** describe assets, trust boundaries, data flows, plausible threats, and mitigations.
3. **Reconnaissance:** map the local app and inspect selected HTTP headers and cookies.
4. **Testing:** conduct controlled tests within the agreed scope; record steps and evidence.
5. **Detection:** triage compatible access logs and note what is and is not logged.
6. **Risk rating:** score likelihood and impact; use CVSS only where appropriate and supported by evidence.
7. **Remediation and retesting:** document fixes, retest using repeatable steps, and record results.
8. **Reporting:** summarise verified results, limitations, framework mappings, and corrective actions.

## Important limitations

- A missing header is an observation to assess, not automatic proof of an exploitable vulnerability.
- Cookie flag checks based on simple string matching can produce misleading results.
- Log pattern matches are indicators, not proof that an attack succeeded. Simple regular expressions can miss variants and generate false positives.
- Synthetic logs and sample CSV rows must never be represented as real assessment evidence.
- Do not claim the application is secure solely because these tools produce no alerts.

## Completion checklist

See `retesting-checklist.md` and the deliverables section in `report.md`. Preserve the original Java files while testing; change them only after recording baseline behaviour and identifying a specific defect.

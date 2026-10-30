# Web-Application-Security-Assessment-of-OWASP-Juice-Shop-project

WTC-3XZ8AMMA
# Web Application Security Assessment of OWASP Juice Shop

## Project Overview

This project conducts a security assessment of OWASP Juice Shop, an intentionally vulnerable web application designed for security education and testing.

The assessment uses Java-based security tools, source-code review, threat modelling, risk scoring, and security-framework mapping to identify and document potential vulnerabilities in a controlled local environment.

The project follows a structured security assessment process: defining scope, identifying threats, reviewing vulnerabilities, evaluating risks, recommending remediation, and planning verification.

## Project Objectives

- Identify potential web application security vulnerabilities using the OWASP Top 10.
- Examine HTTP response headers and cookie security attributes.
- Analyse application access logs for indicators of suspicious activity.
- Assess vulnerabilities using likelihood and impact scores.
- Map findings to NIST Cybersecurity Framework 2.0 and ISO/IEC 27001 security themes.
- Document remediation recommendations and retesting requirements.

## Technologies Used

- **Java 17+** — Development and execution of the three security assessment tools.
- **OWASP Juice Shop** — Local vulnerable application used as the assessment target.
- **Node.js and npm** — Installation and execution of Juice Shop.
- **PowerShell** — Running commands and managing assessment files.
- **CSV and JSON** — Storing findings and generating structured reports.
- **Markdown** — Maintaining assessment documentation.

## Java Security Assessment Tools

### 1. HeadersCheck.java

Checks the HTTP response of a local Juice Shop instance for common browser security controls.

The tool examines Content-Security-Policy, Strict-Transport-Security, X-Content-Type-Options, X-Frame-Options, Referrer-Policy, and Permissions-Policy.

It also examines available Set-Cookie headers for Secure, HttpOnly and SameSite attributes, and reports potentially revealing server headers.

Run:

```powershell
java HeadersCheck.java http://localhost:3000
```

To save results:

```powershell
java HeadersCheck.java http://localhost:3000 > evidence\headers-check.txt
```

The application must be running before the check can succeed. HSTS findings must be interpreted in the context of HTTPS rather than treating its absence on local HTTP as a confirmed vulnerability.

### 2. LogTriage.java

Analyses access logs in combined-log format to identify suspicious request patterns.

The tool detects indicators of SQL injection, cross-site scripting (XSS), path traversal, repeated HTTP 401/403 responses, and repeated HTTP 5xx errors.

Run against the included sample log:

```powershell
java LogTriage.java samples\sample-access.log
```

Save the sample test results:

```powershell
java LogTriage.java samples\sample-access.log > evidence\log-triage-sample.txt
```

The sample log is used for parser testing. Its alerts are not evidence of real attacks against the local application.

### 3. FindingsExport.java

Reads documented vulnerabilities from `findings.csv`, calculates risk scores, assigns severity ratings, maps OWASP categories to NIST CSF 2.0 and ISO 27001 themes, and generates a JSON report.

Each risk score is calculated as:

**Risk Score = Likelihood × Impact**

Both likelihood and impact are rated from 1 to 5.

| Score | Risk Rating |
|---|---|
| 15–25 | Critical |
| 10–14 | High |
| 5–9 | Medium |
| 1–4 | Low |

Generate the JSON report:

```powershell
java FindingsExport.java findings.csv > output\report.json
```

## Assessment Methodology

The project uses PASTA (Process for Attack Simulation and Threat Analysis) as a threat-modelling approach, together with the OWASP Top 10 for vulnerability classification.

The assessment process includes:

1. Defining the application scope and rules of engagement.
2. Identifying application assets, attack surfaces, and potential threats.
3. Reviewing relevant source code and configuration.
4. Conducting authorised local security checks.
5. Assigning preliminary likelihood and impact scores.
6. Mapping findings to security-framework themes.
7. Documenting recommended remediation and verification procedures.

## Assessment Findings

The assessment documents eight preliminary findings based on source-code and configuration observations.

| Finding | Description | Severity |
|---|---|---|
| VUL-001 | SQL injection in login and product search | Critical |
| VUL-002 | Potential server-side request forgery | Critical |
| VUL-003 | Weak password hashing and embedded JWT key material | Critical |
| VUL-004 | Dynamic evaluation of profile data | Critical |
| VUL-005 | Weak redirect URL validation | Medium |
| VUL-006 | Potential application log disclosure | High |
| VUL-007 | Incomplete browser security controls | Medium |
| VUL-008 | Training configuration and backup exposure risk | Medium |

**Assessment summary:** 4 Critical, 1 High, 3 Medium and 0 Low findings.

These are preliminary source-review findings. The risk ratings do not establish that the vulnerabilities were successfully exploited or independently verified through runtime testing.

## Project Structure

```text
Web-Application-Security-Assessment-of-OWASP-Juice-Shop-project/
├── FindingsExport.java
├── HeadersCheck.java
├── LogTriage.java
├── findings.csv
├── framework-gap-analysis.csv
├── pasta-threat-model.md
├── report.md
├── retesting-checklist.md
├── scope-and-rules-of-engagement.md
├── README.md
├── evidence/
│   ├── headers-check.txt
│   └── log-triage-sample.txt
├── output/
│   └── report.json
└── samples/
    └── sample-access.log
```

The evidence files are created when their respective commands are run successfully. The sample log is not a record of actual Juice Shop traffic.

## Running the Project

### Prerequisites

- Java 17 or later
- Node.js and npm compatible with the selected Juice Shop release
- PowerShell
- A local installation of OWASP Juice Shop

### Start OWASP Juice Shop

From the separate Juice Shop application directory:

```powershell
npm.cmd start
```

The application must be installed and built successfully before it can start.

### Run the Java Tools

From the security assessment project directory:

```powershell
java FindingsExport.java findings.csv > output\report.json
java LogTriage.java samples\sample-access.log > evidence\log-triage-sample.txt
java HeadersCheck.java http://localhost:3000 > evidence\headers-check.txt
```

The header check requires the Juice Shop application to be running.

## Security Framework Alignment

Findings are mapped to relevant themes from:

- **OWASP Top 10 (2021):** Classification of common web application security risks.
- **NIST Cybersecurity Framework 2.0:** Governance, identification, protection, detection, response and recovery.
- **ISO/IEC 27001:2022:** Security management and control themes.

These mappings support analysis and remediation planning. They do not represent a formal compliance audit or certification.

## Limitations

OWASP Juice Shop intentionally contains vulnerabilities for educational purposes. The assessment is restricted to a controlled, authorised local environment.

Source-code observations, sample-log detections and runtime security checks are documented separately. No successful exploitation is claimed without supporting evidence.

The assessment does not establish complete vulnerability coverage or certify the application as secure.

## Supporting Documentation

- `scope-and-rules-of-engagement.md` — Assessment scope and authorised testing boundaries.
- `pasta-threat-model.md` — Threat modelling and attack-surface analysis.
- `report.md` — Detailed security assessment report.
- `framework-gap-analysis.csv` — Security-framework mappings and identified gaps.
- `retesting-checklist.md` — Verification steps following remediation.
- `findings.csv` — Structured findings and risk inputs.
- `output/report.json` — Java-generated findings report.

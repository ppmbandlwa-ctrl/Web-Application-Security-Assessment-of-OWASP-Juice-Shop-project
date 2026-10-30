# Testing, Retesting, and Deliverables Checklist

Use this checklist to track work. Do not mark a step complete until you have evidence.

## Before testing

- [ ] Scope and rules of engagement completed and approved
- [ ] Local Juice Shop target confirmed
- [ ] App version, Docker image, Java version, and test date recorded
- [ ] Original Java files backed up or committed before edits
- [ ] Test accounts use dummy lab data
- [ ] Evidence directory created; secrets-redaction rules understood

## Baseline and tooling

- [ ] `java -version` and `javac -version` recorded
- [ ] `HeadersCheck.java` run against the local target
- [ ] Header results reviewed manually; observations not automatically labelled vulnerabilities
- [ ] `FindingsExport.java` run against `findings.csv`
- [ ] `report.json` parsed as valid JSON
- [ ] `LogTriage.java` run against synthetic test log
- [ ] Log parser output compared with expected indicators
- [ ] Actual log source and format verified before using any log as real evidence

## Finding quality

- [ ] Every reported finding has reproducible steps
- [ ] Evidence is sanitised and referenced
- [ ] Severity follows the project's stated thresholds
- [ ] Likelihood and impact have written rationales
- [ ] Every finding has an owner and treatment
- [ ] OWASP, NIST, and ISO mappings are checked and described as relevant mappings, not certification
- [ ] False positives and unverified hypotheses are clearly labelled

## Fix and retest

- [ ] Remediation owner and target date assigned
- [ ] Fix recorded (commit, configuration change, or documented recommendation)
- [ ] Original test repeated under comparable conditions
- [ ] Retest evidence saved and sanitised
- [ ] Result marked Passed / Failed / Partially passed / Not tested
- [ ] Residual risk and accepted risks documented with approver and review date

## Final deliverables

- [ ] Scope and rules of engagement
- [ ] PASTA threat model and verified data-flow notes
- [ ] Findings CSV and one markdown record per finding
- [ ] Generated `report.json`
- [ ] Completed `report.md`
- [ ] Framework gap-analysis register
- [ ] Retesting evidence and corrective-action notes
- [ ] Lessons learned and next review date
- [ ] No synthetic data represented as real evidence

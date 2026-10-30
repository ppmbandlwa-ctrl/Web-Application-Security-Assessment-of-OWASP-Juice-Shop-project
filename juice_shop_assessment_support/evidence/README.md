# Evidence handling

Store only evidence from the authorised local lab. Keep evidence sanitised and do not commit credentials, session cookies, tokens, personal data, or secrets.

## Suggested naming

Use a predictable name, for example:

`YYYY-MM-DD_F-01_headers-response.txt`

or

`YYYY-MM-DD_logtriage-synthetic-output.txt`

Include the date, finding ID or tool, and a short description. Record the command, environment, and what the evidence demonstrates in the relevant finding.

## Evidence log

| Reference | Date/time | Finding/tool | Description | Redacted? |
|---|---|---|---|---|
| [E-001] | [Timestamp + zone] | [F-01 / HeadersCheck] | [Description] | [Yes/No] |

Do not label synthetic examples as application-collected evidence. If evidence contains sensitive values, redact them before sharing or committing.

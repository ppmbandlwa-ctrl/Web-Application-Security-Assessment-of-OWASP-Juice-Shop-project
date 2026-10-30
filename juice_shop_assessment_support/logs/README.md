# Logs directory

`access.log` in this directory is a **synthetic test fixture** intended to exercise the simple `LogTriage.java` parser. It was not collected from Juice Shop and must not be cited as real assessment evidence.

For real lab log review, first verify:
- which component generates the log,
- where the file is written,
- the log format and fields,
- the time range and timezone,
- whether the data is complete enough for the conclusions you plan to draw.

A Docker volume mount alone does not guarantee the application emits a combined-format access log at this path.

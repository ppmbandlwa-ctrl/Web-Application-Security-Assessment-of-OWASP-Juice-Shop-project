import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/** Triage a combined-format access log for attack indicators (NIST Detect: adverse event analysis).
 *  Juice Shop writes access logs (combined format) to its logs/ folder.
 *  Run: java LogTriage.java logs/access.log [failThreshold]   (Java 11+) */
public class LogTriage {
    static final Pattern LINE =
            Pattern.compile("^(\\S+) \\S+ \\S+ \\[([^\\]]+)\\] \"(\\S+) (\\S+)[^\"]*\" (\\d{3}) \\S+");
    static final Map<String, Pattern> PATTERNS = new LinkedHashMap<>();
    static {
        PATTERNS.put("SQL injection hint",
                Pattern.compile("('|%27)|(\\bunion\\b.*\\bselect\\b)|(\\bor\\b\\s+1=1)", Pattern.CASE_INSENSITIVE));
        PATTERNS.put("XSS hint", Pattern.compile("<script|onerror=|javascript:", Pattern.CASE_INSENSITIVE));
        PATTERNS.put("Path traversal hint", Pattern.compile("\\.\\./|%2e%2e", Pattern.CASE_INSENSITIVE));
    }

    static String decode(String s) {
        try { return URLDecoder.decode(s, StandardCharsets.UTF_8); }
        catch (IllegalArgumentException e) { return s; } // malformed escape: keep raw
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) { System.err.println("Usage: java LogTriage.java <logfile> [failThreshold]"); return; }
        int threshold = args.length > 1 ? Integer.parseInt(args[1]) : 5;
        Map<String, Integer> fails = new HashMap<>(), errors = new HashMap<>();
        Map<String, List<String>> hits = new LinkedHashMap<>();

        for (String raw : Files.readAllLines(Path.of(args[0]), StandardCharsets.UTF_8)) {
            Matcher m = LINE.matcher(raw);
            if (!m.find()) continue;
            String ip = m.group(1), ts = m.group(2), url = decode(m.group(4));
            int status = Integer.parseInt(m.group(5));
            if (status == 401 || status == 403) fails.merge(ip, 1, Integer::sum);
            if (status >= 500) errors.merge(ip, 1, Integer::sum);
            for (Map.Entry<String, Pattern> p : PATTERNS.entrySet())
                if (p.getValue().matcher(url).find())
                    hits.computeIfAbsent(p.getKey(), k -> new ArrayList<>())
                        .add(ip + " " + ts + " " + url.substring(0, Math.min(80, url.length())));
        }

        System.out.println("# Alerts\n");
        boolean any = false;
        for (Map.Entry<String, Integer> e : fails.entrySet())
            if (e.getValue() >= threshold) { any = true;
                System.out.printf("- HIGH: %d auth failures (401/403) from %s: possible brute force or access probing%n",
                        e.getValue(), e.getKey()); }
        for (Map.Entry<String, List<String>> e : hits.entrySet()) { any = true;
            System.out.printf("- MEDIUM: %d requests with %s. Example: %s%n",
                    e.getValue().size(), e.getKey(), e.getValue().get(0)); }
        for (Map.Entry<String, Integer> e : errors.entrySet())
            if (e.getValue() >= threshold) { any = true;
                System.out.printf("- LOW: %d server errors triggered by %s (verbose errors? fuzzing?)%n",
                        e.getValue(), e.getKey()); }
        if (!any) System.out.println("- No indicators found.");
        System.out.println("\nNext: triage each alert (true or false positive), document it, and record the incident per your playbook.");
    }
}

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/** Converts findings.csv into a scored, mapped JSON report (no libraries needed).
 *  CSV header: title,category,location,likelihood,impact,owner,treatment,remediation
 *  category is the OWASP code, e.g. A03.   Run: java FindingsExport.java findings.csv > report.json */
public class FindingsExport {
    // code -> {OWASP name, NIST CSF 2.0, ISO 27001 theme}
    static final Map<String, String[]> MAP = new LinkedHashMap<>();
    static {
        MAP.put("A01", new String[]{"Broken Access Control", "Protect: identity and access control", "Access control"});
        MAP.put("A02", new String[]{"Cryptographic Failures", "Protect: data security", "Cryptography"});
        MAP.put("A03", new String[]{"Injection (SQLi, XSS)", "Protect: platform security", "Secure development"});
        MAP.put("A04", new String[]{"Insecure Design", "Govern: risk management strategy", "Risk assessment"});
        MAP.put("A05", new String[]{"Security Misconfiguration", "Protect: platform security", "Configuration management"});
        MAP.put("A06", new String[]{"Vulnerable Components", "Identify: asset management", "Vulnerability management"});
        MAP.put("A07", new String[]{"Authentication Failures", "Protect: authentication", "Access control"});
        MAP.put("A08", new String[]{"Integrity Failures", "Protect: data security", "Change management"});
        MAP.put("A09", new String[]{"Logging and Monitoring Failures", "Detect: continuous monitoring", "Logging and monitoring"});
        MAP.put("A10", new String[]{"SSRF", "Protect: platform security", "Network security"});
    }

    static String rating(int s) { return s >= 15 ? "Critical" : s >= 10 ? "High" : s >= 5 ? "Medium" : "Low"; }

    static String q(String s) { // JSON string with escaping
        StringBuilder b = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': b.append("\\\""); break;
                case '\\': b.append("\\\\"); break;
                case '\n': b.append("\\n"); break;
                case '\r': break;
                case '\t': b.append("\\t"); break;
                default: if (c < 0x20) b.append(String.format("\\u%04x", (int) c)); else b.append(c);
            }
        }
        return b.append('"').toString();
    }

    static List<String> splitCsv(String line) { // handles "quoted, fields" and "" escapes
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQ = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQ) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"') { cur.append('"'); i++; }
                else if (c == '"') inQ = false;
                else cur.append(c);
            } else if (c == '"') inQ = true;
            else if (c == ',') { out.add(cur.toString().trim()); cur.setLength(0); }
            else cur.append(c);
        }
        out.add(cur.toString().trim());
        return out;
    }

    record Finding(String title, String code, String where, int l, int i, String owner, String treat, String fix) {
        int score() { return l * i; }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) { System.err.println("Usage: java FindingsExport.java findings.csv"); return; }
        List<String> lines = Files.readAllLines(Path.of(args[0]), StandardCharsets.UTF_8);
        List<Finding> fs = new ArrayList<>();
        for (int n = 1; n < lines.size(); n++) {
            if (lines.get(n).isBlank()) continue;
            List<String> c = splitCsv(lines.get(n));
            if (c.size() < 8) { System.err.println("Skipping line " + (n + 1) + ": need 8 columns"); continue; }
            String code = c.get(1).toUpperCase();
            int l = Integer.parseInt(c.get(3)), i = Integer.parseInt(c.get(4));
            if (!MAP.containsKey(code) || l < 1 || l > 5 || i < 1 || i > 5 || c.get(5).isEmpty()) {
                System.err.println("Skipping line " + (n + 1) + ": bad category, score outside 1-5, or no owner");
                continue;
            }
            fs.add(new Finding(c.get(0), code, c.get(2), l, i, c.get(5), c.get(6), c.get(7)));
        }
        fs.sort(Comparator.comparingInt(Finding::score).reversed());
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String r : new String[]{"Critical", "High", "Medium", "Low"}) counts.put(r, 0);
        fs.forEach(f -> counts.merge(rating(f.score()), 1, Integer::sum));

        StringBuilder j = new StringBuilder("{\n  \"target\": \"OWASP Juice Shop (local lab)\",\n");
        j.append("  \"method\": \"PASTA, OWASP Top 10, likelihood x impact, NIST CSF 2.0 and ISO 27001 mapping\",\n");
        j.append("  \"summary\": {\"total\": ").append(fs.size());
        counts.forEach((k, v) -> j.append(", ").append(q(k.toLowerCase())).append(": ").append(v));
        j.append("},\n  \"findings\": [\n");
        for (int k = 0; k < fs.size(); k++) {
            Finding f = fs.get(k);
            String[] m = MAP.get(f.code());
            j.append("    {\"id\": ").append(q(String.format("F-%02d", k + 1)))
             .append(", \"title\": ").append(q(f.title()))
             .append(", \"owasp\": ").append(q(f.code() + " " + m[0]))
             .append(", \"location\": ").append(q(f.where()))
             .append(", \"likelihood\": ").append(f.l()).append(", \"impact\": ").append(f.i())
             .append(", \"score\": ").append(f.score()).append(", \"rating\": ").append(q(rating(f.score())))
             .append(", \"owner\": ").append(q(f.owner())).append(", \"treatment\": ").append(q(f.treat()))
             .append(", \"remediation\": ").append(q(f.fix()))
             .append(", \"nist_csf\": ").append(q(m[1])).append(", \"iso_27001_theme\": ").append(q(m[2]))
             .append(k < fs.size() - 1 ? "},\n" : "}\n");
        }
        System.out.println(j.append("  ]\n}"));
    }
}

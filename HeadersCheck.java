import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.*;

/** Checks security headers and cookie flags on a LOCAL Juice Shop (NIST Protect: platform security).
 *  Run: java HeadersCheck.java [url] [--i-own-this]   (Java 11+) */
public class HeadersCheck {
    static final Map<String, String> EXPECTED = new LinkedHashMap<>();
    static {
        EXPECTED.put("content-security-policy", "Limits script sources; mitigates XSS");
        EXPECTED.put("strict-transport-security", "Forces HTTPS (HSTS)");
        EXPECTED.put("x-content-type-options", "Stops MIME sniffing");
        EXPECTED.put("x-frame-options", "Clickjacking protection");
        EXPECTED.put("referrer-policy", "Limits Referer data leakage");
        EXPECTED.put("permissions-policy", "Restricts browser features");
    }

    static List<String> weakCookieFlags(String raw) {
        String low = raw.toLowerCase();
        List<String> missing = new ArrayList<>();
        for (String f : new String[] {"secure", "httponly", "samesite"})
            if (!low.contains(f)) missing.add(f);
        return missing;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://localhost:3000";
        boolean owned = false;
        for (String a : args) {
            if (a.equals("--i-own-this")) owned = true; else url = a;
        }
        URI uri = URI.create(url);
        String host = uri.getHost();
        if (!"localhost".equals(host) && !"127.0.0.1".equals(host) && !owned) {
            System.err.println("Refusing: only test systems you own or are authorized to test.");
            System.exit(1);
        }
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();
        HttpRequest req = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(10))
                .header("User-Agent", "assessment-lab/1.0").GET().build();
        HttpResponse<Void> res = client.send(req, HttpResponse.BodyHandlers.discarding());
        Map<String, List<String>> h = res.headers().map(); // keys are lower-case

        System.out.printf("# Header check for %s (HTTP %d)%n%n", url, res.statusCode());
        int missing = 0;
        for (Map.Entry<String, String> e : EXPECTED.entrySet()) {
            boolean ok = h.containsKey(e.getKey());
            if (!ok) missing++;
            System.out.printf("- [%s] %s: %s%n", ok ? "OK" : "MISSING", e.getKey(), e.getValue());
        }
        for (String banner : new String[] {"server", "x-powered-by"})
            if (h.containsKey(banner))
                System.out.printf("- [INFO] %s reveals: %s (consider hiding)%n", banner, h.get(banner).get(0));
        for (String c : h.getOrDefault("set-cookie", List.of())) {
            List<String> weak = weakCookieFlags(c);
            if (!weak.isEmpty())
                System.out.printf("- [WEAK COOKIE] %s lacks: %s%n", c.split("=")[0], String.join(", ", weak));
        }
        System.out.printf("%n%d of %d expected headers missing.%n", missing, EXPECTED.size());
    }
}


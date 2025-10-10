package util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealeniumConsoleReporter extends AppenderBase<ILoggingEvent> {
    private static final AtomicBoolean INSTALLED = new AtomicBoolean(false);

    // Very permissive patterns – Healenium messages vary by version.
    private static final Pattern OLD_LOCATOR = Pattern.compile("(?i)(failed|old).*?(By\\.[^\\s,]+|\\[[^\\]]+\\]|\"[^\"]+\")");
    private static final Pattern NEW_LOCATOR = Pattern.compile("(?i)(healed|new).*?(By\\.[^\\s,]+|\\[[^\\]]+\\]|\"[^\"]+\")");
    private static final Pattern SCORE       = Pattern.compile("(?i)score\\s*[:=]\\s*([0-9.]+)");

    public static void install() {
        if (INSTALLED.compareAndSet(false, true)) {
            Logger hlm = (Logger) LoggerFactory.getLogger("com.epam.healenium");
            hlm.setLevel(Level.DEBUG); // ensure we see healing details
            HealeniumConsoleReporter app = new HealeniumConsoleReporter();
            app.setName("HealeniumConsoleReporter");
            app.start();
            hlm.addAppender(app);
            System.out.println("[Healenium] Console reporter installed (listening for healing events)...");
        }
    }

    @Override
    protected void append(ILoggingEvent e) {
        String msg = e.getFormattedMessage();
        if (msg == null) return;

        // Only react to messages that mention healing
        if (!msg.toLowerCase().contains("heal")) return;

        String oldLoc = extract(OLD_LOCATOR, msg);
        String newLoc = extract(NEW_LOCATOR, msg);
        String score  = extract(SCORE, msg);

        StringBuilder line = new StringBuilder("[HEALED] ");
        if (oldLoc != null) line.append("from ").append(oldLoc).append(" ");
        if (newLoc != null) line.append("to ").append(newLoc).append(" ");
        if (score  != null) line.append("(score ").append(score).append(") ");
        // Fallback: if we couldn’t parse details, still print the raw message
        if (line.toString().equals("[HEALED] ")) line.append(msg);

        System.out.println(line.toString().trim());
    }

    private static String extract(Pattern p, String s) {
        Matcher m = p.matcher(s);
        if (m.find()) {
            // prefer the locator-looking group if present
            for (int i = m.groupCount(); i >= 1; i--) {
                String g = m.group(i);
                if (g != null && g.length() > 2) return g.trim();
            }
            return m.group().trim();
        }
        return null;
    }
}

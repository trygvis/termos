package no.hackaton.termos.extra;

import no.hackaton.termos.*;
import static no.hackaton.termos.ReadlineUtil.*;
import static no.hackaton.termos.extra.Repl.repl;

import java.io.*;
import java.util.*;

/**
 * TODO: Use a thread factory
 * TODO: Support using UncaughtException.
 *
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ReplThread implements Closeable {

    private final InputStream stdin;
    private final OutputStream stdout;
    private final OutputStream stderr;
    private final Thread thread;

    public ReplThread(final InputStream stdin, final OutputStream stdout, final OutputStream stderr,
                      final ReadLineEnvironment environment, final Map<String, CliCommand> commands,
                      final Runnable onExit) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.stderr = stderr;

        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    repl(stdin, stdout, stderr, environment, commands, "READY> ");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    try {
                        onExit.run();
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        });
        thread.setDaemon(true);
    }

    public void start() {
        thread.start();
    }

    public void close() throws IOException {
        closeSilently(stdin);
        closeSilently(stdout);
        closeSilently(stderr);
    }
}

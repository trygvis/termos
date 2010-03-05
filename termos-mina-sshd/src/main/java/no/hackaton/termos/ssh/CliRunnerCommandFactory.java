package no.hackaton.termos.ssh;

import no.hackaton.termos.*;
import static no.hackaton.termos.ReadlineUtil.*;
import no.hackaton.termos.extra.*;
import org.apache.sshd.common.*;
import org.apache.sshd.server.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class CliRunnerCommandFactory implements Factory<Command> {
    private final Map<String, CliCommand> commands;

    public CliRunnerCommandFactory(Map<String, CliCommand> commands) {
        this.commands = commands;
    }

    public Command create() {
        return new Command() {
            private InputStream stdin;
            private OutputStream stdout;
            private OutputStream stderr;
            private ExitCallback exitCallback;
            private ReplThread repl;

            public void setInputStream(InputStream stdin) {
                this.stdin = stdin;
            }

            public void setOutputStream(OutputStream stdout) {
                this.stdout = stdout;
            }

            public void setErrorStream(OutputStream stderr) {
                this.stderr = stderr;
            }

            public void setExitCallback(ExitCallback exitCallback) {
                this.exitCallback = exitCallback;
            }

            public void start(Environment e) throws IOException {
                repl = new ReplThread(stdin, stdout, stderr, toReadLineEnvironment(e), commands, new Runnable() {
                    public void run() {
                        exitCallback.onExit(0);
                    }
                });
                repl.start();
            }

            public void destroy() {
                closeSilently(repl);
            }
        };
    }

    public static boolean getBoolean(Map<PtyMode, Integer> map, PtyMode mode) {
        Integer i = map.get(mode);

        return i != null && i == 1;
    }

    public static ReadLineEnvironment toReadLineEnvironment(Environment environment) {
        Map<PtyMode, Integer> map = environment.getPtyModes();
//        for (Entry<PtyMode, Integer> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + "=" + entry.getValue());
//        }
        String encoding = null;
        Integer erase = map.get(PtyMode.VERASE);

        return new ReadLineEnvironment(encoding, erase,
                getBoolean(map, PtyMode.ICRNL), getBoolean(map, PtyMode.OCRNL));
    }
}

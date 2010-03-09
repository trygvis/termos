package no.hackaton.termos.ssh;

import static java.lang.Integer.*;
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

    private static Integer getInteger(Map<String, String> env, String key) {
        String s = env.get(key);

        if (s == null) {
            return null;
        }

        try {
            return valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static ReadLineEnvironment toReadLineEnvironment(Environment environment) {
        final Map<String, String> env = environment.getEnv();
        Map<PtyMode, Integer> ptyModes = environment.getPtyModes();
//        for (Entry<PtyMode, Integer> entry : ptyModes.entrySet()) {
//            System.out.println(entry.getKey() + "=" + entry.getValue());
//        }
        String encoding = null;
        Integer erase = ptyModes.get(PtyMode.VERASE);

        final ReadLineEnvironment readLineEnvironment = new ReadLineEnvironment(encoding, erase,
            getBoolean(ptyModes, PtyMode.ICRNL),
            getBoolean(ptyModes, PtyMode.OCRNL),
            getInteger(env, Environment.ENV_COLUMNS),
            getInteger(env, Environment.ENV_LINES));

        environment.addSignalListener(new SignalListener() {
            public void signal(Signal signal) {
                readLineEnvironment.onWindowChange(
                    getInteger(env, Environment.ENV_COLUMNS),
                    getInteger(env, Environment.ENV_LINES));
            }
        });

        return readLineEnvironment;
    }
}

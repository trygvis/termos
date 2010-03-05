package no.hackaton.termos.ssh;

import no.hackaton.termos.*;
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
            private CliRunner cliRunner;

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
                String encoding = null;
                Integer erase = e.getPtyModes().get(PtyMode.VERASE);
                ReadLineEnvironment environment = new ReadLineEnvironment(encoding, erase);
                cliRunner = new CliRunner(stdin, stdout, stderr, environment, commands);
                cliRunner.start();
            }

            public void destroy() {
                ReadlineUtil.closeSilently(cliRunner);
            }
        };
    }
}

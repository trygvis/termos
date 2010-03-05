package no.hackaton.termos;

import static no.hackaton.termos.ReadlineUtil.*;
import no.hackaton.termos.commands.*;
import org.apache.sshd.*;
import org.apache.sshd.common.*;
import org.apache.sshd.server.*;
import org.apache.sshd.server.keyprovider.*;
import org.apache.sshd.server.session.*;

import java.io.*;
import java.security.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class SshTestServer {
    private static int port = 2222;

    private Map<String, CliCommand> commands = new HashMap<String, CliCommand>();

    public static void main(String[] args) throws IOException {
        new SshTestServer().work();
    }

    private void addCommand(CliCommand command) {
        commands.put(command.getId(), command);
    }

    private void work() throws IOException {
        addCommand(new SillyCommand("aaaaaa"));
        addCommand(new SillyCommand("aaaaab"));

        addCommand(new SillyCommand("bbbbbb"));
        addCommand(new SillyCommand("bbcccc"));

        addCommand(new SillyCommand("cccccc"));
        addCommand(new SillyCommand("ccdddd"));
        addCommand(new SillyCommand("ccddee"));

        addCommand(new SillyCommand("dddddd"));

//        commands.clear();
//        addCommand(new SillyCommand("abcX"));
//        addCommand(new SillyCommand("abcY"));

        // -----------------------------------------------------------------------
        //
        // -----------------------------------------------------------------------

        SshServer server = SshServer.setUpDefaultServer();
        server.setPort(port);
        server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File("hostkey.ser").getAbsolutePath()));
        server.setPublickeyAuthenticator(new PublickeyAuthenticator() {
            public boolean authenticate(String s, PublicKey publicKey, ServerSession serverSession) {
                return true;
            }
        });
        server.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String username, String password, ServerSession session) {
                return true;
            }
        });

        server.setShellFactory(new CliShellFactory());

        server.start();
    }

    private class CliShellFactory implements Factory<Command> {
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

                public void start(Environment environment) throws IOException {
                    cliRunner = new CliRunner(stdin, stdout, stderr, environment, exitCallback, commands);
                    cliRunner.start();
                }

                public void destroy() {
                    closeSilently(cliRunner);
                }
            };
        }
    }
}

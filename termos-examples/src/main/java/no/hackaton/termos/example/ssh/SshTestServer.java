package no.hackaton.termos.example.ssh;

import no.hackaton.termos.example.commands.*;
import no.hackaton.termos.extra.*;
import no.hackaton.termos.ssh.*;
import org.apache.sshd.*;
import org.apache.sshd.server.*;
import org.apache.sshd.server.keyprovider.*;
import org.apache.sshd.server.session.*;

import java.io.*;
import java.security.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class SshTestServer {
    private static int port = 4442;

    public static void main(String[] args) throws IOException {
        new SshTestServer().work();
    }

    private void work() throws IOException {
        CommandCollection commands;

//        commands = new CommandCollection().
//                addCommand(new SillyCommand("aaaaaa")).
//                addCommand(new SillyCommand("aaaaab")).
//                addCommand(new SillyCommand("bbbbbb")).
//                addCommand(new SillyCommand("bbcccc")).
//                addCommand(new SillyCommand("cccccc")).
//                addCommand(new SillyCommand("ccdddd")).
//                addCommand(new SillyCommand("ccddee")).
//                addCommand(new SillyCommand("dddddd"));

        // -----------------------------------------------------------------------
        //
        // -----------------------------------------------------------------------

//        commands = new CommandCollection().
//                addCommand(new SillyCommand("abcX")).
//                addCommand(new SillyCommand("abcY"));

        // -----------------------------------------------------------------------
        //
        // -----------------------------------------------------------------------

        commands = new CommandCollection();
        commands.
                addCommand(new HelpCommand(commands.commands)).
                addCommand(new JmxCommand());

        // -----------------------------------------------------------------------
        //
        // -----------------------------------------------------------------------

        createSshServer(commands).start();
    }

    private SshServer createSshServer(CommandCollection commands) throws IOException {
        SshServer server = SshServer.setUpDefaultServer();
        server.setPort(port);
        server.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(new File(System.getProperty("user.home"), ".ssh/termos-hostkey.ser").getAbsolutePath()));
        server.setPublickeyAuthenticator(new PublickeyAuthenticator() {
            public boolean authenticate(String s, PublicKey publicKey, ServerSession serverSession) {
                return true;
            }
        });
        server.setPasswordAuthenticator(new PasswordAuthenticator() {
            public boolean authenticate(String u, String password, ServerSession session) {
                return true;
            }
        });
        server.setShellFactory(new CliRunnerCommandFactory(commands.commands));
        return server;
    }
}

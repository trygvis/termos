package no.hackaton.termos.extra;

import no.hackaton.termos.*;
import static no.hackaton.termos.ReadlineUtil.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class CliRunner implements Runnable, Closeable {

    private final InputStream stdin;
    private final OutputStream stdout;
    private final LineOutput stdoutP;
    private final LineOutput stderrP;
    private final Thread thread;
    private final ReadLineEnvironment environment;
    private final Map<String, CliCommand> commands;

    public CliRunner(InputStream stdin, OutputStream stdout, OutputStream stderr,
                     ReadLineEnvironment environment, Map<String, CliCommand> commands) {
        this.stdin = stdin;
        this.stdout = stdout;
        this.environment = environment;
        this.commands = commands;

        stdoutP = new LineOutput(stdout);
        stderrP = new LineOutput(stderr);

        thread = new Thread(this, "ssh cli");
        thread.setDaemon(true);
    }

    public void start() {
        thread.start();
    }

    public void close() throws IOException {
        System.out.println("CliRunner.close");
        closeSilently(stdin);
        closeSilently(stdoutP);
        closeSilently(stderrP);
    }

    public void run() {
        try {
            doRun();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int doRun() throws IOException {
        String line;

        while (true) {
            ReadLine readLine = new ReadLine(stdin, stdout, environment);

            readLine.sendPrompt("Awesome!");
            line = readLine.readLine("READY> ", new CommandCompleter(commands.keySet()));

            if (line == null) {
                break;
            }

            line = line.trim();

            System.out.println("line = " + line);

            if ("".equals(line)) {
                continue;
            }

            if ("exit".equals(line)) {
                readLine.println("Later!");
                break;
            }

            String[] args = line.split(" ");

            CliCommand command = commands.get(args[0]);
            if (command == null) {
                readLine.println("Unknown command '" + line + "'.");
                continue;
            }

            String[] realArgs = new String[args.length - 1];
            System.arraycopy(args, 0, realArgs, 0, realArgs.length);
            command.run(stdoutP, realArgs);
        }

        return 10;
    }

    private class CommandCompleter implements Completer {
        private final Set<String> commands;

        public CommandCompleter(Set<String> commands) {
            this.commands = commands;
        }

        public List<String> complete(String string) {
            List<String> matches = new ArrayList<String>();

            for (String s : commands) {
                if(s.startsWith(string)) {
                    matches.add(s);
                }
            }

            return matches;
        }
    }
}

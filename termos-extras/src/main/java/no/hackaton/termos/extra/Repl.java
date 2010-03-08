package no.hackaton.termos.extra;

import no.hackaton.termos.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class Repl {

    public static int repl(InputStream stdin, OutputStream stdout, OutputStream stderr,
                           ReadLineEnvironment environment, Map<String, CliCommand> commands) throws IOException {
        return repl(stdin, stdout, stderr, environment, commands, "");
    }

    public static int repl(InputStream stdin, OutputStream stdout, OutputStream stderr,
                           ReadLineEnvironment environment, Map<String, CliCommand> commands, String prompt)
            throws IOException {
        String line;

        while (true) {
            ReadLine readLine = new ReadLine(stdin, stdout, environment);

            readLine.sendPrompt("Awesome!");
            line = readLine.readLine(prompt, new CommandCompleter(commands.keySet()));

            if (line == null) {
                break;
            }

            line = line.trim();

            if ("".equals(line)) {
                continue;
            }

            // TODO: Make this customizable, have a callback to be called so that the one starting the Repl
            // can determine if we should stop or not.
            if ("exit".equals(line)) {
                readLine.println("Later!");
                readLine.flush();
                break;
            }

            String[] args = line.split(" ");

            CliCommand command = commands.get(args[0]);
            if (command == null) {
                readLine.println("Unknown command '" + line + "'.");
                continue;
            }

            String[] realArgs = new String[args.length - 1];
            System.arraycopy(args, 1, realArgs, 0, realArgs.length);
            try {
                command.run(stdin,
                        new TerminalOutputStream(stdout, environment.ocrnl),
                        new TerminalOutputStream(stderr, environment.ocrnl),
                        environment,
                        realArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // TODO: Consider closing the TerminalOutputStream() here to make sure it's not kept and used in a
            // random thread later on. Or not, it's quite far fetched. - trygve
        }

        return 10;
    }

    private static class CommandCompleter implements Completer {
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

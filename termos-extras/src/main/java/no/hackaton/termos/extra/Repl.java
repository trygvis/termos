package no.hackaton.termos.extra;

import static java.util.Collections.*;
import no.hackaton.termos.*;
import static no.hackaton.termos.CompletionUtil.completeStrings;

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
            line = readLine.readLine(prompt, new CommandCompleter(commands));

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

    public static class CommandCompleter implements Completer {
        private final Map<String, CliCommand> commands;

        CommandCompleter(Map<String, CliCommand> commands) {
            this.commands = commands;
        }

        public List<String> complete(String string, int position) {
            System.out.println("Repl$CommandCompleter.complete");

            int index = string.indexOf(' ');
            System.out.println("index = " + index + ", position=" + position);

            // Figure out if we're completing a command name or arguments to the command

            if (index == -1 || index > position) {
                return completeStrings(commands.keySet(), string);
            } else {
                return completeCommand(string, index);
            }
        }

        private List<String> completeCommand(String string, int index) {
            String commandName = string.substring(0, index).trim();

            System.out.println("commandName = " + commandName);

            CliCommand command = commands.get(commandName);
            if (command == null) {
                return emptyList();
            }

            if (!(command instanceof Completer)) {
                return emptyList();
            }

            Completer completer = (Completer) command;

            return completer.complete(string.substring(index + 1), 0);
        }
    }
}

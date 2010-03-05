package no.hackaton.termos.example.commands;

import no.hackaton.termos.*;
import no.hackaton.termos.extra.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class HelpCommand implements CliCommand {

    private final Map<String, CliCommand> commands;

    public HelpCommand(Map<String, CliCommand> commands) {
        this.commands = commands;
    }

    public String getId() {
        return "help";
    }

    public void run(InputStream stdin, OutputStream stdout, OutputStream stderr, ReadLineEnvironment environment, String[] args) throws IOException {
        PrintWriter writer = new PrintWriter(stdout);

        writer.println("Available commands:");
        for (String s : new TreeSet<String>(commands.keySet())) {
            writer.println(s);
        }
        writer.flush();
    }
}

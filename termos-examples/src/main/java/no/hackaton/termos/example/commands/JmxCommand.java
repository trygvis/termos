package no.hackaton.termos.example.commands;

import no.hackaton.termos.*;
import no.hackaton.termos.example.commands.jmx.*;
import no.hackaton.termos.extra.*;
import static no.hackaton.termos.extra.Repl.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxCommand implements CliCommand {

    CommandCollection commands = new CommandCollection() {{
        addCommand(new HelpCommand(commands));
        addCommand(new JmxOsCommand());
        addCommand(new JmxMemoryCommand());
        addCommand(new JmxRunGcCommand());
    }};

    public String getId() {
        return "jmx";
    }

    public void run(InputStream stdin, OutputStream stdout, OutputStream stderr, ReadLineEnvironment environment, String[] args) throws IOException {
        repl(stdin, stdout, stderr, environment, commands.commands, "JMX> ");
    }
}

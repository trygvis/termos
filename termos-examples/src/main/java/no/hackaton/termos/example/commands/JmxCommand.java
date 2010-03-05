package no.hackaton.termos.example.commands;

import no.hackaton.termos.extra.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxCommand implements CliCommand {
    public String getId() {
        return getClass().getName();
    }

    public void run(LineOutput output, String[] args) throws IOException {
        output.println("JMX");
    }
}

package no.hackaton.termos.example.commands;

import no.hackaton.termos.extra.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class SillyCommand implements CliCommand {

    public final String id;

    public SillyCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void run(LineOutput output, String[] args) throws IOException {
        output.println("Running " + id);
    }
}

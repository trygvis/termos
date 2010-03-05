package no.hackaton.termos.extra;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface CliCommand {
    String getId();

    void run(LineOutput output, String[] args) throws IOException;
}

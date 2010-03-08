package no.hackaton.termos.extra;

import no.hackaton.termos.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface CliCommand {
    String getId();

    void run(InputStream stdin, OutputStream stdout, OutputStream stderr, ReadLineEnvironment environment, String[] args) throws Exception;
}

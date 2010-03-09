package no.hackaton.termos.extra;

import no.hackaton.termos.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class SimplePrintingCliCommand implements CliCommand {
    protected PrintWriter writer;
    protected PrintWriter error;
    protected ReadLineEnvironment environment;
    protected String[] args;

    public abstract void run() throws Exception;

    public final void run(InputStream stdin, OutputStream stdout, OutputStream stderr, ReadLineEnvironment environment, String[] args) throws Exception {
        this.writer = new PrintWriter(stdout);
        this.error = new PrintWriter(stderr);
        this.environment = environment;
        this.args = args;

        try {
            run();
        } finally {
            writer.flush();
            error.flush();
        }
    }
}

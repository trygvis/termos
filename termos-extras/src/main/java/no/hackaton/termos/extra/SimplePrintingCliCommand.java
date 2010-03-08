package no.hackaton.termos.extra;

import no.hackaton.termos.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class SimplePrintingCliCommand implements CliCommand {
    protected ReadLineEnvironment environment;
    protected String[] args;

    public abstract void runWithPrinter(PrintWriter printWriter) throws Exception;

    public final void run(InputStream stdin, OutputStream stdout, OutputStream stderr, ReadLineEnvironment environment, String[] args) throws Exception {
        this.environment = environment;
        this.args = args;
        PrintWriter writer = new PrintWriter(stdout);
        runWithPrinter(writer);
        writer.flush();
    }
}

package no.hackaton.termos.extra;

import no.hackaton.termos.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class SimplePrintingCliCommand implements CliCommand {

    public abstract void runWithPrinter(PrintWriter printWriter);

    public void run(InputStream stdin, OutputStream stdout, OutputStream stderr, ReadLineEnvironment environment, String[] args) throws IOException {
        PrintWriter writer = new PrintWriter(stdout);
        runWithPrinter(writer);
        writer.flush();
    }
}

package no.hackaton.termos.extra;

import static no.hackaton.termos.ReadlineUtil.closeSilently;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class LineOutput implements Closeable {
    private final OutputStream outputStream;

    public LineOutput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void close() throws IOException {
        closeSilently(outputStream);
    }

    public void print(String s) throws IOException {
        outputStream.write(toBytes(s));
        outputStream.flush();
    }

    public void println() throws IOException {
        outputStream.write('\r');
        outputStream.write('\n');
        outputStream.flush();
    }

    public void println(String s) throws IOException {
        outputStream.write(toBytes(s));
        outputStream.write('\r');
        outputStream.write('\n');
        outputStream.flush();
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    private byte[] toBytes(String s) {
        return s.getBytes();
    }
}

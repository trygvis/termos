package no.hackaton.termos;

import static no.hackaton.termos.ReadlineUtil.join;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class LoggingOutputStream extends OutputStream {

    private final OutputStream out;

    public LoggingOutputStream(OutputStream out) {
        this.out = out;
    }

    public void write(int b) throws IOException {
        dump(new byte[]{(byte) (0xff & b)});
        out.write(b);
    }

    public void write(byte[] b) throws IOException {
        out.write(dump(b));
    }

    public void write(byte[] b, int off, int len) throws IOException {
        byte[] bytes = new byte[len - off];
        System.arraycopy(b, off, bytes, 0, len);
        dump(bytes);
        out.write(b, off, len);
    }

    public void flush() throws IOException {
        out.flush();
    }

    public void close() throws IOException {
        out.close();
    }

    private byte[] dump(byte[] bytes) {
        System.out.println(bytes.length + " bytes: " + join(bytes));
        return bytes;
    }
}

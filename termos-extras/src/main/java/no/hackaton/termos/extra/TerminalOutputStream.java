package no.hackaton.termos.extra;

import java.io.*;

/**
 * TODO: Override all methods in OutputStream for efficiency.
 *
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class TerminalOutputStream extends OutputStream {
    private final OutputStream out;
    private final boolean crln;

    public TerminalOutputStream(OutputStream out, boolean crln) {
        this.out = out;
        this.crln = crln;
    }

    @Override
    public void write(int b) throws IOException {
        // TODO: I think this logic is wrong, but it works for me - trygve
        // I wonder if it should use "icrln" instead of "ocrln" as it is now and the "!crln" check
        // should be inverted.
        if(b == '\n' && !crln) {
            out.write('\r');
            out.write('\n');
        }
        else {
            out.write(b);
        }
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}

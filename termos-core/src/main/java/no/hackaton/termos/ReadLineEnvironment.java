package no.hackaton.termos;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ReadLineEnvironment {
    public final String encoding;
    public final Integer erase;
    public final boolean icrnl;
    public final boolean ocrnl;

    private int columns;
    private int lines;

    /**
     * TODO: Not entirely sure if this makes sense to have.
     */
    public static final ReadLineEnvironment defaultEnvironment = new ReadLineEnvironment(null, null,
            false, false, 80, 25);

    public ReadLineEnvironment(String encoding, Integer erase, boolean icrnl, boolean ocrnl, Integer columns, Integer lines) {
        this.encoding = encoding;
        this.erase = erase;
        this.icrnl = icrnl;
        this.ocrnl = ocrnl;
        onWindowChange(columns, lines);
    }

    public int getColumns() {
        return columns;
    }

    public int getLines() {
        return lines;
    }

    public void onWindowChange(Integer newColumns, Integer newLines) {
        this.columns = newColumns != null ? newColumns : 80;
        this.lines = newLines != null ? newLines : 25;
    }
}

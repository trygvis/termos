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

    /**
     * TODO: Not entirely sure if this makes sense to have.
     */
    public static final ReadLineEnvironment defaultEnvironment = new ReadLineEnvironment(null, null,
            false, false);

    public ReadLineEnvironment(String encoding, Integer erase, boolean icrnl, boolean ocrnl) {
        this.encoding = encoding;
        this.erase = erase;
        this.icrnl = icrnl;
        this.ocrnl = ocrnl;
    }
}

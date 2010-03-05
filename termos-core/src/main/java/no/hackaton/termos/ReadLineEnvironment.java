package no.hackaton.termos;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ReadLineEnvironment {
    public final String encoding;
    public final Integer erase;

    public ReadLineEnvironment(String encoding, Integer erase) {
        this.encoding = encoding;
        this.erase = erase;
    }
}

package no.hackaton.termos;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class NoCompleter implements Completer {
    public static final Completer noCompleter = new NoCompleter();

    public List<String> complete(String string) {
        return Collections.singletonList(string);
    }
}

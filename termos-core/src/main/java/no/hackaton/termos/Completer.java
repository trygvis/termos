package no.hackaton.termos;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface Completer {
    List<String> complete(String string);
}

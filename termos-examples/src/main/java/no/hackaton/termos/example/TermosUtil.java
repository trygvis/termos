package no.hackaton.termos.example;

import static java.lang.String.valueOf;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class TermosUtil {
    public static String formatByteCount(long count) {
        int G = 1024 * 1024 * 1024;
        int M = 1024 * 1024;
        int k = 1024;
        if(count > G) {
            return count / G + "G";
        }
        if(count > M) {
            return count / M + "M";
        }
        if(count > k) {
            return count / k + "k";
        }

        return valueOf(count);
    }
}

package no.hackaton.termos;

import java.util.*;

public class CompletionUtil {
    public static List<String> completeStrings(Collection<String> strings, String string) {
        List<String> matches = new ArrayList<String>();

        for (String s : strings) {
            if (s.startsWith(string)) {
                matches.add(s);
            }
        }

        return matches;
    }
}

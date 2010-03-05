package no.hackaton.termos;

import static no.hackaton.termos.ReadLine.*;
import static no.hackaton.termos.ReadLineTest.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

public class CompletionTests {
    @Test
    public void testCompletionWithSingleMatch() throws IOException {
        testMachine(new StringListCompleter("aa")).
            i("a").o("a").
            i(TAB, TAB).o("\raa ").
            check("aa ", 3);
    }

    @Test
    public void testCompletionWithSeveralMatches() throws IOException {
        testMachine(new StringListCompleter("abcX", "abcY")).
            i("ab").o("ab").
            i(TAB, TAB).
            o("\r\n").
            o(" abcX\r\n").
            o(" abcY\r\n").
            o("abc").
            check("abc", 3);
    }

    public static class StringListCompleter implements Completer {
        String[] strings;

        public StringListCompleter(String... strings) {
            this.strings = strings;
        }

        public List<String> complete(String string) {
            List<String> candidates = new ArrayList<String>();

            for (String s : strings) {
                if (s.startsWith(string)) {
                    candidates.add(s);
                }
            }

            return candidates;
        }
    }
}

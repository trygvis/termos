package no.hackaton.termos;

import static no.hackaton.termos.ReadLine.*;
import static no.hackaton.termos.ReadLineTest.*;
import org.junit.*;

import java.io.*;
import java.util.*;

public class CompletionTests {
    @Test
    public void testCompletionWithSingleMatch() throws IOException {
        testMachine(new StringListCompleter("abcX")).
            i("a").o("a").
            i(TAB).o("bcX ").
            check("abcX ", 5);
    }

    @Test
    public void testCompletionWithTwoMatches() throws IOException {
        testMachine(new StringListCompleter("abcX", "abcY")).
            i("a").o("a").
            i(TAB).o("bc").
            check("abc", 3);
    }

    @Test
    public void testCompletionWithSeveralMatches() throws IOException {
        testMachine(new StringListCompleter("abcX", "abcY")).
            i("abc").o("abc").
            i(TAB, TAB).
            o("\r\n").
            o("abcX\r\n").
            o("abcY\r\n").
            o("abc").
            check("abc", 3);
    }

    public static class StringListCompleter implements Completer {
        Collection<String> strings;

        public StringListCompleter(String... strings) {
            this.strings = Arrays.asList(strings);
        }

        public List<String> complete(String string, int position) {
            return CompletionUtil.completeStrings(strings, string);
        }
    }
}

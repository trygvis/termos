package no.hackaton.termos;

import static no.hackaton.termos.ReadLine.TAB;
import static no.hackaton.termos.ReadLineTest.testMachine;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompletionTests {
    @Test
    public void testBasic() throws IOException {
        testMachine(new StringListCompleter("aa")).
            i("a").o("a").
            i(TAB).o("\raa").
            check("aa", 2);
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

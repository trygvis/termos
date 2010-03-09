package no.hackaton.termos.extra;

import static java.util.Arrays.asList;
import no.hackaton.termos.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.*;

public class ReplTest {
    CliCommand command = new MyCliCommand();

    @Test
    public void testCommandCompleter() {
        Map<String, CliCommand> commands = new TreeMap<String, CliCommand>() {{
            put("aaa", command);
            put("aab", command);
        }};

        Repl.CommandCompleter completer = new Repl.CommandCompleter(commands);

        assertEquals(asList("xx", "xy"), completer.complete("aaa x", 4));
    }

    private static class MyCliCommand implements CliCommand, Completer {
        public String getId() {
            return "";
        }

        public void run(InputStream stdin, OutputStream stdout, OutputStream stderr, ReadLineEnvironment environment, String[] args) throws Exception {
        }

        public List<String> complete(String currentLine, int position) {
            assertEquals("x", currentLine);
            return asList("xx", "xy");
        }
    }
}

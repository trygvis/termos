package no.hackaton.termos;

import static no.hackaton.termos.NoCompleter.*;
import static no.hackaton.termos.ReadLine.*;
import static no.hackaton.termos.ReadlineUtil.*;
import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ReadLineTest {

    @Test
    public void testBasic() throws Exception {
        testMachine().
                i("Hello").o("Hello").
                check("Hello", 5);
    }

    @Test
    public void testHelloThenBackspace() throws Exception {
        testMachine().
                i("Hellp").o("Hellp").
                i((byte) 0x7f).o(BS).o(clearEol).
                i("o").o("o").
                check("Hello", 5);
    }

    @Test
    public void testAThenBackspace() throws Exception {
        testMachine().
                i("A").o("A").
                i((byte) 0x7f).o(BS).o(clearEol).
                check("", 0);
    }

    @Test
    public void testBackspaceOnEmptyLine() {
        testMachine().
                i(BS).
                check("", 0);
    }

    @Test
    public void testTwoLeftOnHello() throws Exception {
        testMachine().
                i("Hello").i(cursorLf).i(cursorLf).
                o("Hello").o(cursorLf).o(cursorLf).
                check("Hello", 3);
    }

    @Test
    public void testClearScreen() throws Exception {
        testMachine().
                i("Hello").i(cursorLf).i(cursorLf).
                o("Hello").o(cursorLf).o(cursorLf).
                check("Hello", 3).
                i(FF).o(cursorHome).o(clearScreenEd2).o("Hello").
                check("Hello", 3);
    }

    @Test
    public void testBackwardWord() throws Exception {
        testMachine().
                i("Hello World!").o("Hello World!").
                i(ESC, (byte)'b').o(ReadlineUtil.repeat(cursorLf, 7)).
                check("Hello World!", 7);
    }

    @Test
    public void testFindStartOfWord() {
        assertEquals(0, findStartOfWord(ReadlineUtil.asList(""), 0));
    }

    @Test
    public void testFindEndOfWord() {
        assertEquals(0, findEndOfWord(ReadlineUtil.asList(""), 0));
        assertEquals(3, findEndOfWord(ReadlineUtil.asList("foo"), 0));
        assertEquals(3, findEndOfWord(ReadlineUtil.asList("foo"), 2));
        assertEquals(3, findEndOfWord(ReadlineUtil.asList("foo bar"), 0));
        assertEquals(3, findEndOfWord(ReadlineUtil.asList("foo bar"), 2));
        assertEquals(7, findEndOfWord(ReadlineUtil.asList("foo bar"), 3));
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    @Test
    public void testFindLongestMatch() {
        assertEquals("ab", findLongestMatch("a", Arrays.asList("abc", "abd")));
        assertEquals("a", findLongestMatch("a", Arrays.asList("a", "abc")));
        assertEquals("a", findLongestMatch("a", Arrays.asList("a", "b")));
        assertEquals("abc", findLongestMatch("abc", Arrays.asList("abcX", "abcY")));
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    public static ReadLineMachineTester testMachine() {
        return testMachine("");
    }

    public static ReadLineMachineTester testMachine(String prompt) {
        return new ReadLineMachineTester(prompt);
    }

    public static ReadLineMachineTester testMachine(Completer completer) {
        return new ReadLineMachineTester("", completer);
    }

    static class ReadLineMachineTester {
        private ByteArrayOutputStream inputCollector = new ByteArrayOutputStream(1024);
        private ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream(1024);
        private ReadLineEnvironment environment = new ReadLineEnvironment(null, null);
        private String prompt;
        private Completer completer;

        public ReadLineMachineTester(String prompt) {
            this.prompt = prompt;

            completer = noCompleter;
        }

        public ReadLineMachineTester(String prompt, Completer completer) {
            this.prompt = prompt;
            this.completer = completer;
        }

        public ReadLineMachineTester i(byte b) {
            inputCollector.write(b);
            return this;
        }

        public ReadLineMachineTester i(byte... bytes) {
            for (byte b : bytes) {
                i(b);
            }
            return this;
        }

        public ReadLineMachineTester i(String s) {
            return i(s.getBytes());
        }

        public ReadLineMachineTester o(byte b) {
            expectedOutput.write(b);
            return this;
        }

        public ReadLineMachineTester o(byte... bytes) throws IOException {
            expectedOutput.write(bytes);
            return this;
        }

        public ReadLineMachineTester o(String s) throws IOException {
            return o(s.getBytes());
        }

        public ReadLineMachineTester check(String expectedString, int position) {
            ByteArrayOutputStream outputCollector = new ByteArrayOutputStream();
            ReadLine readLine = new ReadLine(new ByteArrayInputStream(inputCollector.toByteArray()), outputCollector, environment);
            String actualString = readLine.readLine(prompt, completer);

            Assert.assertEquals("Read line", expectedString, actualString);

            assertEquals("Position", position, readLine.getPosition());

//            Assert.assertEquals("Bytes written", join(expectedOutput.toByteArray()), join(outputCollector.toByteArray()));
            Assert.assertEquals("Bytes written", joinPretty(expectedOutput.toByteArray()), joinPretty(outputCollector.toByteArray()));

            return this;
        }
    }
}

package no.hackaton.termos;

import static java.lang.Character.*;
import static java.lang.Character.valueOf;
import static java.lang.Integer.*;
import static no.hackaton.termos.NoCompleter.*;
import static no.hackaton.termos.ReadlineUtil.closeSilently;
import org.apache.sshd.common.*;
import org.apache.sshd.server.*;

import java.io.*;
import java.util.*;

/**
 * TODO: Look into CharsetDecoder to decode the data on the fly and echo every successfully read byte back to the client.
 * <p/>
 * TODO: Support history
 * TODO: Support tab-completion
 *
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@SuppressWarnings({"OctalInteger"})
public class ReadLine implements Closeable {
    public static final byte ETX = 003; // Ctrl-c
    public static final byte BS = 010;
    public static final byte TAB = 011;
    public static final byte FF = 014;
    public static final byte ESC = 033;
    public static final byte SPACE = 040;
    public static final byte BEL = 0007;

    public static final byte[] clearEol = {ESC, '[', 'K'};
    public static final byte[] cursorHome = {ESC, '[', 'H'};
    public static final byte[] cursorUp = {ESC, '[', 'A'};
    public static final byte[] cursorDn = {ESC, '[', 'B'};
    public static final byte[] cursorRt = {ESC, '[', 'C'};
    public static final byte[] cursorLf = {ESC, '[', 'D'};
    public static final byte[] clearScreenEd2 = {ESC, '[', '2', 'J'};

    private final InputStream inputStream;
    private final OutputStream outputStream;

    List<Character> chars = new ArrayList<Character>();
    int position = 0;
    private byte erase;

    public ReadLine(InputStream inputStream, OutputStream outputStream, Environment environment) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        Integer erase = environment.getPtyModes().get(PtyMode.VERASE);
        this.erase = erase == null ? 0x7f : erase.byteValue();
    }

    public int getPosition() {
        return position;
    }

    public String readLine() {
        return readLine("", noCompleter);
    }

    public String readLine(String prompt) {
        return readLine(prompt, noCompleter);
    }

    public String readLine(String prompt, Completer completer) {
        try {
            return doRead(prompt, completer);
        } catch (IOException e) {
            return null;
        }
    }

    private String doRead(String prompt, Completer completer) throws IOException {
        int b;
        int tabCount = 0;

        print(prompt);
        outputStream.flush();

        // TODO: This should read all available bytes

        do {
            b = tryNext();

            if (b == -1) {
                break;
            }

            if (0x20 <= b && b < 0x7f) {
                outputStream.write(b);
                outputStream.flush();

                if (isEndOfLine()) {
                    chars.add((char) b);
                    position++;
                } else {
                    chars.set(position++, (char) (0xff & b));
                }
            }

            if (b == '\r') {
                // This seems to be correct, at least if ONLCR=1
                outputStream.write('\r');
                outputStream.write('\n');
                outputStream.flush();
                break;
            }

            if (b == erase) {
                if (position > 0) {
                    position--;
                    chars.remove(position);
                    outputStream.write(BS);
                    outputStream.write(clearEol);
                }
            } else if (b == FF) {
                outputStream.write(cursorHome);
                outputStream.write(clearScreenEd2);
                print(prompt);
                for (Character c : chars) {
                    // TODO: Encode
                    outputStream.write((byte) c.charValue());
                }
            } else if (b == ESC) {
                b = requireNext();

                if (b == '[') {
                    b = requireNext();
                    if (b == 'A' || b == 'B') { // Up || down
                        // ignore
                    } else if (b == 'C') { // Right
                        if (isEndOfLine()) {
                            position++;
                            outputStream.write(cursorRt);
                        }
                    } else if (b == 'D') { // Left
                        if (position > 0) {
                            position--;
                            outputStream.write(cursorLf);
                        }
                    }
                } else if (b == 'b') { // backward-word
                    int i = findStartOfWord(chars, position);

                    int count = position - i;
                    for (int x = 0; x < count; x++) {
                        outputStream.write(cursorLf);
                    }
                    position = position - i;
                } else if (b == 'f') { // forward-word
                    int i = findEndOfWord(chars, position);

                    int count = position - i;
                    for (int x = 0; x < count; x++) {
                        outputStream.write(cursorLf);
                    }
                    position = position - i;
                }
            } else if (b == TAB && tabCount++ == 1) {
                tabCount = 0;

                String currentLine = charsToString();

                List<String> options = completer.complete(currentLine);

                outputStream.write('\r');
                if (options.size() == 1) {
                    String match = options.get(0);
                    int length = match.length();
                    chars = new ArrayList<Character>(length);
                    for (int i = 0; i < length; i++) {
                        chars.add(match.charAt(i));
                    }
                    // Add an extra space after the match to be ready to write arguments to the command
                    chars.add(' ');
                } else {
                    outputStream.write('\n');

                    println("Got " + options.size() + " matches:");
                    for (String option : options) {
                        println(option);
                    }
                }
                print(prompt + charsToString());
            } else if (b == ETX) {
                tabCount = 0;
                println("");
                chars = new ArrayList<Character>();
                print(prompt);
            }
            outputStream.flush();
        } while (true);

        // TODO: Decode
        return charsToString();
    }

    private String charsToString() {
        StringBuffer buffer = new StringBuffer(chars.size());
        for (Character c : chars) {
            buffer.append(valueOf(c));
        }
        return buffer.toString();
    }

    private boolean isEndOfLine() {
        return position == chars.size();
    }

    private int tryNext() throws IOException {
        int b = inputStream.read();
        if (b == -1) {
            return -1;
        }
        System.out.println("b = 0x" + (b < 16 ? "0" : "") + toHexString(b));
        return b;
    }

    private int requireNext() throws IOException {
        int b = inputStream.read();
        if (b == -1) {
            throw new IOException("Unexpected EOF.");
        }
        System.out.println("b = 0x" + (b < 16 ? "0" : "") + toHexString(b));
        return b;
    }

    public void close() throws IOException {
        closeSilently(inputStream);
    }

    public static int findStartOfWord(List<Character> chars, int position) {
//        int i = position;
        return 0;
    }

    public static int findEndOfWord(List<Character> chars, int position) {
        // Find first word
        int i = findStartOfWord(chars, position);
        int end = chars.size();

        for (; i < end; i++) {
            Character c = chars.get(i);
            if (!isLetter(c) && !isDigit(c)) {
                return i;
            }
        }

        return i;
    }

    // -----------------------------------------------------------------------
    // Output
    // -----------------------------------------------------------------------

    private byte[] toBytes(String s) {
        // TODO: Encode
        return s.getBytes();
    }

    public void print(String s) throws IOException {
        outputStream.write(toBytes(s));
    }

    public void println(String s) throws IOException {
        outputStream.write(toBytes(s));
        outputStream.write('\r');
        outputStream.write('\n');
    }

    /**
     * Sets the prompt of the terminal.
     */
    public void sendPrompt(String s) throws IOException {
        outputStream.write(ESC);
        outputStream.write(toBytes("]0;")); // TODO: This should be a byte array
        outputStream.write(toBytes(s));
        outputStream.write(BEL);
        outputStream.flush();
    }
}

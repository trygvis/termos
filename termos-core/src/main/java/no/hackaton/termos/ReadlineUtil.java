package no.hackaton.termos;

import static java.lang.Integer.*;

import java.io.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class ReadlineUtil {
    public static interface F<A, B> {
        B f(A a);
    }

    public static class PrettyF implements F<Byte, String> {
        public String f(Byte b) {
            if (b < 0x20) {
                return toHexString(b);
            } else if (b <= 0x7e) {
                return "'" + Character.toString((char) b.byteValue()) + "'";
            } else {
                return "0x" + toHexString(b);
            }
        }
    }

    public static String joinPretty(byte[] bytes) {
        F<Byte, String> f = new PrettyF();

        StringBuffer buffer = new StringBuffer(bytes.length * 2);

        if (bytes.length == 0) {
            return "";
        }

        buffer.append(f.f(bytes[0]));

        int bytesLength = bytes.length;

        for (int i = 1; i < bytesLength; i++) {
            byte b = bytes[i];
            buffer.append(", ").append(f.f(b));
        }

        return buffer.toString();
    }

    public static String join(byte[] bytes) {
        if (bytes.length == 0) {
            return "";
        }

        StringBuffer buffer = new StringBuffer(bytes.length * 3);

        int b = bytes[0];
        if (b <= 16) {
            buffer.append('0');
        }
        buffer.append(toHexString(b));

        for (int i = 1, bytesLength = bytes.length; i < bytesLength; i++) {
            b = bytes[i];

            buffer.append(' ');
            if (b <= 16) {
                buffer.append('0');
            }
            buffer.append(toHexString(b));
        }

        return buffer.toString();
    }

    public static byte[] repeat(byte[] array, int n) {
        int length = array.length;
        byte[] out = new byte[length * n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(array, 0, out, i * length, length);
        }

        return out;
    }

    public static List<Character> asList(String s) {
        ArrayList<Character> list = new ArrayList<Character>(s.length());
        for (int i = 0; i < s.length(); i++) {
            list.add(s.charAt(i));
        }
        return list;
    }

    /**
     * Returns null.
     */
    public static <T extends Closeable> T closeSilently(T closeable) {
        if (closeable == null) {
            return null;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            // ignore
        }

        return null;
    }
}

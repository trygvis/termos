package no.hackaton.termos.extra.formatting;

import static no.hackaton.termos.extra.formatting.PageUtil.*;
import static org.junit.Assert.*;
import org.junit.*;

import java.io.*;

public class PageUtilTest {
    @Test
    public void testFindLongest() {
        assertEquals(3, findLongest(new String[]{"1", "12", "123"}));
    }

    @Test
    public void testShowList() {
        String[] columnTitles = new String[]{"aaa", "bbbbb"};
        String[] data = new String[]{"woooo", "weeee"};

        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        showList(columnTitles, data, writer);

        assertEquals(toString(new String[]{
            "aaa   | woooo",
            "bbbbb | weeee"
        }), buffer.toString());
    }

    @Test
    public void testShowPage() {
        String[] columnTitles = new String[]{"aaa", "bbbbb"};
        String[][] data = new String[][]{
            new String[]{"woooo", "weeee"},
            new String[]{"fooo", "jooo"},
        };

        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        showPage(columnTitles, data, writer);

        assertEquals(toString(new String[]{
            "aaa   | bbbbb",
            "woooo | weeee",
            "fooo  | jooo ",
        }), buffer.toString());
    }

    private String toString(String[] strings) {
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        for (String string : strings) {
            writer.println(string);
        }
        writer.flush();
        return buffer.toString();
    }
}

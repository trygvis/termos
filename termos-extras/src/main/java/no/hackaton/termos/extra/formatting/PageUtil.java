package no.hackaton.termos.extra.formatting;

import static java.lang.Math.*;

import java.io.*;

/**
 * Utility for showing "pages" of data
 */
public class PageUtil {

    public static int findLongest(String[] strings) {
        int longest = 0;
        for (String string : strings) {
            if(string != null) {
                longest = max(longest, string.length());
            }
        }
        return longest;
    }

    public static void showList(String[] columnTitles, String[] data, PrintWriter writer) {
        int width = findLongest(columnTitles);

        if(columnTitles.length != data.length) {
            throw new RuntimeException("columnTitles.length != data.length");
        }

        for (int i = 0; i < columnTitles.length; i++) {
            String columnTitle = columnTitles[i];
            writer.print(columnTitle);
            for(int j = columnTitle.length(); j < width; j++) {
                writer.print(" ");
            }
            writer.print(" | ");
            writer.print(data[i]);
            writer.println();
        }
    }

    public static void showPage(String[] columnTitles, String[][] data, PrintWriter writer) {
        int[] widths = new int[columnTitles.length];

        for (int i = 0; i < widths.length; i++) {
            widths[i] += columnTitles[i].length();
        }

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                widths[j] = max(widths[j], data[i][j].length());
            }
        }

        for (int i = 0; i < columnTitles.length; i++) {
            int width = widths[i];
            String columnTitle = columnTitles[i];

            if (i > 0) {
                writer.print(" | ");
            }

            writer.print(columnTitle);

            for (int j = columnTitle.length(); j < width; j++) {
                writer.print(" ");
            }
        }
        writer.println();

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                String s = data[i][j];
                int width = widths[j];

                if (j > 0) {
                    writer.print(" | ");
                }

                writer.print(s);

                for (int k = s.length(); k < width; k++) {
                    writer.print(" ");
                }
            }
            writer.println();
        }
    }
}

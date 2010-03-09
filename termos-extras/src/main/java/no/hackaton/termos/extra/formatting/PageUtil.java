package no.hackaton.termos.extra.formatting;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Math.*;
import static java.lang.String.valueOf;
import no.hackaton.termos.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Utility for showing "pages" of data
 */
public class PageUtil {

    public static int findLongest(String[] strings) {
        int longest = 0;
        for (String string : strings) {
            if (string != null) {
                longest = max(longest, string.length());
            }
        }
        return longest;
    }

    public static void showList(String[] columnTitles, String[] data, PrintWriter writer) {
        int width = findLongest(columnTitles);

        if (columnTitles.length != data.length) {
            throw new RuntimeException("columnTitles.length != data.length");
        }

        for (int i = 0; i < columnTitles.length; i++) {
            String columnTitle = columnTitles[i];
            writer.print(columnTitle);
            for (int j = columnTitle.length(); j < width; j++) {
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

        writeColumns(writer, columnTitles, widths);

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

    public static interface FieldFormatter {
        String format(Object o);
    }

    public static final Map<String, FieldFormatter> formatters = Collections.unmodifiableMap(new HashMap<String, FieldFormatter>(){{
        put("short", new ShortFieldFormatter());
        put("int", new IntegerFieldFormatter());
        put("long", new LongFieldFormatter());
    }});

    public static void showRunningPage(ReadLineEnvironment environment, PrintWriter output, int interval, int maxCount, String[] columnTitles, FieldFormatter[] formatters, Callable<Object[]> dataCallback) {
        int count = 0;
        int dataLines = MAX_VALUE;

        if (columnTitles.length != formatters.length) {
            throw new RuntimeException("columnTitles.length != formatters.length");
        }

        int[] widths = new int[columnTitles.length];

        for (int i = 0; i < widths.length; i++) {
            widths[i] = columnTitles[i].length();
        }

        while (true) {
            if (dataLines >= environment.getLines() - 2) {
                writeColumns(output, columnTitles, widths);
                dataLines = 0;
            }
            dataLines++;

            try {
                Object[] objects = dataCallback.call();

                String[] strings = new String[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    strings[i] = formatters[i].format(objects[i]);
                }
                writeColumns(output, strings, widths);
            } catch (Exception e) {
                e.printStackTrace();
            }

            output.flush();

            if (maxCount > 0 && count == maxCount) {
                break;
            }

            count++;

            // TODO: read for a time-limited interval
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // -----------------------------------------------------------------------
    //
    // -----------------------------------------------------------------------

    private static void writeColumns(PrintWriter writer, String[] data, int[] widths) {
        for (int i = 0; i < data.length; i++) {
            int width = widths[i];
            String columnTitle = data[i];

            if (i > 0) {
                writer.print(" | ");
            }

            writer.print(columnTitle);

            for (int j = columnTitle.length(); j < width; j++) {
                writer.print(" ");
            }
        }
        writer.println();
    }

    private static final long k = 1024;
    private static final long M = 1024 * 1024;
    private static final long G = 1024 * 1024 * 1024;
    private static final long T = 1024L * 1024L * 1024L * 1024L;
    private static final long P = 1024L * 1024L * 1024L * 1024L * 1024L;
    private static final long E = 1024L * 1024L * 1024L * 1024L * 1024L * 1024L;

    public static String formatIso(long l) {
        return (l < 0 ? "-" : "") + formatLong(l);
    }

    private static String formatLong(long l) {
        if(l > E) {
            return (l / E) + "E";
        }
        if(l > P) {
            return (l / P) + "P";
        }
        if(l > T) {
            return (l / T) + "T";
        }
        if(l > G) {
            return (l / G) + "G";
        }
        if(l > M) {
            return (l / M) + "M";
        }
        if(l > k) {
            return (l / k) + "k";
        }
        return valueOf(l);
    }

    private static class ShortFieldFormatter implements FieldFormatter {
        public String format(Object o) {
            return formatIso((Short)o);
        }
    }

    private static class IntegerFieldFormatter implements FieldFormatter {
        public String format(Object o) {
            return formatIso((Integer)o);
        }
    }

    private static class LongFieldFormatter implements FieldFormatter {
        public String format(Object o) {
            return formatIso((Long)o);
        }
    }
}

package no.hackaton.termos.example.commands.jmx;

import static no.hackaton.termos.extra.formatting.PageUtil.*;
import no.hackaton.termos.extra.*;

import javax.management.*;
import java.io.*;
import java.lang.management.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxDescribeCommand extends SimplePrintingCliCommand {
    private final MBeanServer mBeanServer;
    private String[] columnTitles = new String[]{
        "Name",
        "Description",
        "Type",
        "Readable",
        "Writable",
    };

    public JmxDescribeCommand() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    public String getId() {
        return "describe";
    }

    @Override
    public void runWithPrinter(PrintWriter writer) throws Exception {
        MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(new ObjectName(args[0]));

        MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();

        writer.println("Showing bean " + args[0] + ":");
        writer.println(" Description: " + mBeanInfo.getDescription());
        writer.println(" Number of attributes: " + attributes.length);

        String[][] data = new String[attributes.length][columnTitles.length];

        for (int i = 0; i < attributes.length; i++) {
            MBeanAttributeInfo attribute = attributes[i];
            data[i] = new String[columnTitles.length];
            data[i][0] = attribute.getName();
            data[i][1] = attribute.getDescription() != null && !attribute.getDescription().equals(attribute.getName()) ? attribute.getDescription() : "";
            data[i][2] = attribute.getType();
            data[i][3] = attribute.isReadable() ? "Y" : "N";
            data[i][4] = attribute.isWritable() ? "Y" : "N";
        }

        showPage(columnTitles, data, writer);
    }
}

package no.hackaton.termos.example.commands.jmx;

import static java.lang.String.valueOf;
import static javax.management.ObjectName.*;
import static no.hackaton.termos.extra.formatting.PageUtil.*;
import no.hackaton.termos.extra.*;

import javax.management.*;
import java.io.*;
import java.lang.management.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxShowCommand extends SimplePrintingCliCommand {
    private final MBeanServer mBeanServer;

    public JmxShowCommand() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    public String getId() {
        return "show";
    }

    @Override
    public void runWithPrinter(PrintWriter writer) throws Exception {
        ObjectName objectName = getInstance(args[0]);
        MBeanInfo mBeanInfo = mBeanServer.getMBeanInfo(objectName);

        MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
        String[] columnTitles = new String[attributes.length + 1];

        String[] data = new String[attributes.length + 1];

        columnTitles[0] = "Attribute";
        data[0] = "Value";

        for (int i = 0; i < attributes.length; i++) {
            MBeanAttributeInfo attribute = attributes[i];
            columnTitles[i + 1] = attribute.getName();
            data[i + 1] = valueOf(mBeanServer.getAttribute(objectName, attribute.getName()));
        }

        showList(columnTitles, data, writer);
    }
}

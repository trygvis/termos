package no.hackaton.termos.example.commands.jmx;

import static java.lang.String.*;
import static javax.management.ObjectName.*;
import no.hackaton.termos.*;
import no.hackaton.termos.extra.*;
import static no.hackaton.termos.extra.formatting.PageUtil.*;

import javax.management.*;
import java.io.*;
import java.lang.management.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxShowCommand extends SimplePrintingCliCommand implements Completer {
    private final MBeanServer mBeanServer;
    private final JmxUtil jmxUtil;

    public JmxShowCommand() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
        jmxUtil = new JmxUtil(mBeanServer);
    }

    public String getId() {
        return "show";
    }

    @Override
    public void runWithPrinter(PrintWriter writer) throws Exception {
        ObjectName objectName = getInstance(args[0]);
        MBeanInfo mBeanInfo;
        try {
            mBeanInfo = mBeanServer.getMBeanInfo(objectName);
        } catch (InstanceNotFoundException e) {
            writer.println("No such instance '" + args[0] + "'.");
            return;
        }

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

    public List<String> complete(String string, int position) {
        return jmxUtil.complete(string, position);
    }
}

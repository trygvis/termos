package no.hackaton.termos.example.commands.jmx;

import static javax.management.ObjectName.*;
import no.hackaton.termos.*;
import no.hackaton.termos.extra.*;
import no.hackaton.termos.extra.formatting.*;
import no.hackaton.termos.extra.formatting.PageUtil.*;
import static no.hackaton.termos.extra.formatting.PageUtil.*;

import javax.management.*;
import java.lang.management.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxFollowCommand extends SimplePrintingCliCommand implements Completer {
    private final MBeanServer mBeanServer;
    private final JmxUtil jmxUtil;

    public JmxFollowCommand() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
        jmxUtil = new JmxUtil(mBeanServer);
    }

    public String getId() {
        return "follow";
    }

    @Override
    public void run() throws Exception {
        if (args.length != 1) {
            error.println("Usage: follow <object name>");
            return;
        }

        final ObjectName objectName = getInstance(args[0]);
        final MBeanInfo mBeanInfo;
        try {
            mBeanInfo = mBeanServer.getMBeanInfo(objectName);
        } catch (InstanceNotFoundException e) {
            writer.println("No such instance '" + args[0] + "'.");
            return;
        }

        final MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();

        final List<String> columnTitleList = new ArrayList<String>(attributes.length);
        List<FieldFormatter> formatters = new ArrayList<FieldFormatter>(attributes.length);

        for (int i = 0; i < attributes.length; i++) {
            MBeanAttributeInfo attribute = attributes[i];

            FieldFormatter formatter = PageUtil.formatters.get(attribute.getType());
            if (formatter == null) {
                continue;
            }

            columnTitleList.add(attribute.getName());
            formatters.add(formatter);
        }

        final String[] columnTitles = columnTitleList.toArray(new String[columnTitleList.size()]);

        int interval = 500;
        int maxCount = 20;
        showRunningPage(environment, writer, interval, maxCount,
            columnTitles,
            formatters.toArray(new FieldFormatter[formatters.size()]),
            new Callable<Object[]>() {
                Object[] data = new Object[columnTitles.length];

                public Object[] call() throws Exception {
                    for (int i = 0; i < columnTitles.length; i++) {
                        Object o = mBeanServer.getAttribute(objectName, columnTitles[i]);

//                        if (o.getClass().isAssignableFrom(long.class)) {
//                            o = new Long(long.class.cast(o));
//                        }

                        data[i] = o;
                    }

                    return data;
                }
            });
    }

    public List<String> complete(String string, int position) {
        return jmxUtil.complete(string, position);
    }
}

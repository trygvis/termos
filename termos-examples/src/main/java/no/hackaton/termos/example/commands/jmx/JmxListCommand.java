package no.hackaton.termos.example.commands.jmx;

import no.hackaton.termos.extra.*;

import javax.management.*;
import java.io.*;
import java.lang.management.*;
import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxListCommand extends SimplePrintingCliCommand {
    private final MBeanServer mBeanServer;

    public JmxListCommand() {
        this.mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    public String getId() {
        return "list";
    }

    @Override
    public void runWithPrinter(PrintWriter writer) {
        Set<ObjectName> names = mBeanServer.queryNames(null, null);
        writer.println("Showing " + names.size() + " JMX objects:");
        for (ObjectName name : names) {
            writer.println(" " + name.getCanonicalName());
        }
    }
}

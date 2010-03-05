package no.hackaton.termos.example.commands.jmx;

import no.hackaton.termos.*;
import no.hackaton.termos.extra.*;

import java.io.*;
import java.lang.management.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxOsCommand extends SimplePrintingCliCommand {
    private final OperatingSystemMXBean os;

    public JmxOsCommand() {
        this.os = ManagementFactory.getOperatingSystemMXBean();
    }

    public String getId() {
        return "os";
    }

    @Override
    public void runWithPrinter(PrintWriter writer) {
        writer.println("Operating system");
        writer.println(" Name:         " + os.getName());
        writer.println(" Arch:         " + os.getArch());
        writer.println(" Version:      " + os.getVersion());
        writer.println(" Processors:   " + os.getAvailableProcessors());
        writer.println(" Load average: " + os.getSystemLoadAverage());
    }
}

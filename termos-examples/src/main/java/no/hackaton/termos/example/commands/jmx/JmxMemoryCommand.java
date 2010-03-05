package no.hackaton.termos.example.commands.jmx;

import static no.hackaton.termos.example.TermosUtil.formatByteCount;
import no.hackaton.termos.extra.*;

import java.io.*;
import java.lang.management.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxMemoryCommand extends SimplePrintingCliCommand {
    private final MemoryMXBean memory;

    public JmxMemoryCommand() {
        this.memory = ManagementFactory.getMemoryMXBean();
    }

    public String getId() {
        return "memory";
    }

    @Override
    public void runWithPrinter(PrintWriter writer) {
        writer.println("Memory");
        writer.println(" Heap");
        print(writer, memory.getHeapMemoryUsage());
        writer.println(" Non-Heap");
        print(writer, memory.getNonHeapMemoryUsage());
        writer.println(" Objects pending finalization: " + memory.getObjectPendingFinalizationCount());
    }

    private void print(PrintWriter writer, MemoryUsage memoryUsage) {
        writer.println("  Init:      " + formatByteCount(memoryUsage.getInit()));
        writer.println("  Max:       " + formatByteCount(memoryUsage.getMax()));
        writer.println("  Used:      " + formatByteCount(memoryUsage.getUsed()));
        writer.println("  Committed: " + formatByteCount(memoryUsage.getCommitted()));
    }
}

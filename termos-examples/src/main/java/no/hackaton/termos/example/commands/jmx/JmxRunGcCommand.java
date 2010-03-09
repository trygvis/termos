package no.hackaton.termos.example.commands.jmx;

import static java.lang.Runtime.*;
import static no.hackaton.termos.example.TermosUtil.*;
import no.hackaton.termos.extra.*;

import java.lang.management.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class JmxRunGcCommand extends SimplePrintingCliCommand {
    private final Runtime runtime = getRuntime();
    private final MemoryMXBean memory;

    public JmxRunGcCommand() {
        this.memory = ManagementFactory.getMemoryMXBean();
    }

    public String getId() {
        return "run-gc";
    }

    @Override
    public void run() {
        writer.print("Running garbage collection...");

        long before = runtime.freeMemory();
        long start = System.currentTimeMillis();
        memory.gc();
        long end = System.currentTimeMillis();
        long after = runtime.freeMemory();

        writer.println("done!");
        writer.println("Freed " + formatByteCount(after - before) + " in " + (end - start) + "ms.");
    }
}

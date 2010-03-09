package no.hackaton.termos.example.commands.jmx;

import javax.management.*;
import java.util.*;

public class JmxUtil {

    private final MBeanServer mBeanServer;

    public JmxUtil(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
    }

    public List<String> complete(String s, int position) {
        return completeJmxObjectName(mBeanServer, s, position);
    }

    public static List<String> completeJmxObjectName(MBeanServer mBeanServer, String prefix, int position) {
        System.out.println("JmxUtil.completeJmxObjectName: prefix = '" + prefix + "'");

        Set<String> candidates = new TreeSet<String>();
        Set<ObjectName> names = mBeanServer.queryNames(null, null);
        for (ObjectName name : names) {
            String canonicalName = name.getCanonicalName();
            if(canonicalName.startsWith(prefix)) {
                candidates.add(canonicalName);
            }
        }

        return new ArrayList<String>(candidates);
    }
}

package no.hackaton.termos;

import org.apache.sshd.common.*;
import org.apache.sshd.server.*;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
class SshEnvironment implements Environment {
    public Map<String, String> getEnv() {
        throw new RuntimeException("Not implemented");
    }

    public Map<PtyMode, Integer> getPtyModes() {
        return Collections.singletonMap(PtyMode.VERASE, 0x7f);
    }

    public void addSignalListener(SignalListener listener, Signal... signal) {
        throw new RuntimeException("Not implemented");
    }

    public void addSignalListener(SignalListener listener, EnumSet<Signal> signals) {
        throw new RuntimeException("Not implemented");
    }

    public void addSignalListener(SignalListener listener) {
        throw new RuntimeException("Not implemented");
    }

    public void removeSignalListener(SignalListener listener) {
        throw new RuntimeException("Not implemented");
    }
}

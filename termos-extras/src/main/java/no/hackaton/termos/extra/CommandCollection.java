package no.hackaton.termos.extra;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class CommandCollection {
    private final Map<String, CliCommand> _commands = new HashMap<String, CliCommand>();
    public final Map<String, CliCommand> commands = Collections.unmodifiableMap(_commands);

    public CommandCollection addCommand(CliCommand command) {
        _commands.put(command.getId(), command);
        return this;
    }

    public CommandCollection addCommand(CliCommand command, String ... aliases) {
        _commands.put(command.getId(), command);
        for (String alias : aliases) {
            _commands.put(alias, command);
        }
        return this;
    }
}

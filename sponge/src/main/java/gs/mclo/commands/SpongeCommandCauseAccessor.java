package gs.mclo.commands;

import gs.mclo.command.AdventureCommandSourceAccessor;
import gs.mclo.MclogsSpongePlugin;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.CommandCause;

import java.nio.file.Path;
import java.util.Collection;

public class SpongeCommandCauseAccessor extends AdventureCommandSourceAccessor {
    protected final MclogsSpongePlugin plugin;
    protected final CommandCause commandCause;

    public SpongeCommandCauseAccessor(MclogsSpongePlugin plugin,
                                      CommandCause commandCause) {
        this.plugin = plugin;
        this.commandCause = commandCause;
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return commandCause.hasPermission(permission.node());
    }

    @Override
    public Path getRootDirectory() {
        return Path.of(".");
    }

    @Override
    public Collection<LogDirectory> getLogDirectories() {
        return LogDirectory.getVanillaLogDirectories(getRootDirectory());
    }

    @Override
    protected Audience getAudience() {
        return this.commandCause.audience();
    }
}

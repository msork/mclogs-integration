package gs.mclo.commands;

import gs.mclo.MclogsSpongePlugin;
import gs.mclo.components.AdventureComponent;
import gs.mclo.commands.SpongeCommandCauseAccessor;
import org.spongepowered.api.command.CommandCause;

public class SpongeBuildContext extends BuildContext<CommandCause, AdventureComponent> {
    protected final MclogsSpongePlugin plugin;

    public SpongeBuildContext(MclogsSpongePlugin plugin) {
        super(CommandEnvironment.DEDICATED_SERVER);
        this.plugin = plugin;
    }

    @Override
    public ICommandSourceAccessor<AdventureComponent> mapSource(CommandCause source) {
        return new SpongeCommandCauseAccessor(plugin, source);
    }
}
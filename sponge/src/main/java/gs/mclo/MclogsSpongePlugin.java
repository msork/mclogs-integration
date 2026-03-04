package gs.mclo;

import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.plugin.PluginContainer;
import com.mojang.brigadier.CommandDispatcher;
import gs.mclo.components.AdventureComponentFactory;
import gs.mclo.commands.SpongeBrigadierCommand;
import gs.mclo.commands.SpongeBuildContext;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.event.lifecycle.RegisterCommandEvent;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.Server;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.lang.reflect.Field;
import java.nio.file.Path;

@Plugin("mclogs")
public class MclogsSpongePlugin {
    private static PluginContainer PLUGIN_CONTAINER;
    private static Path CONFIG_DIR;

    private final Logger logger;

    protected final MclogsCommon mclogsCommon = new MclogsCommon();
    protected CommandDispatcher<CommandCause> dispatcher;
    protected SpongeBuildContext context;
    protected AdventureComponentFactory componentFactory;

    private boolean initialized = false;

    @Inject
    public MclogsSpongePlugin(
            final PluginContainer container,
            final Logger logger,
            @ConfigDir(sharedRoot = false) final Path configDir
    ) {
        PLUGIN_CONTAINER = container;
        CONFIG_DIR = configDir;
        this.logger = logger;
    }

    public Logger logger() {
        return logger;
    }

    public static PluginContainer pluginContainer() {
        if (PLUGIN_CONTAINER == null) throw new IllegalStateException("PluginContainer is not initialized yet");
        return PLUGIN_CONTAINER;
    }

    public static Path configDir() {
        if (CONFIG_DIR == null) throw new IllegalStateException("configDir is not initialized yet");
        return CONFIG_DIR;
    }

    private void initIfNeeded() {
        if (initialized) return;
        initialized = true;

        mclogsCommon.init();

        dispatcher = new CommandDispatcher<>();
        componentFactory = new AdventureComponentFactory();
        context = new SpongeBuildContext(this);

        mclogsCommon.registerCommands(dispatcher, context, componentFactory);
    }

    @Listener
    public void onEnable(StartedEngineEvent<Server> event) {
        initIfNeeded();
    }

    @Listener
    public void onRegisterCommands(RegisterCommandEvent<Command.Raw> event) {
        initIfNeeded();
        event.register(pluginContainer(), new SpongeBrigadierCommand(dispatcher, context, componentFactory), "mclogs", "mclo");
    }

    @Listener
    public void onDisable(StoppingEngineEvent<Server> event) {
        try {
            if (mclogsCommon.configFile != null) {
                mclogsCommon.configFile.save();
                mclogsCommon.configFile.close();
            }
        } catch (Exception e) {
            logger.warn("Failed to close config file cleanly", e);
        }
    }
}
package gs.mclo.platform.services;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.GenericBuilder;
import gs.mclo.MclogsSpongePlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.plugin.PluginContainer;

import java.nio.file.Files;
import java.nio.file.Path;

public class SpongePlatformHelper implements IPlatformHelper {
    private PluginContainer pluginContainer() {
        return MclogsSpongePlugin.pluginContainer();
    }

    private Path configDir() {
        return MclogsSpongePlugin.configDir();
    }

    @Override
    public String getPlatformName() {
        return "Sponge";
    }

    @Override
    public String getMinecraftVersion() {
        // e.g. 1.21.11
        return Sponge.game().platform().minecraftVersion().name();
    }

    @Override
    public String getModVersion() {
        var v = pluginContainer().metadata().version();
        return v == null ? "UNKNOWN" : v.toString();
    }

    @Override
    public GenericBuilder<Config, FileConfig> getConfig() {
        var dataDir = MclogsSpongePlugin.configDir();

        try {
            Files.createDirectories(dataDir);
        } catch (Exception ignored) {}

        return FileConfig.builder(dataDir.resolve("config.toml"));
    }
}
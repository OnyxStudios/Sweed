package nerdhub.sweed.config;

import nerdhub.sweed.Sweed;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

@Deprecated
public class ConfigReloader implements SimpleSynchronousResourceReloadListener {

    private static final Identifier ID = new Identifier(Sweed.MODID, "config_reloader");

    public static void init() {
        Sweed.logger.debug("enabling config reloader");
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new ConfigReloader());
    }

    @Override
    public Identifier getFabricId() {
        return ID;
    }

    @Override
    public void apply(ResourceManager var1) {
        Sweed.logger.debug("reloading configs");
        ConfigHandler.getRegisteredConfigs().forEach(ConfigHandler::reloadConfig);
    }
}
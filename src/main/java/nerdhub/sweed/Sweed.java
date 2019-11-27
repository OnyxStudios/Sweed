package nerdhub.sweed;

import nerdhub.sweed.config.ConfigHandler;
import nerdhub.sweed.config.ConfigReloader;
import nerdhub.sweed.config.SweedConfig;
import nerdhub.sweed.init.SweedBlocks;
import nerdhub.sweed.init.SweedItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sweed implements ModInitializer {

    public static final String MODID = "sweed";
    public static final Logger logger = LogManager.getLogger(MODID);
    public static final Tag<Block> SWEED_SOIL = TagRegistry.block(new Identifier(MODID, "sweed_soil"));
    public static final Tag<Block> CROP_LIKE = TagRegistry.block(new Identifier(MODID, "crop_like"));

    @Override
    public void onInitialize() {
        logger.info("sweed victory!");
        ConfigHandler.registerConfig(MODID, SweedConfig.class);
        Registry.register(Registry.BLOCK, new Identifier(MODID, "sweed"), SweedBlocks.SWEED);
        Registry.register(Registry.ITEM, new Identifier(MODID, "sweed_seeds"), SweedItems.SWEED_SEEDS);
        ConfigReloader.init();
    }

    public static SweedConfig getConfig() {
        return ConfigHandler.getConfig(SweedConfig.class);
    }
}

package nerdhub.sweed.client;

import nerdhub.sweed.init.SweedBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class SweedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //we need to register render layers manually now
        BlockRenderLayerMap.INSTANCE.putBlock(SweedBlocks.SWEED, RenderLayer.getCutout());
    }
}

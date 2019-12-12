package nerdhub.sweed.init;

import com.wtoll.demeter.api.item.SeedItem;
import nerdhub.sweed.Sweed;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class SweedItems {

    public static final Item SWEED_SEEDS = sweedItem();

    private static Item sweedItem() {
        Item sweedItem = new AliasedBlockItem(SweedBlocks.SWEED, new Item.Settings().group(ItemGroup.MATERIALS));
        // Check for alternate definitions for compatibility
        if (Sweed.DEMETER) {
            sweedItem = new SeedItem(SweedBlocks.SWEED, new Item.Settings().group(ItemGroup.MATERIALS));
        }
        return sweedItem;
    }
}

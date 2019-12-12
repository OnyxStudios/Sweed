package nerdhub.sweed.init;

import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class SweedItems {
    public static final Item SWEED_SEEDS = new AliasedBlockItem(SweedBlocks.SWEED, new Item.Settings().group(ItemGroup.MATERIALS));
}

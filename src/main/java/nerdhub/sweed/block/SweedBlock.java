package nerdhub.sweed.block;

import nerdhub.sweed.Sweed;
import nerdhub.sweed.config.SweedConfig;
import nerdhub.sweed.init.SweedItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BeetrootsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class SweedBlock extends BeetrootsBlock {

    public SweedBlock() {
        super(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).build());
    }

    @Environment(EnvType.CLIENT)
    @Override
    protected ItemConvertible getSeedsItem() {
        return SweedItems.SWEED_SEEDS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (Sweed.DEMETER) {
            super.scheduledTick(state, world, pos, random);
            return;
        }

        SweedConfig config = Sweed.getConfig();
        if(random.nextDouble() < 0.66D) {
            int age = this.getAge(state);
            if(age < this.getMaxAge()) {
                float moisture = Math.max(getAvailableMoisture(this, world, pos), 1.0F);
                if(world.getLightLevel(pos, 0) >= 9 && random.nextInt((int) (25.0F / moisture) + 1) == 0) {
                    if(age == this.getMaxAge() - 1 && !config.aggressiveSpread && random.nextDouble() < config.spreadChance) {
                        if(this.spread(world, pos, random) > 0) {
                            return;
                        }
                    }
                    world.setBlockState(pos, this.withAge(age + 1), 2);
                }
            }
            else if(config.aggressiveSpread && random.nextDouble() < config.spreadChance) {
                this.spread(world, pos, random);
            }
        }
    }

    private int spread(World world, BlockPos pos, Random random) {
        int maxSpreads = random.nextInt(Math.max(1, Sweed.getConfig().maxSpreadPlants)) + 1;
        int spreads = 0;
        for(int i = 0; i < Sweed.getConfig().maxSpreadAttempts && spreads < maxSpreads; ++i) {
            BlockPos testPos = pos.add(random.nextInt(7) - 3, random.nextInt(5) - 3, random.nextInt(7) - 3);
            if(world.getBlockState(testPos).getBlock() == this) {
                maxSpreads--;
                continue;
            }
            BlockPos down = testPos.down();
            BlockState state = world.getBlockState(testPos);
            if((state.isAir() || Sweed.CROP_LIKE.contains(state.getBlock())) && world.getFluidState(pos).isEmpty() && canPlantOnTop(world.getBlockState(down), world, down) && world.getLightLevel(testPos) >= Sweed.getConfig().spreadMinLightLevel) {
                world.setBlockState(testPos, this.getDefaultState());
                spreads++;
            }
        }
        return spreads;
    }

    @Override
    protected boolean canPlantOnTop(BlockState state, BlockView world, BlockPos pos) {
        return Sweed.SWEED_SOIL.contains(state.getBlock());
    }

    @Override
    public boolean isMature(BlockState state) {
        return this.getAge(state) > this.getMaxAge();
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (Sweed.DEMETER) {
            super.grow(world, random, pos, state);
            return;
        }


        if(this.getAge(state) == this.getMaxAge() && random.nextDouble() < Sweed.getConfig().spreadChance) {
            this.spread(world, pos, random);
        }
        super.grow(world, random, pos, state);
    }
}

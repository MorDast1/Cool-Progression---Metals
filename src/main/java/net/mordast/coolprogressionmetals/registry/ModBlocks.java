package net.mordast.coolprogressionmetals.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Ores and metal blocks. Ores drop themselves (they are blasted from the ore block, see the recipes); the tool tier
 * each block needs is defined by block tags: mithril_ore -> iron, adamant_ore -> Mithril, nixtris_ore -> Adamant.
 */
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CPMetalsMod.MOD_ID);

    public static final DeferredBlock<Block> MITHRIL_BLOCK = register("mithril_block", 4.0F, 4.0F, SoundType.AMETHYST);
    public static final DeferredBlock<Block> ADAMANT_BLOCK = register("adamant_block", 5.0F, 6.0F, SoundType.NETHERITE_BLOCK);
    public static final DeferredBlock<Block> NIXTRIS_BLOCK = register("nixtris_block", 6.0F, 8.0F, SoundType.NETHERITE_BLOCK);

    public static final DeferredBlock<Block> MITHRIL_ORE = register("mithril_ore", 3.5F, 3.5F, SoundType.STONE);
    public static final DeferredBlock<Block> ADAMANT_ORE = register("adamant_ore", 5.0F, 8.0F, SoundType.ANCIENT_DEBRIS);
    public static final DeferredBlock<Block> NIXTRIS_ORE = register("nixtris_ore", 6.0F, 9.0F, SoundType.NETHER_ORE);

    private ModBlocks() {}

    /** Registers a simple block that needs the right tool to drop, together with its block item. */
    private static DeferredBlock<Block> register(String name, float hardness, float resistance, SoundType sound) {
        DeferredBlock<Block> block = BLOCKS.registerSimpleBlock(name, BlockBehaviour.Properties.of()
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops()
                .sound(sound));
        ModItems.ITEMS.registerSimpleBlockItem(name, block);
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

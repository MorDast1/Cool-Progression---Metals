package net.mordast.coolprogressionmetals.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.Item.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Properties;
import java.util.function.Supplier;

public class Modblocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(CPMetalsMod.MOD_ID);

    public static final DeferredBlock<Block> MITHRIL_BLOCK = registerBlock("mithril_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(4f).requiresCorrectToolForDrops().sound(SoundType.AMETHYST)));
    public static final DeferredBlock<Block> ADAMANT_BLOCK = registerBlock("adamant_block",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> NIXTRIS_BLOCK = registerBlock("nixtris_block",
            () -> new Block(BlockBehaviour.Properties.of()));

    public static final DeferredBlock<Block> MITHRIL_ORE = registerBlock("mithril_ore",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> ADAMANT_ORE = registerBlock("adamant_ore",
            () -> new Block(BlockBehaviour.Properties.of()));
    public static final DeferredBlock<Block> NIXTRIS_ORE = registerBlock("nixtris_ore",
            () -> new Block(BlockBehaviour.Properties.of()));




    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

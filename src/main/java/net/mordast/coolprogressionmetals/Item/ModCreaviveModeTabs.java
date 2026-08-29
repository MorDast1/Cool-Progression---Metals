package net.mordast.coolprogressionmetals.Item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.block.Modblocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreaviveModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CPMetalsMod.MOD_ID);

    public static final Supplier<CreativeModeTab> C_P_METAL_ITEM_TAB = CREATIVE_MODE_TAB.register("cool_progression___metals",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ADAMANT_INGOT.get()))
                    .title(Component.translatable("creative.coolprogressionmetals.items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ADAMANT);
                        output.accept(ModItems.ADAMANT_INGOT);
                        output.accept(ModItems.ADAMANT_MIXTURE);

                        output.accept(ModItems.MITHRIL);
                        output.accept(ModItems.MITHRIL_INGOT);
                        output.accept(ModItems.MITHRIL_MIXTURE);

                        output.accept(ModItems.NIXTRIS);
                        output.accept(ModItems.NIXTRIS_INGOT);
                        output.accept(ModItems.NIXTRIS_POLYMER);


                        output.accept(Modblocks.ADAMANT_BLOCK);
                        output.accept(Modblocks.ADAMANT_ORE);
                        output.accept(Modblocks.MITHRIL_BLOCK);
                        output.accept(Modblocks.MITHRIL_ORE);
                        output.accept(Modblocks.NIXTRIS_BLOCK);
                        output.accept(Modblocks.NIXTRIS_ORE);
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}

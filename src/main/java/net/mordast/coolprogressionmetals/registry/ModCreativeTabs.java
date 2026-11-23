package net.mordast.coolprogressionmetals.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.material.Metal;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/** The mod's creative tab: grouped by metal; per metal crafting resources, then tools, then armor. */
public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CPMetalsMod.MOD_ID);

    public static final Supplier<CreativeModeTab> MAIN = TABS.register("cool_progression___metals",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.ADAMANT_INGOT.get()))
                    .title(Component.translatable("creative.coolprogressionmetals.items"))
                    .displayItems((parameters, output) -> {
                        addMetal(output, Metal.MITHRIL,
                                ModBlocks.MITHRIL_ORE, ModItems.MITHRIL, ModItems.MITHRIL_MIXTURE,
                                ModItems.MITHRIL_INGOT, ModBlocks.MITHRIL_BLOCK);
                        addMetal(output, Metal.ADAMANT,
                                ModBlocks.ADAMANT_ORE, ModItems.ADAMANT, ModItems.ADAMANT_MIXTURE,
                                ModItems.ADAMANT_INGOT, ModBlocks.ADAMANT_BLOCK, ModItems.ADAMANT_ROD,
                                ModItems.ADAMANT_UPGRADE_TEMPLATE);
                        addMetal(output, Metal.NIXTRIS,
                                ModBlocks.NIXTRIS_ORE, ModItems.NIXTRIS, ModItems.NIXTRIS_POLYMER,
                                ModItems.NIXTRIS_INGOT, ModBlocks.NIXTRIS_BLOCK, ModItems.NIXTRIS_ROD,
                                ModItems.NIXTRIS_CORE);
                        output.accept(ModItems.DRAGON_CHESTPLATE);   // closes the Nixtris armor row
                    })
                    .build());

    private ModCreativeTabs() {}

    /** Crafting resources first, then the metal's tools, then its armor. */
    private static void addMetal(CreativeModeTab.Output output, Metal metal, ItemLike... resources) {
        for (ItemLike resource : resources) {
            output.accept(resource);
        }
        MetalSet set = ModItems.equipment(metal);
        set.tools().all().forEach(output::accept);
        set.armor().all().forEach(output::accept);
    }

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}

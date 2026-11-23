package net.mordast.coolprogressionmetals;

import net.mordast.coolprogressionmetals.registry.ModArmorMaterials;
import net.mordast.coolprogressionmetals.registry.ModBlocks;
import net.mordast.coolprogressionmetals.registry.ModCreativeTabs;
import net.mordast.coolprogressionmetals.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * Cool Progression - Metals: Mithril, Adamant and Nixtris with their ores, tools and armor,
 * plus the Dragon Chestplate (see the {@code dragon} package).
 */
@Mod(CPMetalsMod.MOD_ID)
public class CPMetalsMod {
    /** Must match {@code mod_id} in gradle.properties and the mods.toml. */
    public static final String MOD_ID = "coolprogressionmetalsmod";

    public CPMetalsMod(IEventBus modEventBus) {
        ModArmorMaterials.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
    }
}

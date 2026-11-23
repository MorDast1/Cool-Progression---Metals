package net.mordast.coolprogressionmetals.registry;

import net.minecraft.world.item.Item;
import net.mordast.coolprogressionmetals.material.Metal;
import net.neoforged.neoforge.registries.DeferredRegister;

/** All equipment of one metal: five tools and four armor pieces. */
public record MetalSet(Metal metal, ToolSet tools, ArmorSet armor) {

    static MetalSet register(DeferredRegister.Items items, Metal metal) {
        return new MetalSet(metal, ToolSet.register(items, metal), ArmorSet.register(items, metal));
    }

    /** Item properties shared by the whole set: fireproof metals survive lava and fire. */
    static Item.Properties properties(Metal metal) {
        Item.Properties properties = new Item.Properties();
        return metal.isFireproof() ? properties.fireResistant() : properties;
    }
}

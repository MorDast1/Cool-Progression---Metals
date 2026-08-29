package net.mordast.coolprogressionmetals.Item;

import net.minecraft.world.item.Item;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CPMetalsMod.MOD_ID);

    public static final DeferredItem<Item> MITHRIL_INGOT = ITEMS.register("mithril_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ADAMANT_INGOT = ITEMS.register("adamant_ingot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NIXTRIS_INGOT = ITEMS.register("nixtris_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MITHRIL = ITEMS.register("mithril",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ADAMANT = ITEMS.register("adamant",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NIXTRIS = ITEMS.register("nixtris",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MITHRIL_MIXTURE = ITEMS.register("mithril_mixture",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ADAMANT_MIXTURE = ITEMS.register("adamant_mixture",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> NIXTRIS_POLYMER = ITEMS.register("nixtris_polymer",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

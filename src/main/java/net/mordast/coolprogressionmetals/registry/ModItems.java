package net.mordast.coolprogressionmetals.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.dragon.DragonChestplateItem;
import net.mordast.coolprogressionmetals.material.Metal;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

/** All items of the mod. Block items are registered next to their blocks in {@link ModBlocks}. */
public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CPMetalsMod.MOD_ID);

    // ---- crafting resources ----
    public static final DeferredItem<Item> MITHRIL = ITEMS.registerSimpleItem("mithril");
    public static final DeferredItem<Item> ADAMANT = ITEMS.registerSimpleItem("adamant");
    public static final DeferredItem<Item> NIXTRIS = ITEMS.registerSimpleItem("nixtris");

    public static final DeferredItem<Item> MITHRIL_MIXTURE = ITEMS.registerSimpleItem("mithril_mixture");
    public static final DeferredItem<Item> ADAMANT_MIXTURE = ITEMS.registerSimpleItem("adamant_mixture");
    public static final DeferredItem<Item> NIXTRIS_POLYMER = ITEMS.registerSimpleItem("nixtris_polymer");

    public static final DeferredItem<Item> MITHRIL_INGOT = ITEMS.registerSimpleItem("mithril_ingot");
    public static final DeferredItem<Item> ADAMANT_INGOT = ITEMS.registerSimpleItem("adamant_ingot");
    public static final DeferredItem<Item> NIXTRIS_INGOT = ITEMS.registerSimpleItem("nixtris_ingot");

    public static final DeferredItem<Item> ADAMANT_ROD = ITEMS.registerSimpleItem("adamant_rod");
    public static final DeferredItem<Item> NIXTRIS_ROD = ITEMS.registerSimpleItem("nixtris_rod");
    public static final DeferredItem<Item> NIXTRIS_CORE =
            ITEMS.registerSimpleItem("nixtris_core", new Item.Properties().rarity(Rarity.RARE));

    public static final DeferredItem<SmithingTemplateItem> ADAMANT_UPGRADE_TEMPLATE =
            ITEMS.register("adamant_upgrade_smithing_template", SmithingTemplates::adamantUpgrade);

    // ---- special equipment ----
    public static final DeferredItem<DragonChestplateItem> DRAGON_CHESTPLATE =
            ITEMS.register("dragon_chestplate", DragonChestplateItem::new);

    // ---- tools and armor of every metal ----
    private static final Map<Metal, MetalSet> EQUIPMENT = new EnumMap<>(Metal.class);

    static {
        for (Metal metal : Metal.values()) {
            EQUIPMENT.put(metal, MetalSet.register(ITEMS, metal));
        }
    }

    private ModItems() {}

    public static MetalSet equipment(Metal metal) {
        return EQUIPMENT.get(metal);
    }

    /** The ingot that crafts and repairs the equipment of the given metal. */
    public static DeferredItem<Item> ingot(Metal metal) {
        return switch (metal) {
            case MITHRIL -> MITHRIL_INGOT;
            case ADAMANT -> ADAMANT_INGOT;
            case NIXTRIS -> NIXTRIS_INGOT;
        };
    }

    public static Ingredient repairIngredient(Metal metal) {
        return Ingredient.of(ingot(metal).get());
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

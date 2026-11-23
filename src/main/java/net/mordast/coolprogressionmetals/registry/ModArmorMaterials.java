package net.mordast.coolprogressionmetals.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.material.Metal;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * One registered {@link ArmorMaterial} per {@link Metal}. Worn textures:
 * {@code assets/<modid>/textures/models/armor/<metal>_layer_1.png} and {@code _layer_2.png}.
 */
public final class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, CPMetalsMod.MOD_ID);

    private static final Map<Metal, Holder<ArmorMaterial>> MATERIALS = new EnumMap<>(Metal.class);

    static {
        for (Metal metal : Metal.values()) {
            MATERIALS.put(metal, create(metal));
        }
    }

    private ModArmorMaterials() {}

    public static Holder<ArmorMaterial> of(Metal metal) {
        return MATERIALS.get(metal);
    }

    private static Holder<ArmorMaterial> create(Metal metal) {
        Metal.ArmorStats stats = metal.armor();
        Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
        defense.put(ArmorItem.Type.BOOTS, stats.boots());
        defense.put(ArmorItem.Type.LEGGINGS, stats.leggings());
        defense.put(ArmorItem.Type.CHESTPLATE, stats.chestplate());
        defense.put(ArmorItem.Type.HELMET, stats.helmet());
        defense.put(ArmorItem.Type.BODY, stats.body());
        List<ArmorMaterial.Layer> layers =
                List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(CPMetalsMod.MOD_ID, metal.id())));

        return ARMOR_MATERIALS.register(metal.id(), () -> new ArmorMaterial(defense, stats.enchantability(),
                stats.equipSound(), () -> ModItems.repairIngredient(metal), layers, stats.toughness(),
                stats.knockbackResistance()));
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}

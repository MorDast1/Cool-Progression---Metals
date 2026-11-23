package net.mordast.coolprogressionmetals.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.mordast.coolprogressionmetals.material.Metal;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/** The four armor pieces of one metal. */
public record ArmorSet(DeferredItem<ArmorItem> helmet, DeferredItem<ArmorItem> chestplate,
                       DeferredItem<ArmorItem> leggings, DeferredItem<ArmorItem> boots) {

    static ArmorSet register(DeferredRegister.Items items, Metal metal) {
        return new ArmorSet(
                piece(items, metal, "helmet", ArmorItem.Type.HELMET),
                piece(items, metal, "chestplate", ArmorItem.Type.CHESTPLATE),
                piece(items, metal, "leggings", ArmorItem.Type.LEGGINGS),
                piece(items, metal, "boots", ArmorItem.Type.BOOTS));
    }

    private static DeferredItem<ArmorItem> piece(DeferredRegister.Items items, Metal metal, String name,
                                                 ArmorItem.Type type) {
        Holder<ArmorMaterial> material = ModArmorMaterials.of(metal);
        int durability = type.getDurability(metal.armor().durabilityMultiplier());
        return items.register(metal.id() + "_" + name,
                () -> new ArmorItem(material, type, MetalSet.properties(metal).durability(durability)));
    }

    /** In creative-tab order. */
    public List<DeferredItem<?>> all() {
        return List.of(helmet, chestplate, leggings, boots);
    }
}

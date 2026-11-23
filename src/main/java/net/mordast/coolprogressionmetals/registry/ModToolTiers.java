package net.mordast.coolprogressionmetals.registry;

import net.minecraft.world.item.Tier;
import net.mordast.coolprogressionmetals.material.Metal;
import net.mordast.coolprogressionmetals.util.ModTags;
import net.neoforged.neoforge.common.SimpleTier;

import java.util.EnumMap;
import java.util.Map;

/** One tool {@link Tier} per {@link Metal}; the harvestable blocks come from the incorrect_for_*_tool block tags. */
public final class ModToolTiers {
    private static final Map<Metal, Tier> TIERS = new EnumMap<>(Metal.class);

    static {
        for (Metal metal : Metal.values()) {
            Metal.ToolStats stats = metal.tool();
            TIERS.put(metal, new SimpleTier(ModTags.incorrectBlocksFor(metal), stats.durability(), stats.miningSpeed(),
                    stats.attackDamageBonus(), stats.enchantability(), () -> ModItems.repairIngredient(metal)));
        }
    }

    private ModToolTiers() {}

    public static Tier of(Metal metal) {
        return TIERS.get(metal);
    }
}

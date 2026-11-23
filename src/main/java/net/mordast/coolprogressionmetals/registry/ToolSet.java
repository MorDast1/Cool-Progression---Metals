package net.mordast.coolprogressionmetals.registry;

import net.minecraft.world.item.*;
import net.mordast.coolprogressionmetals.material.Metal;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

/** The five tools of one metal. */
public record ToolSet(DeferredItem<SwordItem> sword, DeferredItem<PickaxeItem> pickaxe, DeferredItem<AxeItem> axe,
                      DeferredItem<ShovelItem> shovel, DeferredItem<HoeItem> hoe) {

    // Vanilla base values: attack damage on top of the tier bonus, and the attack-speed modifier.
    private static final int SWORD_DAMAGE = 3;
    private static final float SWORD_SPEED = -2.4F;
    private static final float PICKAXE_DAMAGE = 1.0F;
    private static final float PICKAXE_SPEED = -2.8F;
    private static final float AXE_DAMAGE = 5.0F;
    private static final float AXE_SPEED = -3.0F;
    private static final float SHOVEL_DAMAGE = 1.5F;
    private static final float SHOVEL_SPEED = -3.0F;
    private static final float HOE_SPEED = 0.0F;

    static ToolSet register(DeferredRegister.Items items, Metal metal) {
        Tier tier = ModToolTiers.of(metal);
        float swing = metal.tool().swingSpeedOffset();
        String id = metal.id();
        return new ToolSet(
                items.register(id + "_sword", () -> new SwordItem(tier, MetalSet.properties(metal)
                        .attributes(SwordItem.createAttributes(tier, SWORD_DAMAGE, SWORD_SPEED + swing)))),
                items.register(id + "_pickaxe", () -> new PickaxeItem(tier, MetalSet.properties(metal)
                        .attributes(PickaxeItem.createAttributes(tier, PICKAXE_DAMAGE, PICKAXE_SPEED + swing)))),
                items.register(id + "_axe", () -> new AxeItem(tier, MetalSet.properties(metal)
                        .attributes(AxeItem.createAttributes(tier, AXE_DAMAGE, AXE_SPEED + swing)))),
                items.register(id + "_shovel", () -> new ShovelItem(tier, MetalSet.properties(metal)
                        .attributes(ShovelItem.createAttributes(tier, SHOVEL_DAMAGE, SHOVEL_SPEED + swing)))),
                items.register(id + "_hoe", () -> new HoeItem(tier, MetalSet.properties(metal)
                        .attributes(HoeItem.createAttributes(tier, -tier.getAttackDamageBonus(), HOE_SPEED + swing)))));
    }

    /** In creative-tab order. */
    public List<DeferredItem<?>> all() {
        return List.of(sword, pickaxe, axe, shovel, hoe);
    }
}

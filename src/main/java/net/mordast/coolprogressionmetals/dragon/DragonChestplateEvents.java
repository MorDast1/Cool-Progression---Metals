package net.mordast.coolprogressionmetals.dragon;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/** Game-bus events of the Dragon Chestplate. */
@EventBusSubscriber(modid = CPMetalsMod.MOD_ID)
public final class DragonChestplateEvents {
    /** The penalty and the bottle refill are both checked once per second. */
    private static final int PULSE_INTERVAL_TICKS = 20;
    /** Damage per pulse (1.0 = half a heart) for wearing the chestplate without the Nixtris support armor. */
    private static final float UNSUPPORTED_DAMAGE = 1.0F;

    private DragonChestplateEvents() {}

    /**
     * Immunity: while the chestplate holds at least one empty bottle, damage from dragon's breath clouds is cancelled.
     * (Instant-damage cloud effects bypass addEffect, so they are stopped here.) Without empty bottles the cloud
     * damages the wearer as usual.
     */
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        DamageSource source = event.getSource();
        boolean fromBreath = source.is(DamageTypes.DRAGON_BREATH)
                || (source.getDirectEntity() instanceof AreaEffectCloud cloud && DragonAbilities.isBreathCloud(cloud));
        if (!fromBreath) return;

        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.getItem() instanceof DragonChestplateItem && DragonStorage.hasEmptyBottles(chest)) {
            event.setCanceled(true);
        }
    }

    /**
     * Once per second:
     * <ul>
     *   <li>chestplate WITHOUT the Nixtris helmet, leggings and boots: the wearer takes magic damage (ignores armor);</li>
     *   <li>with the full Nixtris set inside a dragon's breath cloud: one empty bottle becomes dragon's breath.</li>
     * </ul>
     */
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.tickCount % PULSE_INTERVAL_TICKS != 0) return;
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!(chest.getItem() instanceof DragonChestplateItem)) return;

        if (!DragonChestplateItem.hasSupportArmor(player)) {
            player.hurt(player.damageSources().magic(), UNSUPPORTED_DAMAGE);
        } else if (DragonAbilities.isInBreathCloud(player)) {
            DragonStorage.bottleToBreath(chest);
        }
    }
}

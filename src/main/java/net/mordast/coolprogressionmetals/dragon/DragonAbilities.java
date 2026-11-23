package net.mordast.coolprogressionmetals.dragon;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** Gameplay rules of the Dragon Chestplate that are not tied to a single event. */
public final class DragonAbilities {
    /** A boost is only allowed after gliding this long, so the jump press that starts the glide never triggers it. */
    public static final int MIN_GLIDE_TICKS = 5;

    private static final double BOOST_STRENGTH = 1.5;       // extra speed (blocks/tick) along the look direction
    private static final int BOOST_COOLDOWN_TICKS = 10;
    private static final double CLOUD_SEARCH_RADIUS = 8.0;

    private DragonAbilities() {}

    /** Server side: a gliding wearer pressed jump again. Costs one bottle of dragon's breath (it becomes an empty bottle). */
    public static void boost(ServerPlayer player) {
        if (!player.isFallFlying() || player.getFallFlyingTicks() < MIN_GLIDE_TICKS) return;
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!(chest.getItem() instanceof DragonChestplateItem)) return;
        if (player.getCooldowns().isOnCooldown(chest.getItem())) return;
        if (!DragonStorage.breathToBottle(chest)) return;

        player.getCooldowns().addCooldown(chest.getItem(), BOOST_COOLDOWN_TICKS);
        player.setDeltaMovement(player.getDeltaMovement().add(player.getLookAngle().scale(BOOST_STRENGTH)));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));

        ServerLevel level = player.serverLevel();
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDER_DRAGON_FLAP, SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.DRAGON_BREATH, player.getX(), player.getY(), player.getZ(),
                20, 0.3, 0.3, 0.3, 0.02);
    }

    public static boolean isBreathCloud(AreaEffectCloud cloud) {
        return cloud.getParticle().getType() == ParticleTypes.DRAGON_BREATH;
    }

    /** Whether the player stands inside a dragon's breath cloud (same overlap test the cloud itself uses). */
    public static boolean isInBreathCloud(Player player) {
        return !player.level().getEntitiesOfClass(AreaEffectCloud.class,
                player.getBoundingBox().inflate(CLOUD_SEARCH_RADIUS), cloud -> {
                    double dx = player.getX() - cloud.getX();
                    double dz = player.getZ() - cloud.getZ();
                    return isBreathCloud(cloud)
                            && dx * dx + dz * dz <= cloud.getRadius() * cloud.getRadius()
                            && cloud.getBoundingBox().intersects(player.getBoundingBox());
                }).isEmpty();
    }
}

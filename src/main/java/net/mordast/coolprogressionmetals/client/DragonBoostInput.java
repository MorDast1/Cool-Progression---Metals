package net.mordast.coolprogressionmetals.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.dragon.DragonAbilities;
import net.mordast.coolprogressionmetals.network.DragonBoostPayload;
import net.mordast.coolprogressionmetals.registry.ModItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Detects a fresh jump press while gliding in the Dragon Chestplate and asks the server for a boost. */
@EventBusSubscriber(modid = CPMetalsMod.MOD_ID, value = Dist.CLIENT)
public final class DragonBoostInput {
    private static boolean jumpWasDown;

    private DragonBoostInput() {}

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        boolean jumpDown = minecraft.options.keyJump.isDown();
        if (jumpDown && !jumpWasDown && minecraft.player != null && canBoost(minecraft.player)) {
            PacketDistributor.sendToServer(new DragonBoostPayload());
        }
        jumpWasDown = jumpDown;
    }

    private static boolean canBoost(LocalPlayer player) {
        return player.isFallFlying()
                && player.getFallFlyingTicks() >= DragonAbilities.MIN_GLIDE_TICKS
                && player.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.DRAGON_CHESTPLATE.get());
    }
}

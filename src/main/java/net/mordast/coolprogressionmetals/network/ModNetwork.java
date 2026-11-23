package net.mordast.coolprogressionmetals.network;

import net.minecraft.server.level.ServerPlayer;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.dragon.DragonAbilities;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Registers the mod's network payloads. */
@EventBusSubscriber(modid = CPMetalsMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";

    private ModNetwork() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(DragonBoostPayload.TYPE, DragonBoostPayload.CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player) {
                        DragonAbilities.boost(player);
                    }
                }));
    }
}

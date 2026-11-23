package net.mordast.coolprogressionmetals.client;

import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.dragon.DragonChestplateTooltip;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

/** Client-only mod-bus setup. */
@EventBusSubscriber(modid = CPMetalsMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientSetup {
    private ClientSetup() {}

    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(DragonChestplateTooltip.class, DragonChestplateClientTooltip::new);
    }
}

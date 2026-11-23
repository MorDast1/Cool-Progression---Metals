package net.mordast.coolprogressionmetals.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.mordast.coolprogressionmetals.CPMetalsMod;

/** Client -> server: a gliding player pressed jump again while wearing the Dragon Chestplate. Carries no data. */
public record DragonBoostPayload() implements CustomPacketPayload {
    public static final Type<DragonBoostPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CPMetalsMod.MOD_ID, "dragon_boost"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DragonBoostPayload> CODEC =
            StreamCodec.unit(new DragonBoostPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

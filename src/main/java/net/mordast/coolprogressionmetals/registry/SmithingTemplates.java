package net.mordast.coolprogressionmetals.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import net.mordast.coolprogressionmetals.CPMetalsMod;

import java.util.Arrays;
import java.util.List;

/** Factory for the mod's smithing templates (texts live under the {@code upgrade.<modid>.*} lang keys). */
final class SmithingTemplates {
    private static final String ADAMANT_UPGRADE = "upgrade." + CPMetalsMod.MOD_ID + ".adamant_upgrade";

    private SmithingTemplates() {}

    /** Mithril equipment + Adamant Ingot -> Adamant equipment. */
    static SmithingTemplateItem adamantUpgrade() {
        return new SmithingTemplateItem(
                Component.translatable(ADAMANT_UPGRADE + ".applies_to").withStyle(ChatFormatting.BLUE),
                Component.translatable(ADAMANT_UPGRADE + ".ingredients").withStyle(ChatFormatting.BLUE),
                Component.translatable(ADAMANT_UPGRADE).withStyle(ChatFormatting.GRAY),
                Component.translatable(ADAMANT_UPGRADE + ".base_slot_description"),
                Component.translatable(ADAMANT_UPGRADE + ".additions_slot_description"),
                vanillaIcons("item/empty_armor_slot_helmet", "item/empty_armor_slot_chestplate",
                        "item/empty_armor_slot_leggings", "item/empty_armor_slot_boots", "item/empty_slot_sword",
                        "item/empty_slot_pickaxe", "item/empty_slot_axe", "item/empty_slot_hoe",
                        "item/empty_slot_shovel"),
                vanillaIcons("item/empty_slot_ingot"));
    }

    private static List<ResourceLocation> vanillaIcons(String... paths) {
        return Arrays.stream(paths).map(ResourceLocation::withDefaultNamespace).toList();
    }
}

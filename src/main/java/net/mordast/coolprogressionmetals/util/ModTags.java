package net.mordast.coolprogressionmetals.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.mordast.coolprogressionmetals.CPMetalsMod;
import net.mordast.coolprogressionmetals.material.Metal;

/** Block tags that drive the mining progression. Their contents are in {@code data/.../tags/block/}. */
public final class ModTags {
    /** Blocks that need at least a Mithril tool to drop their loot. */
    public static final TagKey<Block> NEEDS_MITHRIL_TOOL = block("needs_mithril_tool");
    /** Blocks that need at least an Adamant tool to drop their loot. */
    public static final TagKey<Block> NEEDS_ADAMANT_TOOL = block("needs_adamant_tool");

    /** Blocks a tool of the given tier can NOT harvest. */
    public static final TagKey<Block> INCORRECT_FOR_MITHRIL_TOOL = block("incorrect_for_mithril_tool");
    public static final TagKey<Block> INCORRECT_FOR_ADAMANT_TOOL = block("incorrect_for_adamant_tool");
    public static final TagKey<Block> INCORRECT_FOR_NIXTRIS_TOOL = block("incorrect_for_nixtris_tool");

    private ModTags() {}

    public static TagKey<Block> incorrectBlocksFor(Metal metal) {
        return switch (metal) {
            case MITHRIL -> INCORRECT_FOR_MITHRIL_TOOL;
            case ADAMANT -> INCORRECT_FOR_ADAMANT_TOOL;
            case NIXTRIS -> INCORRECT_FOR_NIXTRIS_TOOL;
        };
    }

    private static TagKey<Block> block(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(CPMetalsMod.MOD_ID, name));
    }
}

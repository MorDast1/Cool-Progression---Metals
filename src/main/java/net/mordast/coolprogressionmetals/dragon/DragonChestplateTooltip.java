package net.mordast.coolprogressionmetals.dragon;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** Tooltip data for the bundle-style slot display: the stacks in the chestplate's three slots. */
public record DragonChestplateTooltip(List<ItemStack> items) implements TooltipComponent {
}

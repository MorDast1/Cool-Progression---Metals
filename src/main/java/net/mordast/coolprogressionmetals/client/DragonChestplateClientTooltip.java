package net.mordast.coolprogressionmetals.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.mordast.coolprogressionmetals.dragon.DragonChestplateTooltip;
import net.mordast.coolprogressionmetals.dragon.DragonStorage;

import java.util.List;

/** Draws the three slots of the Dragon Chestplate under its name, like the bundle tooltip. */
public class DragonChestplateClientTooltip implements ClientTooltipComponent {
    private static final int SLOT_SIZE = 18;
    private static final int VERTICAL_PADDING = 2;

    private static final int COLOR_SLOT_DARK = 0xFF373737;
    private static final int COLOR_SLOT_LIGHT = 0xFFFFFFFF;
    private static final int COLOR_SLOT_FILL = 0xFF8B8B8B;

    private final List<ItemStack> items;

    public DragonChestplateClientTooltip(DragonChestplateTooltip tooltip) {
        this.items = tooltip.items();
    }

    @Override
    public int getHeight() {
        return SLOT_SIZE + 2 * VERTICAL_PADDING;
    }

    @Override
    public int getWidth(Font font) {
        return DragonStorage.SLOTS * SLOT_SIZE;
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
        int top = y + VERTICAL_PADDING;
        for (int i = 0; i < DragonStorage.SLOTS; i++) {
            int left = x + i * SLOT_SIZE;
            drawSlot(graphics, left, top);
            if (i < items.size() && !items.get(i).isEmpty()) {
                ItemStack stack = items.get(i);
                graphics.renderItem(stack, left + 1, top + 1);
                graphics.renderItemDecorations(font, stack, left + 1, top + 1);
            }
        }
    }

    /** Vanilla-style inventory slot: dark top/left edge, light bottom/right edge, grey inside. */
    private static void drawSlot(GuiGraphics graphics, int left, int top) {
        graphics.fill(left, top, left + SLOT_SIZE, top + SLOT_SIZE, COLOR_SLOT_DARK);
        graphics.fill(left + 1, top + 1, left + SLOT_SIZE, top + SLOT_SIZE, COLOR_SLOT_LIGHT);
        graphics.fill(left + 1, top + 1, left + SLOT_SIZE - 1, top + SLOT_SIZE - 1, COLOR_SLOT_FILL);
    }
}

package net.mordast.coolprogressionmetals.dragon;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * The Dragon Chestplate's store: three slots that hold only empty bottles and bottles of dragon's breath, kept in the
 * chestplate's {@link DataComponents#CONTAINER} component. Reads return copies; only the mutating methods write back.
 */
public final class DragonStorage {
    public static final int SLOTS = 3;
    private static final int SLOT_CAP = 64;

    private DragonStorage() {}

    public static boolean isStorable(ItemStack stack) {
        return stack.is(Items.GLASS_BOTTLE) || stack.is(Items.DRAGON_BREATH);
    }

    /** A copy of the three slots; empty slots are {@link ItemStack#EMPTY}. */
    public static List<ItemStack> read(ItemStack chest) {
        NonNullList<ItemStack> items = NonNullList.withSize(SLOTS, ItemStack.EMPTY);
        chest.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(items);
        return items;
    }

    /** Immunity to dragon's breath needs at least one empty bottle. Allocation-free: this runs on damage events. */
    public static boolean hasEmptyBottles(ItemStack chest) {
        ItemContainerContents contents = chest.get(DataComponents.CONTAINER);
        if (contents == null) return false;
        for (ItemStack stack : contents.nonEmptyItems()) {
            if (stack.is(Items.GLASS_BOTTLE)) return true;
        }
        return false;
    }

    /** Turns one dragon's breath into an empty bottle. Returns false if there is none or no room. */
    public static boolean breathToBottle(ItemStack chest) {
        return convert(chest, Items.DRAGON_BREATH, Items.GLASS_BOTTLE);
    }

    /** Turns one empty bottle into dragon's breath. Returns false if there is none or no room. */
    public static boolean bottleToBreath(ItemStack chest) {
        return convert(chest, Items.GLASS_BOTTLE, Items.DRAGON_BREATH);
    }

    /** Stores as much of {@code incoming} as fits; returns the part that did not fit (not modified itself). */
    public static ItemStack insert(ItemStack chest, ItemStack incoming) {
        List<ItemStack> items = read(chest);
        ItemStack rest = insertInto(items, incoming);
        if (rest.getCount() != incoming.getCount()) {
            write(chest, items);
        }
        return rest;
    }

    /**
     * Removes and returns the ENTIRE last non-empty slot (every bottle in it), or {@link ItemStack#EMPTY} if the store
     * is empty or {@code accepts} rejects that stack (nothing is removed then).
     */
    public static ItemStack extractLastSlot(ItemStack chest, Predicate<ItemStack> accepts) {
        List<ItemStack> items = read(chest);
        int index = lastFilledSlot(items);
        if (index < 0 || !accepts.test(items.get(index))) return ItemStack.EMPTY;
        ItemStack taken = items.get(index).copy();
        items.set(index, ItemStack.EMPTY);
        write(chest, items);
        return taken;
    }

    private static boolean convert(ItemStack chest, Item from, Item to) {
        List<ItemStack> items = read(chest);
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).is(from)) continue;
            List<ItemStack> trial = copyAll(items);
            trial.get(i).shrink(1);
            if (insertInto(trial, new ItemStack(to)).isEmpty()) {
                write(chest, trial);
                return true;
            }
        }
        return false;
    }

    private static ItemStack insertInto(List<ItemStack> items, ItemStack incoming) {
        ItemStack rest = incoming.copy();
        if (!isStorable(rest)) return rest;
        for (ItemStack slot : items) {                      // top up matching stacks first
            if (rest.isEmpty()) break;
            if (!slot.isEmpty() && ItemStack.isSameItemSameComponents(slot, rest)) {
                int move = Math.min(rest.getCount(), SLOT_CAP - slot.getCount());
                if (move > 0) {
                    slot.grow(move);
                    rest.shrink(move);
                }
            }
        }
        for (int i = 0; i < items.size() && !rest.isEmpty(); i++) {   // then use empty slots
            if (items.get(i).isEmpty()) {
                int move = Math.min(rest.getCount(), SLOT_CAP);
                items.set(i, rest.copyWithCount(move));
                rest.shrink(move);
            }
        }
        return rest;
    }

    private static int lastFilledSlot(List<ItemStack> items) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (!items.get(i).isEmpty()) return i;
        }
        return -1;
    }

    private static List<ItemStack> copyAll(List<ItemStack> items) {
        List<ItemStack> copy = new ArrayList<>(items.size());
        for (ItemStack stack : items) copy.add(stack.copy());
        return copy;
    }

    private static void write(ItemStack chest, List<ItemStack> items) {
        items.replaceAll(stack -> stack.isEmpty() ? ItemStack.EMPTY : stack);
        chest.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }
}

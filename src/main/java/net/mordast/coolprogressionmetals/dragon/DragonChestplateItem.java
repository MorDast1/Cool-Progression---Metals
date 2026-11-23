package net.mordast.coolprogressionmetals.dragon;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.mordast.coolprogressionmetals.material.Metal;
import net.mordast.coolprogressionmetals.registry.ModArmorMaterials;
import net.mordast.coolprogressionmetals.registry.ModItems;

import java.util.Optional;

/**
 * A Nixtris chestplate that glides like an elytra and carries a three-slot store of empty bottles and bottles of
 * dragon's breath (see {@link DragonStorage}). The boost, cloud immunity, refill and the penalty for wearing it
 * without the Nixtris support armor are in {@link DragonAbilities} and {@link DragonChestplateEvents}.
 */
public class DragonChestplateItem extends ArmorItem {
    /** Gliding costs one durability point per this many flight ticks, like an elytra. */
    private static final int FLIGHT_TICKS_PER_DAMAGE = 20;

    public DragonChestplateItem() {
        super(ModArmorMaterials.of(Metal.NIXTRIS), Type.CHESTPLATE, new Item.Properties()
                .rarity(Rarity.EPIC)
                .durability(Type.CHESTPLATE.getDurability(Metal.NIXTRIS.armor().durabilityMultiplier())));
    }

    /** True when the player also wears the Nixtris helmet, leggings and boots. */
    public static boolean hasSupportArmor(Player player) {
        var nixtris = ModItems.equipment(Metal.NIXTRIS).armor();
        return player.getItemBySlot(EquipmentSlot.HEAD).is(nixtris.helmet().get())
                && player.getItemBySlot(EquipmentSlot.LEGS).is(nixtris.leggings().get())
                && player.getItemBySlot(EquipmentSlot.FEET).is(nixtris.boots().get());
    }

    // ---------------------------------------------------------------- gliding (elytra behaviour)

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;   // like an elytra: unusable when almost broken
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide && (flightTicks + 1) % FLIGHT_TICKS_PER_DAMAGE == 0) {
            stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
        }
        return true;
    }

    // ---------------------------------------------------------------- bundle-style interaction

    /** Right-click the chestplate in a slot: insert the cursor stack, or take out a whole slot if the cursor is empty. */
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction action,
                                            Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        if (other.isEmpty()) {
            ItemStack taken = DragonStorage.extractLastSlot(self, stack -> true);
            if (taken.isEmpty()) return false;
            access.set(taken);
            return true;
        }
        if (!DragonStorage.isStorable(other)) return false;
        other.setCount(DragonStorage.insert(self, other).getCount());
        return true;
    }

    /** Chestplate on the cursor, right-click a slot: store that slot's stack, or move a whole stored slot into an empty one. */
    @Override
    public boolean overrideStackedOnOther(ItemStack self, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;
        ItemStack target = slot.getItem();
        if (target.isEmpty()) {
            ItemStack taken = DragonStorage.extractLastSlot(self, slot::mayPlace);
            if (taken.isEmpty()) return false;
            slot.set(taken);
            return true;
        }
        if (!DragonStorage.isStorable(target)) return false;
        ItemStack rest = DragonStorage.insert(self, target);
        if (rest.getCount() != target.getCount()) {
            slot.set(rest.isEmpty() ? ItemStack.EMPTY : rest);
        }
        return true;
    }

    /** The three-slot display shown in the tooltip. */
    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return Optional.of(new DragonChestplateTooltip(DragonStorage.read(stack)));
    }
}

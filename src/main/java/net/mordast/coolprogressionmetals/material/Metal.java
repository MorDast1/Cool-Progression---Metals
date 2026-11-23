package net.mordast.coolprogressionmetals.material;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

/**
 * The three metals of the mod. Every balance number for tools and armor lives here, so tuning a metal means
 * editing exactly one place. Progression: wood &lt; stone &lt; iron &lt; gold &lt; diamond &lt; netherite &lt;
 * MITHRIL &lt; ADAMANT &lt; NIXTRIS.
 */
public enum Metal {
    /** Light and fast with great enchantability; roughly Netherite-level, not fireproof. */
    MITHRIL("mithril", false,
            new ToolStats(2000, 10.0F, 4.0F, 25, 0.2F),
            new ArmorStats(37, 3, 6, 8, 3, 11, 3.0F, 0.05F, 25, SoundEvents.ARMOR_EQUIP_DIAMOND)),

    /** Heavy: the most durable and the toughest, swings slower, fireproof. */
    ADAMANT("adamant", true,
            new ToolStats(3200, 9.5F, 5.0F, 12, -0.2F),
            new ArmorStats(50, 4, 7, 9, 4, 13, 5.0F, 0.15F, 12, SoundEvents.ARMOR_EQUIP_NETHERITE)),

    /** Unstable: strongest hits and fastest mining, but less durable than Adamant and NOT fireproof. */
    NIXTRIS("nixtris", false,
            new ToolStats(2700, 12.0F, 6.0F, 18, 0.0F),
            new ArmorStats(42, 4, 8, 9, 4, 14, 4.0F, 0.1F, 18, SoundEvents.ARMOR_EQUIP_NETHERITE));

    private final String id;
    private final boolean fireproof;
    private final ToolStats tool;
    private final ArmorStats armor;

    Metal(String id, boolean fireproof, ToolStats tool, ArmorStats armor) {
        this.id = id;
        this.fireproof = fireproof;
        this.tool = tool;
        this.armor = armor;
    }

    /** Lower-case name used in registry ids, e.g. {@code mithril} -> {@code mithril_pickaxe}. */
    public String id() { return id; }

    /** Whether the metal's items survive lava and fire. */
    public boolean isFireproof() { return fireproof; }

    public ToolStats tool() { return tool; }

    public ArmorStats armor() { return armor; }

    /**
     * @param swingSpeedOffset added to every tool's vanilla attack speed (positive = swings faster)
     */
    public record ToolStats(int durability, float miningSpeed, float attackDamageBonus, int enchantability,
                            float swingSpeedOffset) {}

    /**
     * @param durabilityMultiplier vanilla per-slot base durability (13/15/16/11) is multiplied by this
     * @param body                 defense value used for horse armor
     */
    public record ArmorStats(int durabilityMultiplier, int boots, int leggings, int chestplate, int helmet, int body,
                             float toughness, float knockbackResistance, int enchantability,
                             Holder<SoundEvent> equipSound) {}
}

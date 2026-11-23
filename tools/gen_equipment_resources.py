#!/usr/bin/env python3
"""Generates the data-driven resources of the Mithril / Adamant / Nixtris content.

Run from the project root:   python3 tools/gen_equipment_resources.py

What it writes under src/main/resources:
  * JSON (item models, recipes, loot tables, block/item tags, worldgen, biome modifiers): always rewritten,
    so edit THIS script, not the generated files.
  * lang entries: merged into the existing en_us.json / ru_ru.json; existing keys are never touched.
  * placeholder PNGs: only written when the file does not exist yet, so real art is never overwritten.
"""
import json
import os
import struct
import zlib

MOD_ID = "coolprogressionmetalsmod"        # namespace of assets, loot tables, tags and worldgen
RECIPE_NS = "coolprogressionmetals"         # name of the existing recipe folder in this repo
RES = "src/main/resources"
ASSETS = f"{RES}/assets/{MOD_ID}"
DATA = f"{RES}/data/{MOD_ID}"
RECIPES = f"{RES}/data/{RECIPE_NS}/recipe"

METALS = ["mithril", "adamant", "nixtris"]
TOOLS = ["sword", "pickaxe", "axe", "shovel", "hoe"]
ARMOR = ["helmet", "chestplate", "leggings", "boots"]
DRAGON_CHESTPLATE = f"{MOD_ID}:dragon_chestplate"
ADAMANT_TEMPLATE = f"{MOD_ID}:adamant_upgrade_smithing_template"


def mod(name):
    return f"{MOD_ID}:{name}"


# ------------------------------------------------------------------ file helpers

def write_json(path, obj):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", newline="\n") as f:
        json.dump(obj, f, indent=2)
        f.write("\n")


def write_png(path, width, height, pixel):
    """Writes an RGBA PNG unless the file already exists."""
    if os.path.exists(path):
        return
    os.makedirs(os.path.dirname(path), exist_ok=True)
    rows = b"".join(b"\x00" + b"".join(bytes(pixel(x, y)) for x in range(width)) for y in range(height))

    def chunk(kind, data):
        body = struct.pack(">I", len(data)) + kind + data
        return body + struct.pack(">I", zlib.crc32(kind + data) & 0xffffffff)

    with open(path, "wb") as f:
        f.write(b"\x89PNG\r\n\x1a\n"
                + chunk(b"IHDR", struct.pack(">IIBBBBB", width, height, 8, 6, 0, 0, 0))
                + chunk(b"IDAT", zlib.compress(rows))
                + chunk(b"IEND", b""))


# ------------------------------------------------------------------ textures (placeholders)

def missing_texture(x, y):
    """The standard black/magenta checkerboard (8x8 squares): obviously a placeholder."""
    return (248, 0, 248, 255) if ((x // 8) + (y // 8)) % 2 else (0, 0, 0, 255)


def generate_textures():
    for metal in METALS:
        for part in TOOLS + ARMOR:
            write_png(f"{ASSETS}/textures/item/{metal}_{part}.png", 16, 16, missing_texture)
        for layer in (1, 2):
            write_png(f"{ASSETS}/textures/models/armor/{metal}_layer_{layer}.png", 64, 32, missing_texture)
        for kind in ("ore", "block"):
            write_png(f"{ASSETS}/textures/block/{metal}_{kind}.png", 16, 16, missing_texture)
    for name in EXTRA_ITEMS:
        write_png(f"{ASSETS}/textures/item/{name}.png", 16, 16, missing_texture)


# ------------------------------------------------------------------ item models

EXTRA_ITEMS = ["adamant_rod", "nixtris_rod", "nixtris_core", "adamant_upgrade_smithing_template", "dragon_chestplate"]


def item_model(name, parent):
    write_json(f"{ASSETS}/models/item/{name}.json",
               {"parent": parent, "textures": {"layer0": mod(f"item/{name}")}})


def generate_item_models():
    for metal in METALS:
        for tool in TOOLS:
            item_model(f"{metal}_{tool}", "minecraft:item/handheld")
        for piece in ARMOR:
            item_model(f"{metal}_{piece}", "minecraft:item/generated")
    for name in EXTRA_ITEMS:
        item_model(name, "minecraft:item/handheld_rod" if name.endswith("_rod") else "minecraft:item/generated")


# ------------------------------------------------------------------ recipes

def ingredient(item):
    return {"item": item}


def write_recipe(name, recipe):
    write_json(f"{RECIPES}/{name}.json", recipe)


def shaped(name, result, pattern, key, category="equipment", count=1):
    write_recipe(name, {"type": "minecraft:crafting_shaped", "category": category,
                        "key": {k: ingredient(v) for k, v in key.items()}, "pattern": pattern,
                        "result": {"count": count, "id": result}})


def blasting(name, source, result, xp=20, cooking_time=100):
    write_recipe(name, {"type": "minecraft:blasting", "category": "misc", "cookingtime": cooking_time,
                        "experience": xp, "group": name.split("_")[0],
                        "ingredient": ingredient(source), "result": {"count": 1, "id": result}})


def smithing(name, template, base, addition, result):
    write_recipe(name, {"type": "minecraft:smithing_transform", "template": ingredient(template),
                        "base": ingredient(base), "addition": ingredient(addition),
                        "result": {"count": 1, "id": result}})


EQUIPMENT_PATTERNS = {
    "sword": ["#", "#", "S"], "pickaxe": ["###", " S ", " S "], "axe": ["##", "#S", " S"],
    "shovel": ["#", "S", "S"], "hoe": ["##", " S", " S"],
    "helmet": ["###", "# #"], "chestplate": ["# #", "###", "###"],
    "leggings": ["###", "# #", "# #"], "boots": ["# #", "# #"],
}
HANDLE = {"mithril": "minecraft:stick", "adamant": mod("adamant_rod"), "nixtris": mod("nixtris_rod")}


def generate_equipment_recipes():
    """Mithril and Nixtris: every piece is crafted. Adamant: tools are crafted (with Adamant Rods) or upgraded from
    Mithril; Adamant armor has no crafting recipe - it is only made with the Adamant Upgrade."""
    for metal in METALS:
        for part, pattern in EQUIPMENT_PATTERNS.items():
            if metal == "adamant" and part in ARMOR:
                continue
            key = {"#": mod(f"{metal}_ingot")}
            if any("S" in row for row in pattern):
                key["S"] = HANDLE[metal]
            shaped(f"{metal}_{part}", mod(f"{metal}_{part}"), pattern, key)
    for part in ARMOR:                                  # drop leftovers of the old armor recipes
        stale = f"{RECIPES}/adamant_{part}.json"
        if os.path.exists(stale):
            os.remove(stale)


def generate_special_recipes():
    shaped("adamant_rod", mod("adamant_rod"), [" A ", " B ", " A "],
           {"A": mod("adamant"), "B": "minecraft:blaze_rod"}, "misc", count=2)
    shaped("nixtris_rod", mod("nixtris_rod"), [" N ", " C ", " N "],
           {"N": mod("nixtris_ingot"), "C": "minecraft:popped_chorus_fruit"}, "misc", count=2)

    # Nixtris Core: nether star in the centre, dragon head below, Nixtris in the corners, ingots elsewhere.
    shaped("nixtris_core", mod("nixtris_core"), ["QIQ", "ISI", "QDQ"],
           {"Q": mod("nixtris"), "I": mod("nixtris_ingot"),
            "S": "minecraft:nether_star", "D": "minecraft:dragon_head"}, "misc")

    # Adamant Upgrade template (placeholder recipe, change freely).
    shaped("adamant_upgrade_smithing_template", ADAMANT_TEMPLATE, ["MMM", "MAM", "MMM"],
           {"M": mod("mithril_ingot"), "A": mod("adamant")}, "misc", count=2)

    # Adamant Upgrade: template + Mithril equipment + Adamant Ingot -> Adamant equipment.
    for part in EQUIPMENT_PATTERNS:
        smithing(f"adamant_upgrade_{part}", ADAMANT_TEMPLATE, mod(f"mithril_{part}"),
                 mod("adamant_ingot"), mod(f"adamant_{part}"))

    # Dragon Chestplate: Nixtris chestplate (base) + Nixtris Core (addition) + elytra in the template slot.
    smithing("dragon_chestplate", "minecraft:elytra", mod("nixtris_chestplate"),
             mod("nixtris_core"), DRAGON_CHESTPLATE)


def generate_material_chain_recipes():
    """ore -> processed item -> (mixture / polymer) -> ingot. Mithril/Adamant mixtures are hand-written recipes."""
    blasting("mithril_from_ore_blasting", mod("mithril_ore"), mod("mithril"))
    blasting("nixtris_from_ore_blasting", mod("nixtris_ore"), mod("nixtris"))
    shaped("nixtris_polymer", mod("nixtris_polymer"), ["BCB", "CAC", "BCB"],
           {"A": mod("nixtris"), "B": "minecraft:ender_pearl", "C": "minecraft:crying_obsidian"}, "misc", count=2)
    blasting("nixtris_ingot", mod("nixtris_polymer"), mod("nixtris_ingot"))


# ------------------------------------------------------------------ loot tables

def generate_loot_tables():
    """Every ore and metal block drops itself (ores are blasted from the ore block)."""
    for metal in METALS:
        for kind in ("ore", "block"):
            name = f"{metal}_{kind}"
            write_json(f"{DATA}/loot_table/blocks/{name}.json", {
                "type": "minecraft:block",
                "pools": [{"bonus_rolls": 0.0, "conditions": [{"condition": "minecraft:survives_explosion"}],
                           "entries": [{"type": "minecraft:item", "name": mod(name)}], "rolls": 1.0}],
                "random_sequence": mod(f"blocks/{name}")})


# ------------------------------------------------------------------ tags

def write_tag(path, values):
    write_json(path, {"replace": False, "values": values})


def generate_block_tags():
    tags = f"{RES}/data/minecraft/tags/block"
    write_tag(f"{tags}/mineable/pickaxe.json", [mod(f"{m}_{k}") for m in METALS for k in ("ore", "block")])
    write_tag(f"{tags}/needs_iron_tool.json", [mod("mithril_ore"), mod("mithril_block")])
    write_tag(f"{DATA}/tags/block/needs_mithril_tool.json", [mod("adamant_ore"), mod("adamant_block")])
    write_tag(f"{DATA}/tags/block/needs_adamant_tool.json", [mod("nixtris_ore"), mod("nixtris_block")])
    write_tag(f"{DATA}/tags/block/incorrect_for_mithril_tool.json", [f"#{mod('needs_adamant_tool')}"])
    write_tag(f"{DATA}/tags/block/incorrect_for_adamant_tool.json", [])
    write_tag(f"{DATA}/tags/block/incorrect_for_nixtris_tool.json", [])
    for vanilla in ("wooden", "stone", "iron", "gold", "diamond", "netherite"):   # vanilla tools can't harvest them
        write_tag(f"{tags}/incorrect_for_{vanilla}_tool.json",
                  [f"#{mod('needs_mithril_tool')}", f"#{mod('needs_adamant_tool')}"])


def pieces(kind):
    """Ids of one equipment kind across all metals; the Dragon Chestplate is a chestplate too."""
    ids = [mod(f"{metal}_{kind}") for metal in METALS]
    return ids + [DRAGON_CHESTPLATE] if kind == "chestplate" else ids


def generate_item_tags():
    tags = f"{RES}/data/minecraft/tags/item"
    vanilla_names = {"sword": "swords", "pickaxe": "pickaxes", "axe": "axes", "shovel": "shovels", "hoe": "hoes",
                     "helmet": "head_armor", "chestplate": "chest_armor", "leggings": "leg_armor",
                     "boots": "foot_armor"}
    for kind, tag in vanilla_names.items():
        write_tag(f"{tags}/{tag}.json", pieces(kind))

    armor = [i for kind in ARMOR for i in pieces(kind)]
    tools = [i for kind in TOOLS for i in pieces(kind)]
    mining = [i for kind in ("pickaxe", "axe", "shovel", "hoe") for i in pieces(kind)]
    write_tag(f"{tags}/trimmable_armor.json", armor)

    enchantable = f"{tags}/enchantable"
    write_tag(f"{enchantable}/sword.json", pieces("sword"))
    write_tag(f"{enchantable}/weapon.json", pieces("sword") + pieces("axe"))
    write_tag(f"{enchantable}/sharp_weapon.json", pieces("sword") + pieces("axe"))
    write_tag(f"{enchantable}/mining.json", mining)
    write_tag(f"{enchantable}/mining_loot.json", mining)
    write_tag(f"{enchantable}/durability.json", tools + armor)
    write_tag(f"{enchantable}/armor.json", armor)
    for kind, tag in (("helmet", "head_armor"), ("chestplate", "chest_armor"),
                      ("leggings", "leg_armor"), ("boots", "foot_armor")):
        write_tag(f"{enchantable}/{tag}.json", pieces(kind))


# ------------------------------------------------------------------ worldgen
# Netherite (ancient debris) is two "scattered_ore" batches per chunk, both hidden from air:
#     large: size 3, trapezoid Y 8..24        small: size 2, uniform Y 8..119
# Adamant ore (Nether): the same two batches behind rarity_filter(3)  -> about 3x rarer than netherite.
# Nixtris ore (End):    two batches on end stone behind rarity_filter(5) -> about 5x rarer than netherite.
# Mithril ore (Overworld): ordinary ore veins.

def height_range(kind, low, high):
    return {"type": "minecraft:height_range", "height": {
        "type": f"minecraft:{kind}", "min_inclusive": {"absolute": low}, "max_inclusive": {"absolute": high}}}


def scattered_ore(name, block, size, discard_chance, target):
    write_json(f"{DATA}/worldgen/configured_feature/{name}.json", {
        "type": "minecraft:scattered_ore",
        "config": {"size": size, "discard_chance_on_air_exposure": discard_chance,
                   "targets": [{"target": target, "state": {"Name": mod(block)}}]}})


def placed_feature(name, placement):
    write_json(f"{DATA}/worldgen/placed_feature/{name}.json", {"feature": mod(name), "placement": placement})


def rare_placement(chance, height):
    return [{"type": "minecraft:rarity_filter", "chance": chance}, {"type": "minecraft:in_square"},
            height, {"type": "minecraft:biome"}]


def biome_modifier(name, biomes, features):
    write_json(f"{DATA}/neoforge/biome_modifier/{name}.json", {
        "type": "neoforge:add_features", "biomes": biomes, "step": "underground_ores",
        "features": [mod(f) for f in features]})


def generate_worldgen():
    # Mithril: Overworld veins.
    write_json(f"{DATA}/worldgen/configured_feature/mithril_ore.json", {"type": "minecraft:ore", "config": {
        "size": 6, "discard_chance_on_air_exposure": 0.0,
        "targets": [{"target": {"predicate_type": "minecraft:tag_match", "tag": tag},
                     "state": {"Name": mod("mithril_ore")}}
                    for tag in ("minecraft:stone_ore_replaceables", "minecraft:deepslate_ore_replaceables")]}})
    placed_feature("mithril_ore", [{"type": "minecraft:count", "count": 5}, {"type": "minecraft:in_square"},
                                   height_range("uniform", -64, 16), {"type": "minecraft:biome"}])

    # Adamant: Nether, same shape / heights / air-discard as ancient debris at a third of the rate.
    nether_stone = {"predicate_type": "minecraft:tag_match", "tag": "minecraft:base_stone_nether"}
    scattered_ore("adamant_ore_large", "adamant_ore", 3, 1.0, nether_stone)
    scattered_ore("adamant_ore_small", "adamant_ore", 2, 1.0, nether_stone)
    placed_feature("adamant_ore_large", rare_placement(3, height_range("trapezoid", 8, 24)))
    placed_feature("adamant_ore_small", rare_placement(3, height_range("uniform", 8, 119)))

    # Nixtris: the End, a fifth of the netherite rate. End islands are thin, so only half of the air-exposed
    # veins are discarded (1.0 would make it far rarer than 5x). Tune with the rarity chance.
    end_stone = {"predicate_type": "minecraft:block_match", "block": "minecraft:end_stone"}
    scattered_ore("nixtris_ore_large", "nixtris_ore", 3, 0.5, end_stone)
    scattered_ore("nixtris_ore_small", "nixtris_ore", 2, 0.5, end_stone)
    placed_feature("nixtris_ore_large", rare_placement(5, height_range("trapezoid", 30, 80)))
    placed_feature("nixtris_ore_small", rare_placement(5, height_range("uniform", 30, 90)))

    biome_modifier("add_overworld_ores", "#minecraft:is_overworld", ["mithril_ore"])
    biome_modifier("add_nether_ores", "#minecraft:is_nether", ["adamant_ore_large", "adamant_ore_small"])
    biome_modifier("add_end_ores", "#minecraft:is_end", ["nixtris_ore_large", "nixtris_ore_small"])


# ------------------------------------------------------------------ lang (merged, existing text untouched)

EN_METAL = {"mithril": "Mithril", "adamant": "Adamant", "nixtris": "Nixtris"}
# Russian adjective by grammatical gender: masculine, feminine, plural.
RU_METAL = {"mithril": ("Мифриловый", "Мифриловая", "Мифриловые"),
            "adamant": ("Адамантиевый", "Адамантиевая", "Адамантиевые"),
            "nixtris": ("Никстрисовый", "Никстрисовая", "Никстрисовые")}
RU_NOUN = {"sword": (0, "меч"), "pickaxe": (1, "кирка"), "axe": (0, "топор"), "shovel": (1, "лопата"),
           "hoe": (1, "мотыга"), "helmet": (0, "шлем"), "chestplate": (0, "нагрудник"),
           "leggings": (2, "поножи"), "boots": (2, "ботинки")}

SPECIAL_EN = {
    f"item.{MOD_ID}.adamant_rod": "Adamant Rod",
    f"item.{MOD_ID}.nixtris_rod": "Nixtris Rod",
    f"item.{MOD_ID}.nixtris_core": "Nixtris Core",
    f"item.{MOD_ID}.adamant_upgrade_smithing_template": "Adamant Upgrade",
    f"item.{MOD_ID}.dragon_chestplate": "Dragon Chestplate",
    f"upgrade.{MOD_ID}.adamant_upgrade": "Adamant Upgrade",
    f"upgrade.{MOD_ID}.adamant_upgrade.applies_to": "Mithril equipment",
    f"upgrade.{MOD_ID}.adamant_upgrade.ingredients": "Adamant Ingot",
    f"upgrade.{MOD_ID}.adamant_upgrade.base_slot_description": "Add Mithril equipment",
    f"upgrade.{MOD_ID}.adamant_upgrade.additions_slot_description": "Add an Adamant Ingot",
}
SPECIAL_RU = {
    f"item.{MOD_ID}.adamant_rod": "Адамантовый стержень",
    f"item.{MOD_ID}.nixtris_rod": "Никстрисовый стержень",
    f"item.{MOD_ID}.nixtris_core": "Никстрисовое ядро",
    f"item.{MOD_ID}.adamant_upgrade_smithing_template": "Адамантовое улучшение",
    f"item.{MOD_ID}.dragon_chestplate": "Драконий нагрудник",
    f"upgrade.{MOD_ID}.adamant_upgrade": "Адамантовое улучшение",
    f"upgrade.{MOD_ID}.adamant_upgrade.applies_to": "Мифриловое снаряжение",
    f"upgrade.{MOD_ID}.adamant_upgrade.ingredients": "Адамантовый слиток",
    f"upgrade.{MOD_ID}.adamant_upgrade.base_slot_description": "Добавьте мифриловое снаряжение",
    f"upgrade.{MOD_ID}.adamant_upgrade.additions_slot_description": "Добавьте адамантовый слиток",
}


def merge_lang(path, entries):
    """Appends the entries that are missing, keeping the file's text and line endings otherwise untouched."""
    with open(path, "rb") as f:
        raw = f.read().decode("utf-8")
    newline = "\r\n" if "\r\n" in raw else "\n"
    missing = {k: v for k, v in entries.items() if k not in json.loads(raw)}
    if not missing:
        return
    body = raw.rstrip()
    assert body.endswith("}")
    body = body[:-1].rstrip()
    separator = newline if body.endswith(",") else "," + newline + newline
    lines = ("," + newline).join(
        f"  {json.dumps(k, ensure_ascii=False)}: {json.dumps(v, ensure_ascii=False)}" for k, v in missing.items())
    with open(path, "wb") as f:
        f.write((body + separator + lines + newline + "}").encode("utf-8"))


def generate_lang():
    english, russian = dict(), dict()
    for metal in METALS:
        for kind in TOOLS + ARMOR:
            key = f"item.{MOD_ID}.{metal}_{kind}"
            english[key] = f"{EN_METAL[metal]} {kind.capitalize()}"
            gender, noun = RU_NOUN[kind]
            russian[key] = f"{RU_METAL[metal][gender]} {noun}"
    english.update(SPECIAL_EN)
    russian.update(SPECIAL_RU)
    merge_lang(f"{ASSETS}/lang/en_us.json", english)
    merge_lang(f"{ASSETS}/lang/ru_ru.json", russian)


# ------------------------------------------------------------------ main

def main():
    generate_textures()
    generate_item_models()
    generate_equipment_recipes()
    generate_special_recipes()
    generate_material_chain_recipes()
    generate_loot_tables()
    generate_block_tags()
    generate_item_tags()
    generate_worldgen()
    generate_lang()
    print("resources generated")


if __name__ == "__main__":
    main()

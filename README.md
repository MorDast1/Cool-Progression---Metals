# Cool Progression - Metals

A NeoForge 1.21.1 mod that adds three tiers of metal on top of Netherite, each with its own ore, resources, a full
tool set and a full armor set, plus one special chestplate.

| Metal   | Found in   | Needs to mine it   | Character                                                    |
|---------|------------|--------------------|--------------------------------------------------------------|
| Mithril | Overworld  | iron pickaxe       | light and fast, great enchantability, roughly Netherite-level |
| Adamant | Nether     | Mithril pickaxe    | heaviest, most durable and toughest, fireproof               |
| Nixtris | The End    | Adamant pickaxe    | strongest hits, fastest mining, less durable, not fireproof  |

* **Adamant** equipment is made from Mithril equipment with the *Adamant Upgrade* smithing template (tools can also be
  crafted with Adamant Rods). **Nixtris** tools use Nixtris Rods.
* **Dragon Chestplate** (Nixtris chestplate + Nixtris Core + elytra on a smithing table): glides like an elytra, has a
  three-slot store for empty bottles and dragon's breath, boosts the glide with a jump press, and protects from
  dragon's breath clouds - but hurts the wearer unless the Nixtris helmet, leggings and boots are worn too.

All numbers (durability, speed, damage, enchantability, armor, toughness, knockback resistance, fireproofing) are in
one file: [`material/Metal.java`](src/main/java/net/mordast/coolprogressionmetals/material/Metal.java).

## Code layout

```
src/main/java/net/mordast/coolprogressionmetals
├── CPMetalsMod.java          mod entry point: registers everything
├── material/                 Metal enum - the single source of truth for tool and armor stats
├── registry/                 DeferredRegisters: ModBlocks, ModItems, ModArmorMaterials, ModToolTiers,
│                             ModCreativeTabs; MetalSet / ToolSet / ArmorSet build the equipment of one metal
├── dragon/                   Dragon Chestplate: item, 3-slot DragonStorage, DragonAbilities, game events
├── network/                  payloads (the glide boost press) and their registration
├── client/                   client only: boost key detection, bundle-style tooltip
└── util/                     ModTags (mining-tier block tags)
```

```
src/main/resources
├── assets/coolprogressionmetalsmod     lang, blockstates, models, textures
└── data/                               recipes, loot tables, tags, worldgen, biome modifiers
```

## Building and running

* `./gradlew build` builds the mod, `./gradlew runClient` / `runServer` start the game.
* If the IDE is missing libraries: `./gradlew --refresh-dependencies`; `./gradlew clean` resets the build.
* The project uses the official Mojang mapping names; their license is described in
  <https://github.com/NeoForged/NeoForm/blob/main/Mojang.md>.

## Resources are generated

Recipes, loot tables, tags, worldgen, item models and the equipment lang entries come from
[`tools/gen_equipment_resources.py`](tools/gen_equipment_resources.py). Edit the script and re-run it from the
project root instead of editing those JSON files by hand:

```
python3 tools/gen_equipment_resources.py
```

JSON is rewritten on every run, lang keys are only appended when missing, and placeholder textures (the black and
magenta checkerboard) are only created when the file does not exist, so finished art is never overwritten.
The hand-written recipes (`adamant_mixture`, `mithril_mixture`, ...) are not touched by the script.

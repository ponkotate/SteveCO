# Blocks & Items Registration (Fabric 1.21.4)

## Block Registration

```java
// Helper method pattern
private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory,
                               BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
    ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK,
        Identifier.fromNamespaceAndPath(MOD_ID, name));
    Block block = blockFactory.apply(settings.setId(blockKey));

    if (shouldRegisterItem) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath(MOD_ID, name));
        BlockItem blockItem = new BlockItem(block, new Item.Properties().setId(itemKey).useBlockDescriptionPrefix());
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
    }

    return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
}

// Usage
public static final Block CONDENSED_DIRT = register(
    "condensed_dirt", Block::new,
    BlockBehaviour.Properties.of().sound(SoundType.GRASS),
    true  // also register BlockItem
);
```

**Kotlin equivalent:**

```kotlin
private fun register(name: String, factory: (BlockBehaviour.Properties) -> Block,
                     settings: BlockBehaviour.Properties, registerItem: Boolean = true): Block {
    val blockKey = ResourceKey.create(Registries.BLOCK,
        Identifier.fromNamespaceAndPath(MOD_ID, name))
    val block = factory(settings.setId(blockKey))

    if (registerItem) {
        val itemKey = ResourceKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath(MOD_ID, name))
        val blockItem = BlockItem(block, Item.Properties().setId(itemKey).useBlockDescriptionPrefix())
        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem)
    }

    return Registry.register(BuiltInRegistries.BLOCK, blockKey, block)
}
```

### Block Properties

- `BlockBehaviour.Properties.of()` — new empty properties
- `BlockBehaviour.Properties.ofFullCopy(existingBlock)` — copy from existing
- `.sound(SoundType.X)` — block sounds
- `.strength(hardness, resistance)` — mining hardness/explosion resistance
- `.noCollission()` — no collision box (passthrough)
- `.noOcclusion()` — transparent/non-full blocks
- `.lightLevel { state -> level }` — light emission (0-15)
- `.setId(resourceKey)` — **REQUIRED in 1.21.4**

## Item Registration

```java
public static <T extends Item> T register(String name, Function<Item.Properties, T> factory,
                                           Item.Properties settings) {
    ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM,
        Identifier.fromNamespaceAndPath(MOD_ID, name));
    T item = factory.apply(settings.setId(itemKey));
    Registry.register(BuiltInRegistries.ITEM, itemKey, item);
    return item;
}

public static final Item MY_ITEM = register("my_item", Item::new, new Item.Properties());
```

### Creative Tab

```java
ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
    .register(entries -> entries.accept(ModItems.MY_ITEM));
```

## Required Resource Files

### Blockstate JSON (`assets/<modid>/blockstates/<block>.json`)
```json
{
  "variants": {
    "": { "model": "<modid>:block/<block>" }
  }
}
```

### Block Model (`assets/<modid>/models/block/<block>.json`)
```json
{
  "parent": "minecraft:block/cube_all",
  "textures": { "all": "<modid>:block/<block>" }
}
```

### Item Model for Block (`assets/<modid>/items/<block>.json`)
```json
{
  "model": {
    "type": "minecraft:model",
    "model": "<modid>:block/<block>"
  }
}
```

### Item Model for standalone Item (`assets/<modid>/items/<item>.json`)
```json
{
  "model": {
    "type": "minecraft:model",
    "model": "<modid>:item/<item>"
  }
}
```

### Loot Table (`data/<modid>/loot_table/blocks/<block>.json`)
```json
{
  "type": "minecraft:block",
  "pools": [{
    "rolls": 1,
    "entries": [{ "type": "minecraft:item", "name": "<modid>:<block>" }],
    "conditions": [{ "condition": "minecraft:survives_explosion" }]
  }]
}
```

### Mining Tags (`data/minecraft/tags/block/mineable/<tool>.json`)
```json
{
  "replace": false,
  "values": ["<modid>:<block>"]
}
```
Tool options: `shovel`, `pickaxe`, `axe`, `hoe`.
Mining level tags: `needs_stone_tool`, `needs_iron_tool`, `needs_diamond_tool`.

## Static Initialization

Classes with registered fields MUST have an `initialize()` method called from the mod entrypoint to trigger class loading:

```kotlin
object ModBlocks {
    val MY_BLOCK = register(...)

    fun initialize() {
        // Ensures static fields are evaluated
    }
}
```

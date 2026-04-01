---
name: fabric-dev
description: Fabric Mod development reference for Minecraft 1.21.4. Use when writing or modifying Fabric mod code — blocks, items, networking, HUD rendering, sounds, data attachments, effects, or registration patterns.
user-invocable: false
---

# Fabric Mod Development Reference (Minecraft 1.21.4)

This skill provides Fabric API patterns and conventions for Minecraft modding.
For detailed API reference per topic, see the supporting files:

- [blocks-and-items.md](blocks-and-items.md) — Block/Item registration, models, blockstates, loot tables
- [networking.md](networking.md) — Custom packets (S2C/C2S), payload definition, sending/receiving
- [hud-rendering.md](hud-rendering.md) — HUD overlay rendering with HudElementRegistry
- [sounds.md](sounds.md) — Custom sound events, sounds.json, OGG files
- [data-attachments.md](data-attachments.md) — AttachmentRegistry for persistent player/entity data
- [effects.md](effects.md) — Mob effects (status effects), applying effects to players
- [datagen.md](datagen.md) — Data generation setup and providers

## Key Principles

1. **Registration pattern**: All game objects (blocks, items, sounds, etc.) must be registered via `Registry.register()` with a `ResourceKey` or `Identifier`. Always call an `initialize()` method from the mod entrypoint to trigger static initialization.

2. **Identifier format**: `Identifier.fromNamespaceAndPath("modid", "name")` — never use the deprecated `Identifier("modid", "name")` constructor.

3. **ResourceKey (1.21.4)**: Blocks and items require `ResourceKey` and `.setId(key)` on their Settings/Properties. Use `ResourceKey.create(Registries.BLOCK, identifier)` or `ResourceKey.create(Registries.ITEM, identifier)`.

4. **Entrypoints**: Server/common logic in `ModInitializer`, client-only logic in `ClientModInitializer`. Registered in `fabric.mod.json` under `"entrypoints"`.

5. **Kotlin**: Use `fabric-language-kotlin` adapter. Entrypoint classes use `object` with `: ModInitializer`.

6. **Asset paths**:
   - Textures: `assets/<modid>/textures/`
   - Models: `assets/<modid>/models/`
   - Blockstates: `assets/<modid>/blockstates/`
   - Item definitions: `assets/<modid>/items/`
   - Sounds: `assets/<modid>/sounds/` + `assets/<modid>/sounds.json`
   - Lang: `assets/<modid>/lang/en_us.json`

7. **Data paths**:
   - Recipes: `data/<modid>/recipe/`
   - Loot tables: `data/<modid>/loot_table/blocks/`
   - Tags: `data/minecraft/tags/block/` (or `/item/`)

8. **Official docs**: https://docs.fabricmc.net/develop/

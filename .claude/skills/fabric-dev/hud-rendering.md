# HUD Rendering (Fabric 1.21.4)

## API: HudElementRegistry (Fabric API 0.116+)

The old `HudRenderCallback` is **deprecated**. Use `HudElementRegistry` instead.

## Registration

In **ClientModInitializer**:

```kotlin
HudElementRegistry.attachElementBefore(
    VanillaHudElements.CHAT,  // anchor element
    Identifier.fromNamespaceAndPath(MOD_ID, "my_hud"),
    ::renderMyHud
)
```

### Anchor Options (VanillaHudElements)

Common anchors for positioning:
- `VanillaHudElements.CHAT` — before chat
- `VanillaHudElements.HOTBAR` — before hotbar
- `VanillaHudElements.EXPERIENCE_BAR` — before XP bar
- Use `attachElementAfter()` to render after an anchor

## Render Function

```kotlin
fun renderMyHud(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    // Drawing operations here
}
```

## GuiGraphics Drawing Methods

### Fill (solid rectangle)
```kotlin
graphics.fill(x1, y1, x2, y2, color)  // color as ARGB int
```

### Texture
```kotlin
// Draw a texture region
graphics.blit(
    textureIdentifier,  // ResourceLocation of the texture
    x, y,               // screen position
    u, v,               // texture UV offset
    width, height,      // size on screen
    textureWidth, textureHeight  // full texture dimensions
)
```

### Text
```kotlin
val textRenderer = Minecraft.getInstance().font
graphics.drawString(textRenderer, "Hello", x, y, 0xFFFFFF)
```

### Colored Fill with Alpha
```kotlin
// ARGB color: 0xAARRGGBB
graphics.fill(x1, y1, x2, y2, 0x80FF0000)  // semi-transparent red
```

## DeltaTracker

- `deltaTracker.getGameTimeDeltaPartialTick(false)` — interpolation between ticks (for animations)
- For real-world timing, use `Util.getMillis()` instead

## Example: Drawing a Face Icon

```kotlin
private val FACE_TEXTURE = Identifier.fromNamespaceAndPath(MOD_ID, "textures/gui/face_normal.png")

fun renderUrgencyHud(graphics: GuiGraphics, deltaTracker: DeltaTracker) {
    val screenWidth = Minecraft.getInstance().window.guiScaledWidth
    val screenHeight = Minecraft.getInstance().window.guiScaledHeight

    // Draw 16x16 icon from a 16x16 texture file
    graphics.blit(FACE_TEXTURE, x, y, 0f, 0f, 16, 16, 16, 16)
}
```

## Tips

- HUD renders every frame — keep it lightweight
- Use `Minecraft.getInstance().options.hideGui` to respect F1 toggle
- Screen coordinates: (0,0) is top-left
- `guiScaledWidth` / `guiScaledHeight` give the current GUI-scaled resolution

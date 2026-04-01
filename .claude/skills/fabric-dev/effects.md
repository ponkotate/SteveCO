# Mob Effects / Status Effects (Fabric 1.21.4)

## Applying Effects to Players

```kotlin
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

// Apply Slowness I for 5 seconds (100 ticks)
val instance = MobEffectInstance(
    MobEffects.MOVEMENT_SLOWDOWN,  // effect holder
    5 * 20,                         // duration in ticks (20 ticks = 1 second)
    0,                              // amplifier (0 = level I, 1 = level II, etc.)
    false,                          // ambient (true = beacon-like, aqua particles)
    true,                           // visible particles
    true                            // show icon in HUD
)
player.addEffect(instance)
```

## MobEffectInstance Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| effect | `Holder<MobEffect>` | The effect to apply |
| duration | `Int` | Duration in ticks (20 ticks = 1 second) |
| amplifier | `Int` | Level minus 1 (0 = I, 1 = II, 2 = III) |
| ambient | `Boolean` | If true, from environmental source (lighter particles) |
| visible | `Boolean` | Whether to show particles |
| showIcon | `Boolean` | Whether to show the HUD icon |

## Common Vanilla Effects

| Effect | Field | Description |
|--------|-------|-------------|
| Slowness | `MobEffects.MOVEMENT_SLOWDOWN` | Reduces movement speed |
| Speed | `MobEffects.MOVEMENT_SPEED` | Increases movement speed |
| Nausea | `MobEffects.CONFUSION` | Screen wobble |
| Weakness | `MobEffects.WEAKNESS` | Reduces melee damage |
| Strength | `MobEffects.DAMAGE_BOOST` | Increases melee damage |

## Creating Custom Effects

```kotlin
class MyEffect : MobEffect(MobEffectCategory.HARMFUL, 0xFFFF00) {
    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean = true

    override fun applyEffectTick(level: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        // Custom tick logic
        return super.applyEffectTick(level, entity, amplifier)
    }
}

// Registration
val MY_EFFECT: Holder<MobEffect> = Registry.registerForHolder(
    BuiltInRegistries.MOB_EFFECT,
    Identifier.fromNamespaceAndPath(MOD_ID, "my_effect"),
    MyEffect()
)
```

## MobEffectCategory

- `MobEffectCategory.BENEFICIAL` — positive (green)
- `MobEffectCategory.HARMFUL` — negative (red)
- `MobEffectCategory.NEUTRAL` — neither (blue)

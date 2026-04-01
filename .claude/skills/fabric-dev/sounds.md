# Custom Sounds (Fabric 1.21.4)

## File Structure

```
assets/<modid>/
  sounds.json              # Sound event definitions
  sounds/
    my_sound.ogg           # OGG Vorbis mono audio file
```

## Audio Requirements

- Format: **OGG Vorbis** (.ogg)
- Channels: **Mono** (single channel) — stereo will not spatialize correctly
- Use Audacity or similar to convert stereo to mono and export as OGG

## sounds.json

```json
{
  "pee_release": {
    "subtitle": "sound.steveco.pee_release",
    "sounds": ["steveco:pee_release"]
  },
  "chamber_pot_use": {
    "subtitle": "sound.steveco.chamber_pot_use",
    "sounds": ["steveco:chamber_pot_use"]
  }
}
```

The `sounds` array entries reference `assets/<modid>/sounds/<name>.ogg` (without the `.ogg` extension).

## SoundEvent Registration

```kotlin
object ModSounds {
    val PEE_RELEASE: SoundEvent = registerSound("pee_release")
    val CHAMBER_POT_USE: SoundEvent = registerSound("chamber_pot_use")

    private fun registerSound(id: String): SoundEvent {
        val identifier = Identifier.fromNamespaceAndPath(MOD_ID, id)
        return Registry.register(
            BuiltInRegistries.SOUND_EVENT,
            identifier,
            SoundEvent.createVariableRangeEvent(identifier)
        )
    }

    fun initialize() {
        // triggers static init
    }
}
```

## Playing Sounds

### Server-side (heard by all nearby players)
```kotlin
world.playSound(
    null,           // player to exclude (null = no exclusion)
    blockPos,       // position
    ModSounds.PEE_RELEASE,
    SoundSource.PLAYERS,  // sound category
    1.0f,           // volume
    1.0f            // pitch
)
```

### SoundSource Categories
- `SoundSource.PLAYERS` — player-related sounds
- `SoundSource.BLOCKS` — block-related sounds
- `SoundSource.MASTER` — master volume
- `SoundSource.HOSTILE` / `NEUTRAL` — mob sounds

## Translation (for subtitles)

In `assets/<modid>/lang/en_us.json`:
```json
{
  "sound.steveco.pee_release": "Pee releases",
  "sound.steveco.chamber_pot_use": "Chamber pot used"
}
```

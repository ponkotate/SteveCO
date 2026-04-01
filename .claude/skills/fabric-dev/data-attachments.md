# Data Attachments (Fabric 1.21.4)

Attach arbitrary persistent data to Entities, Block Entities, Levels, and Chunks.

## Registration

```kotlin
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry
import net.fabricmc.fabric.api.attachment.v1.AttachmentType

// Basic (non-persistent, clears on restart)
val MY_DATA: AttachmentType<String> = AttachmentRegistry.create(
    Identifier.fromNamespaceAndPath(MOD_ID, "my_data")
)

// Persistent with default value (survives restarts, saved to NBT)
val URGENCY: AttachmentType<Int> = AttachmentRegistry.create(
    Identifier.fromNamespaceAndPath(MOD_ID, "urgency")
) { builder ->
    builder
        .initializer { 0 }           // default value
        .persistent(Codec.INT)        // serialize with Codec
        .copyOnDeath()                // preserve across death (optional)
}
```

## Builder Options

- `.initializer { defaultValue }` — provides default when first accessed
- `.persistent(codec)` — enables NBT serialization with the given Codec
- `.copyOnDeath()` — copies value when player respawns (otherwise resets)
- `.syncPredicate(predicate)` — auto-sync to clients

### Sync Predicates

- `AttachmentSyncPredicate.all()` — sync to all clients
- `AttachmentSyncPredicate.targetOnly()` — sync only to the owning player
- `AttachmentSyncPredicate.allButTarget()` — sync to everyone except owner

## Reading Data

```kotlin
val player: ServerPlayer = ...

// Check if attached
player.hasAttached(URGENCY)

// Get value (nullable)
val value: Int? = player.getAttached(URGENCY)

// Get with default fallback
val value: Int = player.getAttachedOrElse(URGENCY, 0)

// Get or set default
val value: Int = player.getAttachedOrSet(URGENCY, 0)

// Get or throw
val value: Int = player.getAttachedOrThrow(URGENCY)
```

## Writing Data

```kotlin
// Set value
player.setAttached(URGENCY, 50)

// Modify in-place
player.modifyAttached(URGENCY) { current -> current + 1 }

// Remove
player.removeAttached(URGENCY)
```

## Important Notes

- Use **immutable types** for attachment values
- Only modify via `setAttached()` / `modifyAttached()` — direct mutation won't trigger persistence/sync
- Common Codecs: `Codec.INT`, `Codec.FLOAT`, `Codec.STRING`, `Codec.BOOL`, `BlockPos.CODEC`
- Works on: `Entity`, `BlockEntity`, `Level`, `Chunk`

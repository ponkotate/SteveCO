# Networking (Fabric 1.21.4)

## Custom Packet Payload

Define packets as Records (Java) or data classes (Kotlin) implementing `CustomPacketPayload`:

```java
public record MyS2CPayload(int value) implements CustomPacketPayload {
    public static final Identifier PAYLOAD_ID =
        Identifier.fromNamespaceAndPath(MOD_ID, "my_sync");
    public static final CustomPacketPayload.Type<MyS2CPayload> ID =
        new CustomPacketPayload.Type<>(PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, MyS2CPayload> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.INT, MyS2CPayload::value,
            MyS2CPayload::new
        );

    @Override
    public Type<? extends CustomPacketPayload> type() { return ID; }
}
```

**Kotlin:**

```kotlin
data class MyS2CPayload(val value: Int) : CustomPacketPayload {
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID

    companion object {
        val PAYLOAD_ID: Identifier = Identifier.fromNamespaceAndPath(MOD_ID, "my_sync")
        val ID = CustomPacketPayload.Type<MyS2CPayload>(PAYLOAD_ID)
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, MyS2CPayload> =
            StreamCodec.composite(
                ByteBufCodecs.INT, MyS2CPayload::value,
                ::MyS2CPayload
            )
    }
}
```

## Registration

In the **common initializer** (ModInitializer):

```kotlin
// Server → Client packet
PayloadTypeRegistry.playS2C().register(MyS2CPayload.ID, MyS2CPayload.CODEC)

// Client → Server packet
PayloadTypeRegistry.playC2S().register(MyC2SPayload.ID, MyC2SPayload.CODEC)
```

## Sending Packets

```kotlin
// Server → Client (from server code)
ServerPlayNetworking.send(serverPlayer, MyS2CPayload(42))

// Client → Server (from client code)
ClientPlayNetworking.send(MyC2SPayload(data))
```

## Receiving Packets

```kotlin
// Client receives S2C (in ClientModInitializer)
ClientPlayNetworking.registerGlobalReceiver(MyS2CPayload.ID) { payload, context ->
    // Already on client thread, safe to update client state
    ClientData.value = payload.value
}

// Server receives C2S (in ModInitializer)
ServerPlayNetworking.registerGlobalReceiver(MyC2SPayload.ID) { payload, context ->
    // ALWAYS validate on server side
    val player = context.player()
    // ... handle
}
```

## StreamCodec Common Types

- `ByteBufCodecs.INT` — int
- `ByteBufCodecs.FLOAT` — float
- `ByteBufCodecs.BOOL` — boolean
- `ByteBufCodecs.STRING_UTF8` — String
- `BlockPos.STREAM_CODEC` — BlockPos
- `ByteBufCodecs.VAR_INT` — variable-length int

## Important Notes

- Always validate packet contents on the server side
- Packet handlers on the client run on the network thread by default in some versions — check if `context.player()` is available
- Register payloads before registering receivers

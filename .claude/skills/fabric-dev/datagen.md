# Data Generation (Fabric 1.21.4)

Programmatically generate JSON resources (recipes, loot tables, models, tags, etc.).

## Setup

### build.gradle.kts

```kotlin
fabricApi {
    configureDataGeneration() {
        client = true  // enable client-side datagen (models, etc.)
    }
}
```

### Entrypoint Class

```kotlin
class MyModDataGenerator : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(generator: FabricDataGenerator) {
        val pack = generator.createPack()
        // Register providers here
        pack.addProvider(::MyRecipeProvider)
        pack.addProvider(::MyModelProvider)
        pack.addProvider(::MyLootTableProvider)
        pack.addProvider(::MyBlockTagProvider)
    }
}
```

### fabric.mod.json

```json
{
  "entrypoints": {
    "fabric-datagen": [
      "com.mymod.datagen.MyModDataGenerator"
    ]
  }
}
```

## Running

```bash
./gradlew runDatagen
```

Generated files output to `src/main/generated/`.

## Common Providers

### Recipe Provider

```kotlin
class MyRecipeProvider(output: FabricDataOutput, registries: CompletableFuture<HolderLookup.Provider>)
    : FabricRecipeProvider(output, registries) {

    override fun buildRecipes(exporter: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CHAMBER_POT)
            .pattern("I I")
            .pattern("III")
            .define('I', Items.IRON_INGOT)
            .unlockedBy("has_iron", has(Items.IRON_INGOT))
            .save(exporter)
    }
}
```

### Block Loot Table Provider

```kotlin
class MyLootTableProvider(output: FabricDataOutput, registries: CompletableFuture<HolderLookup.Provider>)
    : FabricBlockLootTableProvider(output, registries) {

    override fun generate() {
        dropSelf(ModBlocks.CHAMBER_POT)
        // For blocks that drop nothing:
        // dropNothing(ModBlocks.PEE_BLOCK)
    }
}
```

### Tag Provider

```kotlin
class MyBlockTagProvider(output: FabricDataOutput, registries: CompletableFuture<HolderLookup.Provider>)
    : FabricTagProvider.BlockTagProvider(output, registries) {

    override fun addTags(lookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .add(ModBlocks.CHAMBER_POT)
    }
}
```

### Model Provider

```kotlin
class MyModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(generator: BlockModelGenerators) {
        generator.createTrivialCube(ModBlocks.MY_BLOCK)
    }

    override fun generateItemModels(generator: ItemModelGenerators) {
        generator.generateFlatItem(ModItems.MY_ITEM, ModelTemplates.FLAT_ITEM)
    }
}
```

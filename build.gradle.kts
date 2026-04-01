plugins {
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
    id("org.jetbrains.kotlin.jvm") version "2.3.20"
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String
base.archivesName.set(project.property("archives_base_name") as String)

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
    implementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric_kotlin_version")}")
}

tasks.processResources {
    inputs.property("version", project.property("mod_version"))
    filesMatching("fabric.mod.json") {
        expand(mapOf("version" to project.property("mod_version")))
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    withSourcesJar()
}

kotlin {
    jvmToolchain(25)
}

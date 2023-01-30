import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("dev.s7a.gradle.minecraft.server") version "2.1.0"
}

group = "com.eternalcode"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.eternalcode.pl/releases") }
}

dependencies {
    // spigot api
    compileOnly("org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")

    // kyori adventure
    implementation("net.kyori:adventure-platform-bukkit:4.2.0")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")

    // litecommands
    implementation("dev.rollczi.litecommands:bukkit:2.8.2")
    implementation("dev.rollczi.litecommands:core:2.8.2")

    // cdn
    implementation("net.dzikoysk:cdn:1.14.3")

    // bstats
    implementation("org.bstats:bstats-bukkit:3.0.0")

    // triumph gui
    implementation("dev.triumphteam:triumph-gui:3.1.4")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

bukkit {
    main = "com.eternalcode.friends.EternalFriends"
    apiVersion = "1.13"
    name = "EternalFriends"
    prefix = "EternalFriends"
    author = "Kamicjusz"
    version = "${project.version}"
    description = "EternalFriends is a plugin that allows you to manage your friends list."
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<ShadowJar> {
    archiveFileName.set("EternalFriends v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "org/checkerframework/**",
        "META-INF/**",
        "javax/**",
    )

    mergeServiceFiles()
    minimize()

    val prefix = "com.eternalcode.friends.libs"

    listOf(
        "panda",
        "org.panda_lang",
        "org.bstats",
        "net.dzikoysk",
        "net.kyori",
        "dev.rollczi",
        "dev.triumphteam",
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}

task<LaunchMinecraftServerTask>("buildAndLaunchServer") {
    dependsOn("shadowJar") // build task (build, jar, shadowJar, ...)
    doFirst {
        copy {
            from(buildDir.resolve("libs/EternalFriends v${project.version}.jar")) // build/libs/example.jar
            into(buildDir.resolve("MinecraftPaperServer/plugins")) // build/MinecraftPaperServer/plugins
        }
    }

    jarUrl.set(LaunchMinecraftServerTask.JarUrl.Paper("1.19.3"))
    jarName.set("server.jar")
    serverDirectory.set(buildDir.resolve("MinecraftPaperServer")) // build/MinecraftPaperServer
    nogui.set(true)
    agreeEula.set(true)
}
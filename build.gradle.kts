import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default;

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
    id("xyz.jpenilla.run-paper") version "2.1.0"
}

group = "com.eternalcode"
version = "1.0.0"

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()

    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
}

dependencies {
    // spigot api
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")

    // kyori adventure
    implementation("net.kyori:adventure-platform-bukkit:4.3.0")
    implementation("net.kyori:adventure-text-minimessage:4.13.1")

    // litecommands
    implementation("dev.rollczi.litecommands:bukkit-adventure:2.8.7")

    // cdn
    implementation("net.dzikoysk:cdn:1.14.4")

    // bstats
    implementation("org.bstats:bstats-bukkit:3.0.2")

    // triumph gui
    implementation("dev.triumphteam:triumph-gui:3.1.4")

    // HikariCP
    implementation("com.zaxxer:HikariCP:5.0.1")

    // MySQL
    implementation("com.mysql:mysql-connector-j:8.0.33")

    // protocollib
    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0-SNAPSHOT")
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
    depend = listOf("ProtocolLib")
    description = "EternalFriends is a plugin that allows you to manage your friends list."
    permissions {
        register("eternalfriends.access.*") {
            children = listOf(
                "eternalfriends.access.accept",
                "eternalfriends.access.deny",
                "eternalfriends.access.help",
                "eternalfriends.access.ignore",
                "eternalfriends.access.invite",
                "eternalfriends.access.kick",
                "eternalfriends.access.list"
            )
            default = Default.OP
        }
        register("eternalfriends.admin.*") {
            children = listOf(
                "eternalfriends.admin.list",
                "eternalfriends.admin.reload"
            )
            default = Default.OP
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<ShadowJar> {
    archiveFileName.set("EternalFriends v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
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

tasks {
    runServer {
        minecraftVersion("1.19.3")
    }
}
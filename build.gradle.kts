import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default

plugins {
    `java-library`
    checkstyle

    id("org.openrewrite.rewrite") version("6.5.6")

    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.2.2"
}

group = "com.eternalcode"
version = "1.0.0"

rewrite {
    activeRecipe("org.openrewrite.staticanalysis.CodeCleanup")
}

repositories {
    gradlePluginPortal()
    mavenCentral()

    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
    maven { url = uri("https://repo.panda-lang.org/releases") }
    maven { url = uri("https://repo.dmulloy2.net/repository/public/") }
    maven { url = uri("https://storehouse.okaeri.eu/repository/maven-public/") }
}

checkstyle {
    toolVersion = "10.12.4"

    configFile = file("${rootDir}/checkstyle/checkstyle.xml")
    configProperties["checkstyle.suppressions.file"] = "${rootDir}/checkstyle/suppressions.xml"

    maxErrors = 0
    maxWarnings = 0
}

dependencies {
    // spigot api
    compileOnly("org.spigotmc:spigot-api:1.20.2-R0.1-SNAPSHOT")

    // kyori adventure
    implementation("net.kyori:adventure-platform-bukkit:4.3.1")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")

    // litecommands
    implementation("dev.rollczi.litecommands:bukkit-adventure:2.8.9")

    // okaeri configs
    val okaeriConfigsVersion = "5.0.0-beta.5"
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:${okaeriConfigsVersion}")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:${okaeriConfigsVersion}")

    // bstats
    implementation("org.bstats:bstats-bukkit:3.0.2")

    // triumph gui
    implementation("dev.triumphteam:triumph-gui:3.1.7")

    // Database
    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("com.mysql:mysql-connector-j:8.2.0")

    // ProtocolLib for managing friendship nametag's
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")

    rewrite("org.openrewrite.recipe:rewrite-static-analysis:1.1.0")
}

tasks.compileJava {
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
        register("eternalfriends.access.all") {
            children = listOf(
                "eternalfriends.access.gui",
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
        register("eternalfriends.admin.all") {
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

tasks.shadowJar {
    archiveFileName.set("EternalFriends v${project.version}.jar")

    exclude(
        "org/intellij/lang/annotations/**",
        "org/jetbrains/annotations/**",
        "META-INF/**",
        "javax/**",
    )

    dependsOn("rewriteRun")
    dependsOn("test")

    mergeServiceFiles()

    val prefix = "com.eternalcode.friends.libs"

    listOf(
        "panda",
        "org.panda_lang",
        "org.bstats",
        "net.dzikoysk",
        "net.kyori",
        "dev.rollczi",
        "dev.triumphteam",
    ).forEach { relocate(it, prefix) }
}

tasks {
    runServer {
        minecraftVersion("1.20.1")

        downloadPlugins {
            hangar("protocolib", "5.1.0")
        }
    }
}
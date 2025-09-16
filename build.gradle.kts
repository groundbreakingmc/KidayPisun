import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.8"
    // https://github.com/Minecrell/plugin-yml
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "com.github.groundbreakingmc"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral() // Lombok
    maven("https://repo.papermc.io/repository/maven-public/") // Paper
    maven("https://jitpack.io/") // MyLib, BStats
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // https://github.com/groundbreakingmc/MyLib
    if (project.hasProperty("shadeMyLib")) {
        implementation("com.github.groundbreakingmc:MyLib:e49fbe23e8") {
            isTransitive = false
        }
    } else {
        compileOnly("com.github.groundbreakingmc:MyLib:e49fbe23e8")
    }

    // https://github.com/SpongePowered/Configurate
    compileOnly("org.spongepowered:configurate-yaml:4.2.0")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly("org.projectlombok:lombok:1.18.40")
    annotationProcessor("org.projectlombok:lombok:1.18.40")

    // https://mvnrepository.com/artifact/org.bstats/bstats-bukkit
    implementation("org.bstats:bstats-bukkit:3.1.0")

    // https://github.com/mfnalex/CustomBlockData
    implementation("com.jeff-media:custom-block-data:2.2.3")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

bukkit {
    if (project.hasProperty("ruSpigotPlugin")) {
        name = "TowerBuilder"
    }

    main = "com.github.groundbreakingmc.kidaypisun.KidayPisun"
    apiVersion = "1.13"

    author = "GroundbreakingMC"
    website = "https//github.com/groundbreakingmc/MyLib"

    if (project.hasProperty("shadeMyLib")) {
        libraries = listOf("org.spongepowered:configurate-yaml:4.2.0")
    } else {
        depend = listOf("MyLib")
    }

    commands {
        register("pisun") {
            permission = "kidaypisun.use"
            aliases = listOf("spampisun")
        }
    }

    permissions {
        register("kidaypisun.use") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<Javadoc> {
        options.encoding = "UTF-8"
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        if (project.hasProperty("ruSpigotPlugin")) {
            archiveFileName.set("TowerBuilder-${version}.jar")
        }
        archiveClassifier.set("") // removes "-all"

        relocate("org.bstats", "com.github.groundbreakingmc.kidaypisun.bstats.metrics")
        relocate("com.jeff_media.customblockdata", "com.github.groundbreakingmc.kidaypisun.customblockdata")
        if (project.hasProperty("shadeMyLib")) {
            relocate("com.github.groundbreakingmc.mylib", "com.github.groundbreakingmc.kidaypisun.mylib")
        }

        minimize()
    }
}

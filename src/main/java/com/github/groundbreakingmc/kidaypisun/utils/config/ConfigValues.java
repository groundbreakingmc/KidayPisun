package com.github.groundbreakingmc.kidaypisun.utils.config;

import com.github.groundbreakingmc.kidaypisun.KidayPisun;
import com.github.groundbreakingmc.mylib.config.loaders.Loaders;
import com.github.groundbreakingmc.mylib.logger.console.LoggerFactory;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public final class ConfigValues {

    private int resetTime;
    private Dick defaultDick;
    private Map<String, Dick> dicks;

    @Getter(AccessLevel.NONE)
    private final KidayPisun plugin;

    public ConfigValues(final KidayPisun plugin) {
        this.plugin = plugin;
    }

    public void setupValues() {
        final ConfigurationNode config = Loaders.YAML.loader(this.plugin, LoggerFactory.createLogger(this.plugin))
                .fileName("config.yml")
                .load();

        this.resetTime = config.node("reset-in").getInt();

        final ConfigurationNode defaultDickSection = config.node("default-dick");
        this.defaultDick = this.getDick(defaultDickSection);

        final HashMap<String, Dick> tempMap = new HashMap<>();

        final ConfigurationNode dickSection = config.node("dicks");
        for (final Map.Entry<Object, ? extends ConfigurationNode> entry : dickSection.childrenMap().entrySet()) {
            final String key = String.valueOf(entry.getKey());
            if (key.equalsIgnoreCase("reload")) {
                this.plugin.getLogger().warning("Cannot create dick with the key \"reload\"!");
                continue;
            }

            final ConfigurationNode keySection = dickSection.node(key);
            final Dick dick = this.getDick(keySection);
            if (dick.length < 1) {
                this.plugin.getLogger().warning("Cannot create dick with the length less then 1!");
                continue;
            }

            tempMap.put(key, dick);
        }

        this.dicks = ImmutableMap.copyOf(tempMap);
    }

    private Dick getDick(final ConfigurationNode section) {
        return new Dick(
                this.getBlockData(section, true),
                this.getBlockData(section, false),
                this.defaultDick == null
                        ? section.node("length").getInt()
                        : section.node("length", this.defaultDick.length).getInt()
        );
    }

    private BlockData getBlockData(final ConfigurationNode section, final boolean head) {
        String materialName = section.node(head ? "head-material" : "body-material").getString();

        if (materialName == null) {
            materialName = head
                    ? this.defaultDick.headBlockData.getMaterial().name()
                    : this.defaultDick.bodyBlockData.getMaterial().name();
        }

        return Objects.requireNonNull(Material.getMaterial(materialName.toUpperCase()), "Specified unknown material: " + materialName)
                .createBlockData();
    }

    public record Dick(
            BlockData headBlockData,
            BlockData bodyBlockData,
            int length
    ) {
    }
}

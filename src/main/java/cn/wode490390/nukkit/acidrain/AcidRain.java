package cn.wode490390.nukkit.acidrain;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.potion.Effect;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.BlockIterator;
import cn.nukkit.utils.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AcidRain extends PluginBase {

    private static final String PERMISSION_ACIDRAIN_IMMUNE = "acidrain.immune";

    private static final String CONFIG_DELAY = "delay";
    private static final String CONFIG_WORLD = "world";
    private static final String CONFIG_IMMUNE_ARMOR = "immune-armor";
    private static final String CONFIG_EFFECTS = "effects";

    private static final String CONFIG_EFFECT_ID = "id";
    private static final String CONFIG_EFFECT_DURATION = "duration";
    private static final String CONFIG_EFFECT_AMPLIFIER = "amplifier";

    private List<String> worlds;
    private List<Integer> armors;
    private List<Effect> effects = new ArrayList<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        Config config = this.getConfig();
        String node = CONFIG_DELAY;
        int delay;
        try {
            delay = config.getInt(node) * 20;
        } catch (Exception e) {
            delay = 5 * 20;
            this.logLoadException(node);
        }
        node = CONFIG_WORLD;
        try {
            this.worlds = config.getStringList(node);
        } catch (Exception e) {
            this.worlds = new ArrayList<>();
            this.logLoadException(node);
        }
        node = CONFIG_IMMUNE_ARMOR;
        try {
            this.armors = config.getIntegerList(node);
        } catch (Exception e) {
            this.armors = new ArrayList<>();
            this.logLoadException(node);
        }
        List<Map> list;
        node = CONFIG_EFFECTS;
        try {
            list = config.getMapList(node);
        } catch (Exception e) {
            list = new ArrayList<>();
            this.logLoadException(node);
        }
        for (Map map : list) {
            Effect effect;
            String cfg = CONFIG_EFFECT_ID;
            try {
                effect = Effect.getEffect((int) map.get(cfg));
            } catch (Exception e) {
                this.logLoadException(node + "." + cfg);
                continue;
            }
            cfg = CONFIG_EFFECT_DURATION;
            try {
                effect.setDuration((int) map.get(cfg) * 20);
            } catch (Exception ignore) {

            }
            cfg = CONFIG_EFFECT_AMPLIFIER;
            try {
                effect.setAmplifier((int) map.get(cfg));
            } catch (Exception ignore) {

            }
            this.effects.add(effect);
        }

        new NukkitRunnable() {
            @Override
            public void run() {
                for (Level level : getServer().getLevels().values()) {
                    if (worlds.contains(level.getName())) {
                        player:
                        for (Player player : level.getPlayers().values()) {
                            boolean hit = false;
                            int x = player.getFloorX();
                            int y = player.getFloorY();
                            int z = player.getFloorZ();
                            int biome = level.getBiomeId(x, z);
                            int top = y + 2;
                            List<Integer> armor = new ArrayList<>();
                            for (Item item : player.getInventory().getArmorContents()) {
                                armor.add(item.getId());
                            }
                            armor.retainAll(armors);
                            if (level.isRaining() && player.isSurvival() && armor.isEmpty() && !player.hasPermission(PERMISSION_ACIDRAIN_IMMUNE) && y >= 0 && top <= 255 && biome != EnumBiome.DESERT.id && biome != EnumBiome.HELL.id && biome != 9 && biome != EnumBiome.SAVANNA.id && biome != EnumBiome.SAVANNA_PLATEAU.id && biome != EnumBiome.MESA.id && biome != EnumBiome.MESA_PLATEAU_F.id && biome != EnumBiome.MESA_PLATEAU.id && biome != 40 && biome != 41 && biome != 42 && biome != 43 && biome != EnumBiome.DESERT_M.id && biome != EnumBiome.SAVANNA_M.id && biome != EnumBiome.SAVANNA_PLATEAU_M.id && biome != EnumBiome.MESA_BRYCE.id && biome != EnumBiome.MESA_PLATEAU_F_M.id && biome != EnumBiome.MESA_PLATEAU_M.id) {
                                BlockIterator iterator = new BlockIterator(level, new Vector3(x, top, z), new Vector3(x, 255, z));
                                for (int i = top; i <= 255; i++) {
                                    Block block = iterator.next();
                                    if (block.getId() != Block.AIR) {
                                        continue player;
                                    } else if (i < 255) {
                                        continue;
                                    }
                                    hit = true;
                                }
                            } else if (top > 255) {
                                hit = true;
                            }
                            if (hit) {
                                for (Effect effect : effects) {
                                    player.addEffect(effect.clone());
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, delay);
        new MetricsLite(this);
    }

    private void logLoadException(String text) {
        this.getLogger().alert("An error occurred while reading the configuration '" + text + "'. Use the default value.");
    }
}

package ondamageeffect;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ModConfig {

    private static final String CAT_GENERAL = "general";
    private static final String CAT_EFFECT  = "effect";
    private static final String CAT_MODULES  = "modules";

    public static boolean ENABLE_ON_DAMAGE_TAKEN;
    public static boolean ENABLE_ON_DAMAGE_DONE;
    public static boolean ENABLE_FALL_DAMAGE;
    public static boolean ONLY_HOSTILE_TARGETS;
    public static boolean ENABLE_PLAYER_TARGETS;
    public static List<PotionEffect> EFFECT_LIST = new ArrayList<>();

    private static Configuration cfg;

    static void load(File file) {
        cfg = new Configuration(file);
        sync();
    }

    @SubscribeEvent
    public void onCfgChange(ConfigChangedEvent.OnConfigChangedEvent evt) {
        if (evt.getModID().equals(HitEffectMod.MODID)) sync();
    }

    private static void sync() {

        ENABLE_ON_DAMAGE_TAKEN  = cfg.getBoolean("enableOnDamageTaken",
        		CAT_MODULES, true,
                "Apply effects when the player takes damage");

        ENABLE_ON_DAMAGE_DONE   = cfg.getBoolean("enableOnDamageDone",
        		CAT_MODULES, true,
                "Apply effects when the player deals damage");

        ENABLE_FALL_DAMAGE      = cfg.getBoolean("enableFallDamage",
                CAT_GENERAL, false,
                "If false, fall / void damage taken will not trigger effects");

        ONLY_HOSTILE_TARGETS    = cfg.getBoolean("onlyHostileTargets",
                CAT_GENERAL, true,
                "When dealing damage, trigger only if the target is neutral or hostile");

        ENABLE_PLAYER_TARGETS   = cfg.getBoolean("enablePlayerTargets",
                CAT_GENERAL, false,
                "If true, damaging another player also triggers the effect");

        boolean useSeconds      = cfg.getBoolean("useSeconds",
                CAT_GENERAL, true,
                "If true, the duration value is interpreted as seconds (converted to ticks by multiplying by 20)");

        String[] raw = cfg.getStringList(
                "effects",
                CAT_EFFECT,
                new String[]{""},
                "List of potion effects in the form <id>,<durationTicks>,<level>");
        
        EFFECT_LIST.clear();

        for (String line : raw) {
            String[] parts = line.replace(" ", "").split(",");
            if (parts.length != 3) {
            	LogManager.getLogger().warn("Skipping bad entry, it should be <id>,<durationTicks>,<level>", HitEffectMod.MODID);
            	continue;
            }

            Potion pot = ForgeRegistries.POTIONS.getValue(
                             new ResourceLocation(parts[0]));
            if (pot == null) {
            	LogManager.getLogger().warn("Skipping bad entry, potion effect should not be null", HitEffectMod.MODID);
            	continue;
            }

            try {
                int durRaw = Integer.parseInt(parts[1]);
                int amplifier = Integer.parseInt(parts[2]);
                int dur = useSeconds ? durRaw * 20 : durRaw;
                if (dur > 0 && amplifier >= 0)
                    EFFECT_LIST.add(new PotionEffect(pot, dur, amplifier - 1));
            } catch (NumberFormatException ignored) { }
        }
        

        if (cfg.hasChanged()) cfg.save();
    }
}

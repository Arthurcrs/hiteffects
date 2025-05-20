package ondamageeffect;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

import java.io.File;

public class ModConfig {
    private static Configuration cfg;

    public static Potion EFFECT;
    public static int    DURATION;
    public static int    AMPLIFIER;

    static void load(File file) {
        cfg = new Configuration(file);
        sync();
    }

    @SubscribeEvent
    public void onCfgChange(ConfigChangedEvent.OnConfigChangedEvent evt) {
        if (evt.getModID().equals(HitEffectMod.MODID)) sync();
    }

    private static void sync() {
        final String cat = "hit_effect";
        String  rl = cfg.get(cat, "effect", "minecraft:speed").getString();
        int  dur  = cfg.get(cat, "duration", 200).getInt();
        int  amp  = cfg.get(cat, "amplifier", 0).getInt();

        EFFECT    = ForgeRegistries.POTIONS.getValue(new ResourceLocation(rl));
        DURATION  = dur;
        AMPLIFIER = amp;

        if (cfg.hasChanged()) cfg.save();
    }
}

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
    public static int    LEVEL;
    public static boolean ENABLE_ON_DAMAGE_TAKEN;
    public static boolean ENABLE_ON_DAMAGE_DONE;
    public static boolean ENABLE_WITH_FALL_DAMAGE;
    

    static void load(File file) {
        cfg = new Configuration(file);
        sync();
    }

    @SubscribeEvent
    public void onCfgChange(ConfigChangedEvent.OnConfigChangedEvent evt) {
        if (evt.getModID().equals(HitEffectMod.MODID)) sync();
    }

    private static void sync() {
    	boolean enableWithFallDamage = cfg.getBoolean("enableWithFallDamage", "General", false, "Apply damage taken effects when the player takes fall damage?");
    	boolean enableOnDamageTaken = cfg.getBoolean("enableOnDamageTaken", "General", true, "Apply effects when the player takes damage?");
    	boolean enableOnDamageDone = cfg.getBoolean("enableOnDamageDone", "General", true, "Apply effects when the player deals damage?");
    	
        String  effectId = cfg.get("effect", "Id", "").getString();
        int  duration  = cfg.get("effect", "Duration in ticks", 200).getInt();
        int  level  = cfg.get("effect", "Level", 1).getInt() - 1;
        
        EFFECT    = ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectId));
        DURATION  = duration;
        LEVEL = level;
        ENABLE_ON_DAMAGE_TAKEN = enableOnDamageTaken;
        ENABLE_ON_DAMAGE_DONE = enableOnDamageDone;
        ENABLE_WITH_FALL_DAMAGE = enableWithFallDamage;

        if (cfg.hasChanged()) cfg.save();
    }
}

package ondamageeffect;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = HitEffectsMod.MODID,
     name = HitEffectsMod.NAME,
     version = HitEffectsMod.VERSION,
     acceptedMinecraftVersions = "[1.12.2]")
public class HitEffectsMod {
    public static final String MODID  = "hiteffects";
    public static final String NAME   = "Hit Effects";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        ModConfig.load(e.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new DamageEffectHandler());
    }
}
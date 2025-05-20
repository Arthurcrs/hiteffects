package ondamageeffect;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = HitEffectMod.MODID,
     name = HitEffectMod.NAME,
     version = HitEffectMod.VERSION,
     acceptedMinecraftVersions = "[1.12,1.12.2]")
public class HitEffectMod {
    public static final String MODID  = "ondamageeffect";
    public static final String NAME   = "On-Damage Effect";
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
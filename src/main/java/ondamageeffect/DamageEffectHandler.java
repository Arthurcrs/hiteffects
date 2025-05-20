package ondamageeffect;

import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageEffectHandler {

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent evt) {
        if (!(evt.getEntityLiving() instanceof EntityPlayer)) return;
        if (evt.getEntityLiving().world.isRemote) return;                    // server only

        DamageSource src = evt.getSource();
        if (src == DamageSource.FALL || src == DamageSource.OUT_OF_WORLD) return;

        EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
        if (ModConfig.EFFECT == null) return;

        player.removePotionEffect(ModConfig.EFFECT);                         // refresh, don't stack
        player.addPotionEffect(new PotionEffect(
                ModConfig.EFFECT,
                ModConfig.DURATION,
                ModConfig.AMPLIFIER));
    }
}
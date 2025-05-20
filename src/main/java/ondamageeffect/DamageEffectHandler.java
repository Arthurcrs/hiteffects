package ondamageeffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageEffectHandler {

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent evt) {
        if (!ModConfig.ENABLE_ON_DAMAGE_TAKEN) return;
        if (!(evt.getEntityLiving() instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
        if (player.world.isRemote) return;

        DamageSource src = evt.getSource();
        if (!ModConfig.ENABLE_WITH_FALL_DAMAGE &&
            (src == DamageSource.FALL || src == DamageSource.OUT_OF_WORLD)) return;

        applyEffects(player);
    }

    @SubscribeEvent
    public void onPlayerDealsDamage(LivingHurtEvent evt) {
        if (!ModConfig.ENABLE_ON_DAMAGE_DONE) return;

        Entity trueSrc = evt.getSource().getTrueSource();
        if (!(trueSrc instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) trueSrc;
        if (player.world.isRemote) return;

        applyEffects(player);
    }

    private void applyEffects(EntityPlayer player) {
        if (ModConfig.EFFECT == null) return;

        player.removePotionEffect(ModConfig.EFFECT);
        player.addPotionEffect(new PotionEffect(
                ModConfig.EFFECT,
                ModConfig.DURATION,
                ModConfig.LEVEL));
    }
}
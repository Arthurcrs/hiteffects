package ondamageeffect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.monster.IMob;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DamageEffectHandler {

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent evt) {
        if (!ModConfig.ENABLE_ON_DAMAGE_TAKEN) return;
        if (!(evt.getEntityLiving() instanceof EntityPlayer)) return;

        DamageSource src = evt.getSource();
        if (!ModConfig.ENABLE_FALL_DAMAGE &&
            (src == DamageSource.FALL || src == DamageSource.OUT_OF_WORLD))
            return;
        
        EntityPlayer player = (EntityPlayer) evt.getEntityLiving();
        if (player.world.isRemote) return;

        applyEffects(player);
    }

    @SubscribeEvent
    public void onPlayerDealsDamage(LivingHurtEvent evt) {
        if (!ModConfig.ENABLE_ON_DAMAGE_DONE) return;

        Entity trueSrc = evt.getSource().getTrueSource();
        if (!(trueSrc instanceof EntityPlayer)) return;

        Entity victim = evt.getEntityLiving();

        if (ModConfig.ONLY_HOSTILE_TARGETS) {
            boolean victimIsHostile = victim instanceof IMob;
            boolean victimIsPlayer  = victim instanceof EntityPlayer;

            if (!victimIsHostile &&
                !(ModConfig.ENABLE_PLAYER_TARGETS && victimIsPlayer))
                return;
        }

        EntityPlayer attacker = (EntityPlayer) trueSrc;
        if (attacker.world.isRemote) return;

        applyEffects(attacker);
    }

    private void applyEffects(EntityPlayer player) {
        if (ModConfig.EFFECT_LIST.isEmpty()) return;

        for (PotionEffect pe : ModConfig.EFFECT_LIST) {
            player.removePotionEffect(pe.getPotion());
            player.addPotionEffect(new PotionEffect(
                    pe.getPotion(), pe.getDuration(), pe.getAmplifier()));
        }
    }
}

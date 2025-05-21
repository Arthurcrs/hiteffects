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
	private static final String CAT_EFFECT = "effects";
	private static final String CAT_MODULES = "modules";

	public static boolean ENABLE_ON_DAMAGE_TAKEN;
	public static boolean ENABLE_ON_DAMAGE_DONE;
	public static boolean ENABLE_FALL_DAMAGE;
	public static boolean ONLY_HOSTILE_TARGETS;
	public static boolean ENABLE_PLAYER_TARGETS;
	public static boolean ENABLE_TAKEN_FROM_PLAYER;
	public static List<PotionEffect> ON_TAKING_DAMAGE_EFFECT_LIST = new ArrayList<>();
	public static List<PotionEffect> ON_DEALING_DAMAGE_EFFECT_LIST = new ArrayList<>();

	private static Configuration cfg;

	static void load(File file) {
		cfg = new Configuration(file);
		sync();
	}

	@SubscribeEvent
	public void onCfgChange(ConfigChangedEvent.OnConfigChangedEvent evt) {
		if (evt.getModID().equals(HitEffectsMod.MODID))
			sync();
	}

	private static void sync() {

		ENABLE_ON_DAMAGE_TAKEN = cfg.getBoolean("enableOnDamageTaken", CAT_MODULES, true,
				"Apply effects when the player takes damage");

		ENABLE_ON_DAMAGE_DONE = cfg.getBoolean("enableOnDamageDone", CAT_MODULES, true,
				"Apply effects when the player deals damage");

		ENABLE_FALL_DAMAGE = cfg.getBoolean("enableFallDamage", CAT_GENERAL, false,
				"If false, fall / void damage taken will not trigger effects");

		ONLY_HOSTILE_TARGETS = cfg.getBoolean("onlyHostileTargets", CAT_GENERAL, true,
				"When dealing damage, trigger only if the target is neutral or hostile");

		ENABLE_PLAYER_TARGETS = cfg.getBoolean("enableWhenDamagingOtherPlayers", CAT_GENERAL, false,
				"If true, damaging another player will triggers effects");

		ENABLE_TAKEN_FROM_PLAYER = cfg.getBoolean("enableWhenTakingDamageFromPlayers", CAT_GENERAL, false,
				"If true, taking damage from another player will trigger effects");

		boolean useSeconds = cfg.getBoolean("useSeconds", CAT_GENERAL, true,
				"If true, the duration value is interpreted as seconds (converted to ticks by multiplying by 20)");

		String[] onTakingDamageEffectList = cfg.getStringList("effectsOnTakingDamage", CAT_EFFECT, new String[] { "" },
				"List of potion effects applied to the player when damage is taken <id>,<duration>,<level>");

		String[] onDealingDamageEffectList = cfg.getStringList("effectsOnDealingDamage", CAT_EFFECT,
				new String[] { "" },
				"List of potion effects applied to the player when damage is dealt <id>,<duration>,<level>");

		ON_TAKING_DAMAGE_EFFECT_LIST = parseEffectList(onTakingDamageEffectList, useSeconds);
		ON_DEALING_DAMAGE_EFFECT_LIST = parseEffectList(onDealingDamageEffectList, useSeconds);

		if (cfg.hasChanged())
			cfg.save();
	}

	private static List<PotionEffect> parseEffectList(String[] raw, boolean useSeconds) {
		List<PotionEffect> out = new ArrayList<>();

		for (String line : raw) {
			String[] p = line.replace(" ", "").split(",");
			if (p.length != 3) {
				LogManager.getLogger().warn("[{}] bad entry '{}'; expected <id>,<dur>,<lvl>", HitEffectsMod.MODID,
						line);
				continue;
			}

			Potion pot = ForgeRegistries.POTIONS.getValue(new ResourceLocation(p[0]));
			if (pot == null) {
				LogManager.getLogger().warn("[{}] unknown potion '{}'", HitEffectsMod.MODID, p[0]);
				continue;
			}

			try {
				int dur = Integer.parseInt(p[1]);
				int amplifier = Integer.parseInt(p[2]) - 1;
				dur = useSeconds ? dur * 20 : dur;

				if (dur > 0 && amplifier >= 0)
					out.add(new PotionEffect(pot, dur, amplifier));
			} catch (NumberFormatException ignored) {
			}
		}
		return out;
	}

}

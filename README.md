### What is this mod?

This mod allows you to set potion effects to be applied to the player when taking or dealing damage. The duration and level of each effect can be configured, and you can assign different effects for damage taken versus damage dealt. Additional configurations let you apply effects only when dealing damage to hostile entities or disable them for player-versus-player interactions.

<span style="color: #236fa1;">This is my first Forge Mod. It is for minecraft 1.12.2 only. If you encounter bugs or have suggestions, please leave a comment.</span>

### Example on how to set up the effects in the config:

Note that the duration is set in seconds (to get the number of ticks multiply it by 20), there is also an option in the config to use ticks instead of seconds.

```
effects {
    # List of potion effects applied to the player when damage is dealt <id>,<duration>,<level> [default: []]
    S:effectsOnDealingDamage <
        minecraft:regeneration, 5, 1
        minecraft:strength, 10, 1
     >

    # List of potion effects applied to the player when damage is taken <id>,<duration>,<level> [default: []]
    S:effectsOnTakingDamage <
        minecraft:strength, 10, 1
        minecraft:wither, 20, 1
     >
}
```


### Configurations


```
general {
    # If false, fall / void damage taken will not trigger effects [default: false]
    B:enableFallDamage=false

    # If true, damaging another player will triggers effects [default: false]
    B:enableWhenDamagingOtherPlayers=false

    # If true, taking damage from another player will trigger effects [default: false]
    B:enableWhenTakingDamageFromPlayers=false

    # When dealing damage, trigger only if the target is neutral or hostile [default: true]
    B:onlyHostileTargets=true

    # If true, the duration value is interpreted as seconds (converted to ticks by multiplying by 20) [default: true]
    B:useSeconds=true
}
```


### Where do I get effect ids?


There are other ways, but I would recomment the mod [TellMe](https://www.curseforge.com/minecraft/mc-mods/tellme) and using the command "/tellme dump-csv potions".
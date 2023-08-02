package com.talons.playershops.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class PlayerShopsCommonConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Boolean> UseAllowance;
    public static final ForgeConfigSpec.ConfigValue<String> AllowanceItem;
    public static final ForgeConfigSpec.ConfigValue<Integer> AllowanceItemCount;
    public static final ForgeConfigSpec.ConfigValue<Long> AllowanceTimeOfDay;
    public static final ForgeConfigSpec.ConfigValue<String> AllowanceMessage;
    public static final ForgeConfigSpec.ConfigValue<String> AllowanceFailMessage;

    static {
        BUILDER.push("Common configs for Player Shops");

        UseAllowance = BUILDER.comment("whether or not to use the allowance system")
                .define("UseAllowance", false);
        AllowanceItem = BUILDER.comment("The id of the item given as an allowance")
                .define("AllowanceItem", "minecraft:raw_gold");
        AllowanceItemCount = BUILDER.comment("The number of items players get given")
                .define("AllowanceItemCount", 1);
        AllowanceTimeOfDay = BUILDER.comment("The time in ticks between when a player is given there allowance, one day is 24000 ticks, max is one day currently")
                .define("AllowanceTime", (long)24000);
        AllowanceMessage = BUILDER.comment("The message displayed to players when getting their allowance")
                .define("AllowanceMessage", "The sun rises and you gain your allowance");
        AllowanceFailMessage = BUILDER.comment("The message displayed to players when they cant get their allowance, probably due to full inventory")
                .define("AllowanceFailMessage", "The sun rises but your inventory was full");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}

package com.snugz.trulytreasures.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;
import java.util.List;

public class TrulyTreasuresConfig {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec config;
    public static final ForgeConfigSpec.IntValue basePrice;
    public static final ForgeConfigSpec.IntValue maxTrades;
    public static final ForgeConfigSpec.BooleanValue sellCurses;
    public static final ForgeConfigSpec.ConfigValue<List<String>> villagerEnchantmentExceptions;
    public static final ForgeConfigSpec.ConfigValue<List<String>> wandererEnchantmentExceptions;
    public static final ForgeConfigSpec.ConfigValue<List<String>> enchantmentsWithCurses;

    static {
        builder.push("Truly Treasures Config");

        basePrice = builder
                .comment("Defines the base integer value used to generate the prices of the books, which is multiplied based on the level of the enchantment. Prices are calculated as follows:",
                        "If the enchantment on the book has a maximum level of one (such as Mending), it will use the base price multiplied by 2. Otherwise, it will use the base price multiplied by the level of the enchantment",
                        "Note that prices will never go above 64 emeralds")
                .defineInRange("Base book price", 16, 1, 64);

        maxTrades = builder
                .comment("Defines the maximum number of times you can buy from the same book trade")
                .defineInRange("Max book trades", 2, 1, 100);

        sellCurses = builder
                .comment("Defines whether it is possible for wandering traders to sell curse enchantments or not")
                .define("Wandering traders should sell curses", false);

        villagerEnchantmentExceptions = builder
                .comment("List of enchantments to be kept by villagers. Any enchantments listed here will not be removed from villager trading",
                        "TIP: If you want an enchantment to be completely ignored by this mod, you can add it to both this option and the \"Enchantments not sold by wandering traders\"",
                        "EXAMPLE: \"[\"minecraft:frost_walker\", \"momentum:momentum\", \"leap:leaping\"]\"")
                .define("Enchantments villagers will keep", Lists.newArrayList(""));

        wandererEnchantmentExceptions = builder
                .comment("List of enchantments that should not be sold by wandering traders. Any enchantments listed here will not be sold by wandering traders",
                        "TIP: If you want an enchantment to be completely ignored by this mod, you can add it to both this option and the \"Enchantments villagers will keep\"",
                        "EXAMPLE: \"[\"minecraft:frost_walker\", \"momentum:momentum\", \"leap:leaping\"]\"")
                .define("Enchantments not sold by wandering traders", Lists.newArrayList(""));

        enchantmentsWithCurses = builder
                .comment("List of enchantments that should have curses added to them. If an enchantment is listed here, it will always have a curse added alongside it when sold by a wandering trader",
                        "EXAMPLE: \"[\"minecraft:frost_walker\", \"momentum:momentum\", \"leap:leaping\"]\"")
                .define("Enchantments with curses added to them", Lists.newArrayList(""));

        builder.pop();

        config = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}

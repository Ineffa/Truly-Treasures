package com.snugz.trulytreasures.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.snugz.trulytreasures.TrulyTreasures;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;

public class TrulyTreasuresConfig {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec config;
    public static ForgeConfigSpec.IntValue basePrice;
    public static ForgeConfigSpec.IntValue maxTrades;
    public static ForgeConfigSpec.BooleanValue sellCurses;

    static {
        builder.push("Trader Config");

        basePrice = builder
                .comment("Defines the base integer value used to generate the prices of the books, which is multiplied based on the level of the enchantment. Prices are calculated as follows:",
                        "If the enchantment on the book has a maximum level of one (such as Mending), it will use the base price multiplied by 2. Otherwise, it will use the base price multiplied by the level of the enchantment",
                        "Note that prices will never go above 64 emeralds")
                .translation(TrulyTreasures.MOD_ID + ".config.basePrice")
                .defineInRange("basePrice", 16, 1, 64);

        maxTrades = builder
                .comment("Defines the maximum number of times you can buy from the same book trade")
                .translation(TrulyTreasures.MOD_ID + ".config.maxTrades")
                .defineInRange("maxTrades", 2, 1, 100);

        sellCurses = builder
                .comment("Defines whether it is possible for the Wandering Trader to sell curse enchantments or not")
                .translation(TrulyTreasures.MOD_ID + ".config.sellCurses")
                .define("sellCurses", false);

        builder.pop();

        config = builder.build();
    }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}

package com.snugzy.trulytreasures.config;

import com.google.common.collect.Lists;
import com.snugzy.trulytreasures.TrulyTreasures;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.List;

@Config(name = TrulyTreasures.MOD_ID)
public class TrulyTreasuresConfig implements ConfigData {

    @ConfigEntry.Gui.CollapsibleObject
    public WanderingTraderSettings wanderingTraderSettings = new WanderingTraderSettings();

    @ConfigEntry.Gui.CollapsibleObject
    public VillagerSettings villagerSettings = new VillagerSettings();

    public static class WanderingTraderSettings {
        @ConfigEntry.Gui.Tooltip(count = 3)
        @ConfigEntry.Gui.RequiresRestart
        @ConfigEntry.BoundedDiscrete(min = 0, max = 64)
        public int basePrice = 16;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
        public int maxTrades = 2;

        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.RequiresRestart
        public boolean sellCurses = false;

        @ConfigEntry.Gui.Tooltip(count = 2)
        @ConfigEntry.Gui.RequiresRestart
        public List<String> wandererEnchantmentExceptions = Lists.newArrayList("");
    }

    public static class VillagerSettings {
        @ConfigEntry.Gui.Tooltip(count = 2)
        public List<String> villagerEnchantmentExceptions = Lists.newArrayList("");
    }
}

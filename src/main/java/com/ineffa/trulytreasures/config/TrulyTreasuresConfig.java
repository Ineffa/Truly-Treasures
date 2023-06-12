package com.ineffa.trulytreasures.config;

import com.ineffa.trulytreasures.TrulyTreasures;
import com.ineffa.trulytreasures.VillagerEnchantmentRemovalMode;
import com.ineffa.trulytreasures.WandererTreasureEnchantmentFilter;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = TrulyTreasures.MOD_ID)
@Config.Gui.Background(value = "minecraft:textures/block/chiseled_bookshelf_occupied.png")
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

        @ConfigEntry.Gui.Tooltip(count = 7)
        @ConfigEntry.Gui.RequiresRestart
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public WandererTreasureEnchantmentFilter treasureEnchantmentFilter = WandererTreasureEnchantmentFilter.DEFAULT;

        @ConfigEntry.Gui.Tooltip(count = 2)
        @ConfigEntry.Gui.RequiresRestart
        public List<String> enchantmentBlacklist = new ArrayList<>();
    }

    public static class VillagerSettings {
        @ConfigEntry.Gui.Tooltip(count = 6)
        @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
        public VillagerEnchantmentRemovalMode enchantmentRemovalMode = VillagerEnchantmentRemovalMode.TREASURE;

        @ConfigEntry.Gui.Tooltip(count = 2)
        public List<String> enchantmentsToKeep = new ArrayList<>();
    }
}

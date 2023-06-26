package com.ineffa.trulytreasures;

import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = TrulyTreasures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class Events {

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        WandererTreasureEnchantmentFilter traderFilter = TrulyTreasures.config.wanderingTraderSettings.treasureEnchantmentFilter;
        traderFilter.filterEnchantments(ForgeRegistries.ENCHANTMENTS.getValues().stream()).forEach(enchantment -> TrulyTreasures.addWandererTradesForEnchantment(enchantment, event));
    }
}

package com.snugzy.trulytreasures;

import com.snugzy.trulytreasures.config.TrulyTreasuresConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TrulyTreasures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        if (!TrulyTreasuresConfig.sellTreasureEnchantments.get()) return;

        List<String> exceptions = TrulyTreasuresConfig.wandererEnchantmentExceptions.get();
        List<String> cursedEnchantments = TrulyTreasuresConfig.enchantmentsWithCurses.get();
        List<Enchantment> enchantmentList = TrulyTreasuresConfig.sellCurses.get() ? Registry.ENCHANTMENT.stream().filter((e) -> !exceptions.contains(e.getRegistryName().toString()) && e.isTreasureEnchantment() && e.canVillagerTrade()).collect(Collectors.toList()) : Registry.ENCHANTMENT.stream().filter((e) -> { return !exceptions.contains(e.getRegistryName().toString()) && e.isTreasureEnchantment() && e.canVillagerTrade() && !e.isCurse(); }).collect(Collectors.toList());
        List<Enchantment> curseList = Registry.ENCHANTMENT.stream().filter((c) -> c.isCurse() && c.canVillagerTrade()).collect(Collectors.toList());

        for (Enchantment enchantment : enchantmentList) {
            for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                ItemStack enchantedBook = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, level));
                enchantedBook.setCount(1);
                int basePrice = TrulyTreasuresConfig.basePrice.get();
                int emeralds = enchantment.getMaxLevel() == 1 ? basePrice * 2 : MathHelper.clamp(basePrice * level, 0, 64);
                int xp = enchantment.getMaxLevel() == 1 ? 10 : 5 * level;

                if (cursedEnchantments.contains(enchantment.getRegistryName().toString())) {
                    for (Enchantment curse : curseList) {
                        for (int curseLevel = curse.getMinLevel(); curseLevel <= curse.getMaxLevel(); curseLevel++) {
                            ItemStack cursedEnchantedBook = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, level));
                            cursedEnchantedBook.addEnchantment(curse, curseLevel);
                            cursedEnchantedBook.setCount(1);

                            event.getRareTrades().add(new BasicTrade(emeralds, cursedEnchantedBook, TrulyTreasuresConfig.maxTrades.get(), xp));
                        }
                    }
                }
                else event.getRareTrades().add(new BasicTrade(emeralds, enchantedBook, TrulyTreasuresConfig.maxTrades.get(), xp));
            }
        }
    }
}

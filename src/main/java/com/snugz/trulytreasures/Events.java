package com.snugz.trulytreasures;

import com.snugz.trulytreasures.config.TrulyTreasuresConfig;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
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
        List<String> exceptions = TrulyTreasuresConfig.wandererEnchantmentExceptions.get();
        List<String> cursedEnchantments = TrulyTreasuresConfig.enchantmentsWithCurses.get();
        List<Enchantment> enchantmentList = TrulyTreasuresConfig.sellCurses.get() ? Registry.ENCHANTMENT.stream().filter((e) -> { return !exceptions.contains(e.getRegistryName().toString()) && e.isTreasureOnly() && e.isTradeable(); }).collect(Collectors.toList()) : Registry.ENCHANTMENT.stream().filter((e) -> { return !exceptions.contains(e.getRegistryName().toString()) && e.isTreasureOnly() && e.isTradeable() && !e.isCurse(); }).collect(Collectors.toList());
        List<Enchantment> curseList = Registry.ENCHANTMENT.stream().filter((c) -> { return c.isCurse() && c.isTradeable(); }).collect(Collectors.toList());

        for (Enchantment enchantment : enchantmentList) {
            for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
                enchantedBook.setCount(1);
                int basePrice = TrulyTreasuresConfig.basePrice.get();
                int emeralds = enchantment.getMaxLevel() == 1 ? basePrice * 2 : Mth.clamp(basePrice * level, 0, 64);
                int xp = enchantment.getMaxLevel() == 1 ? 10 : 5 * level;

                if (cursedEnchantments.contains(enchantment.getRegistryName().toString())) {
                    for (Enchantment curse : curseList) {
                        for (int curseLevel = curse.getMinLevel(); curseLevel <= curse.getMaxLevel(); curseLevel++) {
                            ItemStack cursedEnchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
                            cursedEnchantedBook.enchant(curse, curseLevel);
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

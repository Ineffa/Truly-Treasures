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
        List<Enchantment> enchantmentList = TrulyTreasuresConfig.sellCurses.get() ?
                Registry.ENCHANTMENT.stream().filter((e) -> {
            return e.isTreasureOnly() && e.isTradeable();
        }).collect(Collectors.toList())
                :
                Registry.ENCHANTMENT.stream().filter((e) -> {
            return e.isTreasureOnly() && e.isTradeable() && !e.isCurse();
        }).collect(Collectors.toList());

        for (Enchantment enchantment : enchantmentList) {
            for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                ItemStack itemStack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
                itemStack.setCount(1);
                int basePrice = TrulyTreasuresConfig.basePrice.get();
                int emeralds = enchantment.getMaxLevel() == 1 ? basePrice * 2 : Mth.clamp(basePrice * level, 0, 64);
                int xp = enchantment.getMaxLevel() == 1 ? 10 : 5 * level;
                event.getRareTrades().add(new BasicTrade(emeralds, itemStack, TrulyTreasuresConfig.maxTrades.get(), xp));
            }
        }
    }
}

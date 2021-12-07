package com.snugzy.trulytreasures;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;

import java.util.List;
import java.util.stream.Collectors;

public class TrulyTreasures implements ModInitializer {

    @Override
    public void onInitialize() {
        // TODO: make curses configurable
        List<Enchantment> enchantmentList = Registry.ENCHANTMENT.stream().filter((e) -> {
            return e.isTreasure() && e.isAvailableForEnchantedBookOffer() && !e.isCursed();
        }).collect(Collectors.toList());

        for (Enchantment enchantment : enchantmentList) {
            for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                addWandererBookTrade(enchantment, level);
            }
        }

        RegistryEntryAddedCallback.event(Registry.ENCHANTMENT).register((rawId, id, enchantment) -> {
            // TODO: make curses configurable
            if (enchantment.isTreasure() && enchantment.isAvailableForEnchantedBookOffer() && !enchantment.isCursed()) {
                for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                    addWandererBookTrade(enchantment, level);
                }
            }
        });
    }

    private static void addWandererBookTrade(Enchantment enchantment, int level) {
        ItemStack book = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, level));
        book.setCount(1);
        int basePrice = 16; // TODO: make base price configurable
        int price = enchantment.getMaxLevel() == 1 ? basePrice * 2 : MathHelper.clamp(basePrice * level, 0, 64);
        int xp = enchantment.getMaxLevel() == 1 ? 10 : 5 * level;
        int maxTrades = 2; // TODO: make max trades configurable

        TradeOfferHelper.registerWanderingTraderOffers(maxTrades, (factory -> {
            factory.add(((entity, random) -> new TradeOffer(new ItemStack(Items.EMERALD, price), book, maxTrades, xp, 1.0F)));
        }));
    }
}

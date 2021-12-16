package com.snugzy.trulytreasures;

import com.snugzy.trulytreasures.config.TrulyTreasuresConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
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
    public static final String MOD_ID = "trulytreasures";
    public static TrulyTreasuresConfig config;

    @Override
    public void onInitialize() {
        AutoConfig.register(TrulyTreasuresConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(TrulyTreasuresConfig.class).getConfig();
        List<String> exceptions = config.wanderingTraderSettings.wandererEnchantmentExceptions;

        List<Enchantment> enchantmentList = config.wanderingTraderSettings.sellCurses ?
                Registry.ENCHANTMENT.stream().filter((e) -> !exceptions.contains(Registry.ENCHANTMENT.getId(e).toString()) && e.isTreasure() && e.isAvailableForEnchantedBookOffer()).collect(Collectors.toList())
                :
                Registry.ENCHANTMENT.stream().filter((e) -> !exceptions.contains(Registry.ENCHANTMENT.getId(e).toString()) && e.isTreasure() && e.isAvailableForEnchantedBookOffer() && !e.isCursed()).collect(Collectors.toList());

        // Debug
        // enchantmentList.forEach(e -> System.out.println(Registry.ENCHANTMENT.getId(e).toString()));

        for (Enchantment enchantment : enchantmentList) {
            for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                addWandererBookTrade(enchantment, level);
            }
        }

        RegistryEntryAddedCallback.event(Registry.ENCHANTMENT).register((rawId, id, enchantment) -> {
            if (!exceptions.contains(Registry.ENCHANTMENT.getId(enchantment).toString()) && enchantment.isTreasure() && enchantment.isAvailableForEnchantedBookOffer()) {
                if (!(!config.wanderingTraderSettings.sellCurses && enchantment.isCursed())) {
                    for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                        addWandererBookTrade(enchantment, level);
                    }
                }
            }
        });
    }

    private static void addWandererBookTrade(Enchantment enchantment, int level) {
        ItemStack book = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, level));
        book.setCount(1);
        int maxTrades = config.wanderingTraderSettings.maxTrades;
        int basePrice = config.wanderingTraderSettings.basePrice;
        int price = enchantment.getMaxLevel() == 1 ? basePrice * 2 : MathHelper.clamp(basePrice * level, 0, 64);
        int xp = enchantment.getMaxLevel() == 1 ? 10 : 5 * level;

        TradeOfferHelper.registerWanderingTraderOffers(2, (factory -> factory.add(((entity, random) -> new TradeOffer(new ItemStack(Items.EMERALD, price), book, maxTrades, xp, 1.0F)))));

        // Debug
        // System.out.println(Registry.ENCHANTMENT.getId(enchantment).toString() + " level " + level + " price is " + price);
    }
}

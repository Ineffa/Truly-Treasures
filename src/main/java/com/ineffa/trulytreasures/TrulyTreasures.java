package com.ineffa.trulytreasures;

import com.ineffa.trulytreasures.config.TrulyTreasuresConfig;
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
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
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

        if (!config.wanderingTraderSettings.sellTreasureEnchantments) return;
        List<String> exceptions = config.wanderingTraderSettings.wandererEnchantmentExceptions;

        List<Enchantment> enchantmentList = config.wanderingTraderSettings.sellCurses ?
                Registries.ENCHANTMENT.stream().filter((e) -> !exceptions.contains(Registries.ENCHANTMENT.getId(e).toString()) && e.isTreasure() && e.isAvailableForEnchantedBookOffer()).collect(Collectors.toList())
                :
                Registries.ENCHANTMENT.stream().filter((e) -> !exceptions.contains(Registries.ENCHANTMENT.getId(e).toString()) && e.isTreasure() && e.isAvailableForEnchantedBookOffer() && !e.isCursed()).collect(Collectors.toList());

        // Debug
        // enchantmentList.forEach(e -> System.out.println(Registries.ENCHANTMENT.getId(e).toString()));

        for (Enchantment enchantment : enchantmentList) {
            for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
                addWandererBookTrade(enchantment, level);
            }
        }

        RegistryEntryAddedCallback.event(Registries.ENCHANTMENT).register((rawId, id, enchantment) -> {
            if (!exceptions.contains(Registries.ENCHANTMENT.getId(enchantment).toString()) && enchantment.isTreasure() && enchantment.isAvailableForEnchantedBookOffer()) {
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
        // System.out.println(Registries.ENCHANTMENT.getId(enchantment).toString() + " level " + level + " price is " + price);
    }
}

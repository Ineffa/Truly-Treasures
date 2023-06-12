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

import java.util.Objects;

public class TrulyTreasures implements ModInitializer {
    public static final String MOD_ID = "trulytreasures";
    public static TrulyTreasuresConfig config;

    @Override
    public void onInitialize() {
        AutoConfig.register(TrulyTreasuresConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(TrulyTreasuresConfig.class).getConfig();

        WandererTreasureEnchantmentFilter traderFilter = config.wanderingTraderSettings.treasureEnchantmentFilter;

        traderFilter.filterEnchantments(Registries.ENCHANTMENT.stream()).forEach(TrulyTreasures::addWandererTradesForEnchantment);

        RegistryEntryAddedCallback.event(Registries.ENCHANTMENT).register((rawId, id, enchantment) -> {
            if (traderFilter.acceptsEnchantment(enchantment)) addWandererTradesForEnchantment(enchantment);
        });
    }

    private static void addWandererTradesForEnchantment(Enchantment enchantment) {
        for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
            ItemStack book = EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, level));
            book.setCount(1);
            int maxTrades = config.wanderingTraderSettings.maxTrades;
            int basePrice = config.wanderingTraderSettings.basePrice;
            int price = enchantment.getMaxLevel() == 1 ? basePrice * 2 : MathHelper.clamp(basePrice * level, 0, 64);
            int xp = enchantment.getMaxLevel() == 1 ? 10 : 5 * level;

            TradeOfferHelper.registerWanderingTraderOffers(2, (factory -> factory.add(((entity, random) -> new TradeOffer(new ItemStack(Items.EMERALD, price), book, maxTrades, xp, 1.0F)))));
        }
    }

    public static boolean isEnchantmentAllowedForVillagers(Enchantment enchantment) {
        if (!enchantment.isAvailableForEnchantedBookOffer()) return false;

        return !config.villagerSettings.enchantmentRemovalMode.shouldRemoveEnchantment(enchantment) || config.villagerSettings.enchantmentsToKeep.contains(Objects.requireNonNull(Registries.ENCHANTMENT.getId(enchantment)).toString());
    }
}

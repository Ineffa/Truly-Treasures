package com.ineffa.trulytreasures;

import com.ineffa.trulytreasures.config.TrulyTreasuresConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod("trulytreasures")
public class TrulyTreasures {
    public static final String MOD_ID = "trulytreasures";
    public static TrulyTreasuresConfig config;

    public TrulyTreasures() {
        AutoConfig.register(TrulyTreasuresConfig.class, GsonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(TrulyTreasuresConfig.class).getConfig();

        MinecraftForge.EVENT_BUS.register(this);
    }

    protected static void addWandererTradesForEnchantment(Enchantment enchantment, WandererTradesEvent event) {
        for (int level = enchantment.getMinLevel(); level <= enchantment.getMaxLevel(); level++) {
            ItemStack book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
            book.setCount(1);
            int maxTrades = config.wanderingTraderSettings.maxTrades;
            int basePrice = config.wanderingTraderSettings.basePrice;
            int price = enchantment.getMaxLevel() == 1 ? basePrice * 2 : Mth.clamp(basePrice * level, 0, 64);
            int xp = enchantment.getMaxLevel() == 1 ? 10 : 5 * level;

            event.getRareTrades().add(new BasicItemListing(price, book, maxTrades, xp));
        }
    }

    public static boolean isEnchantmentAllowedForVillagers(Enchantment enchantment) {
        if (!enchantment.isTradeable()) return false;

        return !config.villagerSettings.enchantmentRemovalMode.shouldRemoveEnchantment(enchantment) || config.villagerSettings.enchantmentsToKeep.contains(Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(enchantment)).toString());
    }
}

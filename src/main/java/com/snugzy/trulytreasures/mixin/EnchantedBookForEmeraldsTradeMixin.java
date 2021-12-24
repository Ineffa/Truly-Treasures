package com.snugzy.trulytreasures.mixin;

import com.snugzy.trulytreasures.config.TrulyTreasuresConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(VillagerTrades.EnchantedBookForEmeraldsTrade.class)
public class EnchantedBookForEmeraldsTradeMixin {
    @Shadow
    @Final
    private int xpValue;

    @Inject(at = @At("HEAD"), method = "getOffer", cancellable = true)
    private void removeTreasureEnchantments(Entity trader, Random rand, CallbackInfoReturnable<MerchantOffer> callback) {
        List<String> exceptions = TrulyTreasuresConfig.villagerEnchantmentExceptions.get();
        List<Enchantment> list = Registry.ENCHANTMENT.stream().filter((e) -> !TrulyTreasuresConfig.removeTreasureEnchantments.get() || exceptions.contains(e.getRegistryName().toString()) ? e.canVillagerTrade() : !e.isTreasureEnchantment() && e.canVillagerTrade()).collect(Collectors.toList());
        Enchantment enchantment = list.get(rand.nextInt(list.size()));
        int i = MathHelper.nextInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemstack = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, i));
        int j = 2 + rand.nextInt(5 + i * 10) + 3 * i;

        if (j > 64) {
            j = 64;
        }

        callback.setReturnValue(new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemstack, 12, this.xpValue, 0.2F));
    }
}

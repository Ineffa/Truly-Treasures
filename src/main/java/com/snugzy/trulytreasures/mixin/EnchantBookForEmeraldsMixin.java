package com.snugzy.trulytreasures.mixin;

import com.snugzy.trulytreasures.config.TrulyTreasuresConfig;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mixin(VillagerTrades.EnchantBookForEmeralds.class)
public class EnchantBookForEmeraldsMixin {
    @Shadow
    @Final
    private int villagerXp;

    @Inject(at = @At("HEAD"), method = "getOffer", cancellable = true)
    private void removeTreasureEnchantments(Entity p_35685_, Random p_35686_, CallbackInfoReturnable<MerchantOffer> callback) {
        List<String> exceptions = TrulyTreasuresConfig.villagerEnchantmentExceptions.get();
        List<Enchantment> list = Registry.ENCHANTMENT.stream().filter((e) -> {
            return exceptions.contains(e.getRegistryName().toString()) ? e.isTradeable() : !e.isTreasureOnly() && e.isTradeable();
        }).collect(Collectors.toList());
        Enchantment enchantment = list.get(p_35686_.nextInt(list.size()));
        int i = Mth.nextInt(p_35686_, enchantment.getMinLevel(), enchantment.getMaxLevel());
        ItemStack itemstack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
        int j = 2 + p_35686_.nextInt(5 + i * 10) + 3 * i;

        if (j > 64) {
            j = 64;
        }

        callback.setReturnValue(new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemstack, 12, this.villagerXp, 0.2F));
    }
}

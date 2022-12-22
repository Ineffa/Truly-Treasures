package com.ineffa.trulytreasures.mixin;

import com.ineffa.trulytreasures.TrulyTreasures;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(TradeOffers.EnchantBookFactory.class)
public class EnchantBookFactoryMixin {

    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"))
    private Stream<Enchantment> modifyVillagerEnchantmentTrades(Stream<Enchantment> stream, Predicate<Enchantment> predicate) {
        List<String> exceptions = TrulyTreasures.config.villagerSettings.villagerEnchantmentExceptions;
        return stream.filter((enchantment) -> !TrulyTreasures.config.villagerSettings.removeTreasureEnchantments || exceptions.contains(Registries.ENCHANTMENT.getId(enchantment).toString()) ? enchantment.isAvailableForEnchantedBookOffer() : !enchantment.isTreasure() && enchantment.isAvailableForEnchantedBookOffer());
    }
}

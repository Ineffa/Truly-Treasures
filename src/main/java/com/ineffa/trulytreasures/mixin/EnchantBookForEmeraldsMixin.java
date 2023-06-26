package com.ineffa.trulytreasures.mixin;

import com.ineffa.trulytreasures.TrulyTreasures;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(VillagerTrades.EnchantBookForEmeralds.class)
public class EnchantBookForEmeraldsMixin {

    @Redirect(method = "getOffer", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"))
    private Stream<Enchantment> modifyVillagerEnchantmentTrades(Stream<Enchantment> stream, Predicate<Enchantment> predicate) {
        return stream.filter(TrulyTreasures::isEnchantmentAllowedForVillagers);
    }
}

package com.ineffa.trulytreasures;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum WandererTreasureEnchantmentFilter {
    DEFAULT(enchantment -> enchantment.isAvailableForEnchantedBookOffer() && !enchantment.isCursed()),
    VILLAGER(Enchantment::isAvailableForEnchantedBookOffer),
    POSITIVE(enchantment -> !enchantment.isCursed()),
    ALL(enchantment -> true),
    CURSES(Enchantment::isCursed),
    NONE(enchantment -> false);

    private final Predicate<Enchantment> enchantmentFilter;

    WandererTreasureEnchantmentFilter(Predicate<Enchantment> enchantmentFilter) {
        this.enchantmentFilter = enchantment -> enchantment.isTreasure() && enchantmentFilter.test(enchantment) && !TrulyTreasures.config.wanderingTraderSettings.enchantmentBlacklist.contains(Registries.ENCHANTMENT.getId(enchantment).toString());
    }

    public List<Enchantment> filterEnchantments(Stream<Enchantment> streamToFilter) {
        return streamToFilter.filter(this.enchantmentFilter).toList();
    }

    public boolean acceptsEnchantment(Enchantment enchantment) {
        return this.enchantmentFilter.test(enchantment);
    }
}

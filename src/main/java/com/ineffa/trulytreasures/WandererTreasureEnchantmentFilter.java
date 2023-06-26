package com.ineffa.trulytreasures;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public enum WandererTreasureEnchantmentFilter {
    DEFAULT(enchantment -> enchantment.isTradeable() && !enchantment.isCurse()),
    VILLAGER(Enchantment::isTradeable),
    POSITIVE(enchantment -> !enchantment.isCurse()),
    ALL(enchantment -> true),
    CURSES(Enchantment::isCurse),
    NONE(enchantment -> false);

    private final Predicate<Enchantment> enchantmentFilter;

    WandererTreasureEnchantmentFilter(Predicate<Enchantment> enchantmentFilter) {
        this.enchantmentFilter = enchantment -> enchantment.isTreasureOnly() && enchantmentFilter.test(enchantment) && !TrulyTreasures.config.wanderingTraderSettings.enchantmentBlacklist.contains(ForgeRegistries.ENCHANTMENTS.getKey(enchantment).toString());
    }

    public List<Enchantment> filterEnchantments(Stream<Enchantment> streamToFilter) {
        return streamToFilter.filter(this.enchantmentFilter).toList();
    }

    public boolean acceptsEnchantment(Enchantment enchantment) {
        return this.enchantmentFilter.test(enchantment);
    }
}

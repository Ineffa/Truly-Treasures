package com.ineffa.trulytreasures;

import net.minecraft.enchantment.Enchantment;

import java.util.function.Predicate;

public enum VillagerEnchantmentRemovalMode {
    TREASURE(Enchantment::isTreasure),
    REGULAR(enchantment -> !enchantment.isTreasure()),
    CURSES(Enchantment::isCursed),
    POSITIVE(enchantment -> !enchantment.isCursed()),
    NONE(enchantment -> false);

    private final Predicate<Enchantment> removalPredicate;

    VillagerEnchantmentRemovalMode(Predicate<Enchantment> removalPredicate) {
        this.removalPredicate = removalPredicate;
    }

    public boolean shouldRemoveEnchantment(Enchantment enchantment) {
        return this.removalPredicate.test(enchantment);
    }
}

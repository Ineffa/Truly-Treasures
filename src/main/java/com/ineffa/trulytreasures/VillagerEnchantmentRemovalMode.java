package com.ineffa.trulytreasures;

import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Predicate;

public enum VillagerEnchantmentRemovalMode {
    TREASURE(Enchantment::isTreasureOnly),
    REGULAR(enchantment -> !enchantment.isTreasureOnly()),
    CURSES(Enchantment::isCurse),
    POSITIVE(enchantment -> !enchantment.isCurse()),
    NONE(enchantment -> false);

    private final Predicate<Enchantment> removalPredicate;

    VillagerEnchantmentRemovalMode(Predicate<Enchantment> removalPredicate) {
        this.removalPredicate = removalPredicate;
    }

    public boolean shouldRemoveEnchantment(Enchantment enchantment) {
        return this.removalPredicate.test(enchantment);
    }
}

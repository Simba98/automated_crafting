package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.ItemStack;
import net.sssubtlety.automated_crafting.Config;

public class TemplateInventory extends RecipeInventory {
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return super.isValid(slot, stack) && Config.isSimpleMode();
    }
}

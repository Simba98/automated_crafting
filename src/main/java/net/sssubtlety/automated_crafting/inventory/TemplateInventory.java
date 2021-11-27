package net.sssubtlety.automated_crafting.inventory;

import net.minecraft.item.ItemStack;
import net.sssubtlety.automated_crafting.Config;
import net.sssubtlety.automated_crafting.blockEntity.AutoCrafterBlockEntity;

public class TemplateInventory extends RecipeInventory {
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return Config.isSimpleMode();
    }
}

package net.sssubtlety.automated_crafting.mixin;

import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WItemSlot.class)
public interface WItemSlotAccessor {
    @Accessor("inventory")
    Inventory getInventory();
    @Accessor("inventory")
    void setInventory(Inventory inventory);

    @Accessor("startIndex")
    int getStartIndex();
    @Accessor("startIndex")
    void setStartIndex(int startIndex);

    @Accessor("slotsWide")
    int getSlotsWide();
    @Accessor("slotsWide")
    void setSlotsWide(int width);

    @Accessor("slotsHigh")
    int getSlotsHigh();
    @Accessor("slotsHigh")
    void setSlotsHigh(int height);
}

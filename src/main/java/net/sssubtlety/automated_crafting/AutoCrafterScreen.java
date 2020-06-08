package net.sssubtlety.automated_crafting;


import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;

public class AutoCrafterScreen extends CottonInventoryScreen<AutoCrafterController> {
    public AutoCrafterScreen(AutoCrafterController container, PlayerEntity player) {
        super(container, player);
    }
}
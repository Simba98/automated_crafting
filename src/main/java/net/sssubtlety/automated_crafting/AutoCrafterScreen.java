package net.sssubtlety.automated_crafting;


import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class AutoCrafterScreen extends CottonInventoryScreen<AutoCrafterGuiDescription> {
    public AutoCrafterScreen(AutoCrafterGuiDescription gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}
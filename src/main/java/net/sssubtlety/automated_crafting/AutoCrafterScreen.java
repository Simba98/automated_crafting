package net.sssubtlety.automated_crafting;


import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.sssubtlety.automated_crafting.guiDescription.AbstractAutoCrafterGuiDescription;

public class AutoCrafterScreen extends CottonInventoryScreen<AbstractAutoCrafterGuiDescription> {
    public AutoCrafterScreen(AbstractAutoCrafterGuiDescription gui, PlayerEntity player, Text title) {
        super(gui, player, title);
    }
}
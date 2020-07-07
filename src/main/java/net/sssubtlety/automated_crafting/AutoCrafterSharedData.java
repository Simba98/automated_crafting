package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.sssubtlety.automated_crafting.AutomatedCraftingConfig;

public interface AutoCrafterSharedData {
    boolean SIMPLE_MODE = AutoConfig.getConfigHolder(AutomatedCraftingConfig.class).getConfig().isSimpleMode();

    boolean QUASI_CONNECTIVITY = AutoConfig.getConfigHolder(AutomatedCraftingConfig.class).getConfig().isQuasiConnected();

    int GRID_WIDTH = 3;
    int GRID_HEIGHT = 3;
    int OUTPUT_SLOT = GRID_WIDTH * GRID_HEIGHT * (SIMPLE_MODE ? 2 : 1);
}

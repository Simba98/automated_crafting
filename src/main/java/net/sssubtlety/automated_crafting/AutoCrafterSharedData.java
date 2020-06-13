package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;

public interface AutoCrafterSharedData {
    boolean SIMPLE_MODE = AutoConfig.getConfigHolder(AutomatedCraftingConfig.class).getConfig().simpleMode;

    boolean QUASI_CONNECTIVITY = AutoConfig.getConfigHolder(AutomatedCraftingConfig.class).getConfig().quasiConnectivity;

    int GRID_WIDTH = 3;
    int GRID_HEIGHT = 3;
    int OUTPUT_SLOT = GRID_WIDTH * GRID_HEIGHT * (SIMPLE_MODE ? 2 : 1);
}

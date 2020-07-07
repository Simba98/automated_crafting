package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.sssubtlety.automated_crafting.AutomatedCraftingConfig;

public class AutoCrafterSharedData {
    public static final boolean SIMPLE_MODE = AutoConfig.getConfigHolder(AutomatedCraftingConfig.class).getConfig().isSimpleMode();

    public static final boolean QUASI_CONNECTIVITY = AutoConfig.getConfigHolder(AutomatedCraftingConfig.class).getConfig().isQuasiConnected();

    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final int OUTPUT_SLOT = GRID_WIDTH * GRID_HEIGHT * (SIMPLE_MODE ? 2 : 1);

    private static int validationKey = Integer.MIN_VALUE;

    public static int getValidationKey() {
        return validationKey;
    }

    public static void updateValidationKey() {
        validationKey++;
    }
}

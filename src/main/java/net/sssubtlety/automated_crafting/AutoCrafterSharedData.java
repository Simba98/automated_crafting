package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.sssubtlety.automated_crafting.block.complexity.ComplexMode;
import net.sssubtlety.automated_crafting.block.complexity.ComplexityMode;
import net.sssubtlety.automated_crafting.block.complexity.SimpleMode;
import net.sssubtlety.automated_crafting.block.connectivity.BasicConnectivity;
import net.sssubtlety.automated_crafting.block.connectivity.Connectivity;
import net.sssubtlety.automated_crafting.block.connectivity.QuasiConnectivity;

public abstract class AutoCrafterSharedData {
    private static final AutomatedCraftingConfig CONFIG = AutoConfig.getConfigHolder(AutomatedCraftingConfig.class).getConfig();
    
    public static final boolean SIMPLE_MODE = CONFIG.isSimpleMode();

    public static final boolean QUASI_CONNECTIVITY = CONFIG.isQuasiConnected();

    public static final boolean REDIRECTS_REDSTONE = CONFIG.doesRedirectRedstone();

    public static final boolean CRAFTS_CONTINUOUSLY = CONFIG.doesCraftContinuously();

    public static boolean DOES_COMPARATOR_READ_OUTPUT = CONFIG.doesComparatorReadOutput();
    
    public static final Class<? extends ComplexityMode> COMPLEXITY_CLASS = SIMPLE_MODE ? SimpleMode.class : ComplexMode.class;

    public static final Class<? extends Connectivity> CONNECTIVITY_CLASS = QUASI_CONNECTIVITY ? QuasiConnectivity.class : BasicConnectivity.class;

    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final int GRID_SIZE = GRID_WIDTH * GRID_HEIGHT;
    public static final int OUTPUT_SLOT = GRID_SIZE * (SIMPLE_MODE ? 2 : 1);

    public static final int FIRST_INPUT_SLOT = SIMPLE_MODE ? GRID_SIZE : 0;

    public static final int FIRST_TEMPLATE_SLOT = 0;


    private static int validationKey = Integer.MIN_VALUE;

    public static int getValidationKey() {
        return validationKey;
    }

    public static void updateValidationKey() {
        validationKey++;
    }
}

package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.sssubtlety.automated_crafting.block.complexity.ComplexMode;
import net.sssubtlety.automated_crafting.block.complexity.ComplexityMode;
import net.sssubtlety.automated_crafting.block.complexity.SimpleMode;
import net.sssubtlety.automated_crafting.block.connectivity.BasicConnectivity;
import net.sssubtlety.automated_crafting.block.connectivity.Connectivity;
import net.sssubtlety.automated_crafting.block.connectivity.QuasiConnectivity;

import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.QUASI_CONNECTIVITY;
import static net.sssubtlety.automated_crafting.AutoCrafterSharedData.SIMPLE_MODE;

@Config(name = "automated_crafting")
public class AutomatedCraftingConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    boolean simpleMode = true;

    @ConfigEntry.Gui.Tooltip()
    boolean quasiConnectivity = false;

    @ConfigEntry.Gui.Tooltip()
    boolean redirectsRedstone = false;

    @ConfigEntry.Gui.Tooltip()
    boolean craftContinuously = false;

    public boolean isSimpleMode() {
        return simpleMode;
    }

    public boolean isQuasiConnected() {
        return quasiConnectivity;
    }

    public boolean doesCraftContinuously() {
        return craftContinuously;
    }

    public boolean doesRedirectRedstone() {
        return redirectsRedstone;
    }


}

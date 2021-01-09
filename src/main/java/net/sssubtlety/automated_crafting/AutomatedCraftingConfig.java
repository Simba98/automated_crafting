package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

import static net.sssubtlety.automated_crafting.AutomatedCraftingInit.MOD_ID;

@Config(name = MOD_ID)
public class AutomatedCraftingConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    boolean simpleMode = true;

    @ConfigEntry.Gui.Tooltip()
    boolean quasiConnectivity = false;

    @ConfigEntry.Gui.Tooltip()
    boolean redirectsRedstone = false;

    @ConfigEntry.Gui.Tooltip()
    boolean craftContinuously = false;

    @ConfigEntry.Gui.Tooltip()
    boolean comparatorReadsOutput = false;

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

    public boolean doesComparatorReadOutput() {
        return comparatorReadsOutput;
    }
}

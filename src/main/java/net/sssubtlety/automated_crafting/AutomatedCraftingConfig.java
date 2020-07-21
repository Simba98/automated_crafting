package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "automated_crafting")
public class AutomatedCraftingConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    boolean simpleMode = true;

    @ConfigEntry.Gui.Tooltip()
    boolean quasiConnectivity = false;

    public boolean isSimpleMode() {
        return simpleMode;
    }

    public boolean isQuasiConnected() {
        return quasiConnectivity;
    }
}

package net.sssubtlety.automated_crafting;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = "automated_crafting")
public class AutomatedCraftingConfig implements ConfigData {
    boolean simpleMode = true;
    boolean quasiConnectivity = false;

    public boolean isSimpleMode() {
        return simpleMode;
    }

    public boolean isQuasiConnected() {
        return quasiConnectivity;
    }
}

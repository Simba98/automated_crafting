package net.sssubtlety.automated_crafting;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import static net.sssubtlety.automated_crafting.AutomatedCrafting.NAMESPACE;

@me.shedaniel.autoconfig.annotation.Config(name = NAMESPACE)
public class Config implements ConfigData {
    private static Config INSTANCE;

    public static void init() {
        AutoConfig.register(Config.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(Config.class).getConfig();
    }

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

    public static boolean isSimpleMode() {
        return INSTANCE.simpleMode;
    }

    public static boolean isQuasiConnected() {
        return INSTANCE.quasiConnectivity;
    }

    public static boolean doesCraftContinuously() {
        return INSTANCE.craftContinuously;
    }

    public static boolean doesRedirectRedstone() {
        return INSTANCE.redirectsRedstone;
    }

    public static boolean doesComparatorReadOutput() {
        return INSTANCE.comparatorReadsOutput;
    }
}

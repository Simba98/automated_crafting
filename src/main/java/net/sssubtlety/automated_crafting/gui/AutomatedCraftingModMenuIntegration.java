package net.sssubtlety.automated_crafting.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.sssubtlety.automated_crafting.Config;

import java.util.Optional;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class AutomatedCraftingModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            Optional<Supplier<Screen>> optionalScreen = getConfigScreen(parent);
            if (optionalScreen.isPresent()) {
                return optionalScreen.get().get();
            } else {
                return parent;
            }
        };
    }

//    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(AutoConfig.getConfigScreen(Config.class, screen));
    }
}

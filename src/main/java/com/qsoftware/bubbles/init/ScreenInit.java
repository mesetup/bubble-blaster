package com.qsoftware.bubbles.init;

import com.qsoftware.bubbles.QInternalAddon;
import com.qsoftware.bubbles.common.init.ObjectInit;
import com.qsoftware.bubbles.common.screen.ScreenType;
import com.qsoftware.bubbles.registry.DeferredRegister;
import com.qsoftware.bubbles.registry.Registers;
import com.qsoftware.bubbles.registry.object.RegistryObject;
import com.qsoftware.bubbles.screen.CommandScreen;
import com.qsoftware.bubbles.screen.PauseScreen;

import java.util.function.Supplier;

@SuppressWarnings("rawtypes")
public class ScreenInit implements ObjectInit<ScreenType> {
    public static final DeferredRegister<ScreenType> SCREENS = DeferredRegister.create(QInternalAddon.ADDON_ID, Registers.SCREENS);

    // Screens
    public static final RegistryObject<ScreenType<PauseScreen>> PAUSE_SCREEN = register("pause", () -> new ScreenType<>(PauseScreen.class));
    public static final RegistryObject<ScreenType<CommandScreen>> COMMAND_SCREEN = register("command", () -> new ScreenType<>(CommandScreen.class));

    private static <T extends ScreenType<?>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return SCREENS.register(name, supplier);
    }
}

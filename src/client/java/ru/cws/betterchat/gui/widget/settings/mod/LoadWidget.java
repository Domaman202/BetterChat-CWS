package ru.cws.betterchat.gui.widget.settings.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class LoadWidget extends ChatWidget {
    public LoadWidget(int i, int j) {
        super(i, j, 90, 20, Text.of("Загрузить"), Text.of("Загрузить конфигурацию из выбранного файла"));
    }

    @Override
    public void onPress() {
        var filterE = MemoryUtil.memUTF8Safe("*.json", true);
        var filterA = MemoryUtil.memAllocPointer(1);
        filterA.put(0, filterE);
        try {
            var file = TinyFileDialogs.tinyfd_openFileDialog(
                    "Select config file to load",
                    BetterChatMod.CONFIG_FILE,
                    filterA,
                    "Json file",
                    false
            );
            if (file != null) {
                BetterChatMod.load(file);
                MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
            }
        } finally {
            MemoryUtil.memFree(filterA);
            MemoryUtil.memFree(filterE);
        }
    }
}

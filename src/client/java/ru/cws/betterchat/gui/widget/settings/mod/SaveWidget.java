package ru.cws.betterchat.gui.widget.settings.mod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.tinyfd.TinyFileDialogs;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ChatWidget;
import ru.cws.betterchat.screen.ModSettingsScreen;

public class SaveWidget extends ChatWidget {
    public SaveWidget(int x, int y, int w) {
        super(x, y, w, 20, Text.of("Сохранить"), Text.of("Сохранить конфигурацию в выбранный файл"));
        this.flexRender.setBaseColor(0x60606010);
        this.flexRender.setHoverColor(0x60FFFF10);
    }

    @Override
    public void onPress() {
        var filterE = MemoryUtil.memUTF8Safe("*.json", true);
        var filterA = MemoryUtil.memAllocPointer(1);
        filterA.put(0, filterE);
        try {
            var file = TinyFileDialogs.tinyfd_saveFileDialog(
                    "Select config file to save",
                    BetterChatMod.CONFIG_FILE,
                    filterA,
                    "Json file"
            );
            if (file != null) {
                BetterChatMod.save(file);
                MinecraftClient.getInstance().setScreen(new ModSettingsScreen());
            }
        } finally {
            MemoryUtil.memFree(filterA);
            MemoryUtil.memFree(filterE);
        }
    }
}

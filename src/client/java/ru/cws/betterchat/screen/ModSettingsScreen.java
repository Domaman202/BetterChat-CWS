package ru.cws.betterchat.screen;

import net.minecraft.text.Text;
import ru.cws.betterchat.gui.widget.settings.mod.AutosaveSettingsWidget;
import ru.cws.betterchat.gui.widget.settings.mod.FlexRenderSettingsWidget;
import ru.cws.betterchat.gui.widget.settings.mod.LoadWidget;
import ru.cws.betterchat.gui.widget.settings.mod.SaveWidget;

public class ModSettingsScreen extends AbstractSettingsScreen {
    public ModSettingsScreen() {
        super(Text.of("Настройки чата"));
    }

    @Override
    protected void init() {
        super.init();
        //
        this.addSettingsWidget(new FlexRenderSettingsWidget());
        this.addSettingsWidget(new AutosaveSettingsWidget());
        //
        this.addDrawableChild(new SaveWidget(this.width / 2 - 92, this.height / 2 - 53 + this.settingsOffset));
        this.addDrawableChild(new LoadWidget(this.width / 2 + 2, this.height / 2 - 53 + this.settingsOffset));
        this.settingsOffset += 21;
    }
}

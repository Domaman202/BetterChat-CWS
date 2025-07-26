package ru.cws.betterchat.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import ru.cws.betterchat.gui.widget.settings.mod.*;

public class ModSettingsScreen extends AbstractSettingsScreen {
    public ModSettingsScreen() {
        this(0);
    }

    public ModSettingsScreen(int tabListPosition) {
        super(Text.of("Настройки чата"), null);
        this.tabListPosition = tabListPosition;
    }

    @Override
    public void BetterCombat$recalcTabsList() {
        super.BetterCombat$recalcTabsList();
        //
        this.addSettingsWidget(new FixedTabSizeSettingsWidget());
        this.addSettingsWidget(new HeavyTexturesSettingsWidget());
        this.addSettingsWidget(new FlexRenderSettingsWidget());
        this.addSettingsWidget(new AutosaveSettingsWidget());
        //
        var xc = this.getXC();
        var xs = this.getXS();
        var xe = this.getXE();
        var y = this.getYC() - 53 + this.settingsOffset;
        this.addDrawableChild(new SaveWidget(xc + 2, y, xe - xc - 4));
        this.addDrawableChild(new LoadWidget(xs + 3, y, xc - xs - 4));
        this.settingsOffset += 21;
    }
}

package ru.cws.betterchat.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.cws.betterchat.BetterChatMod;
import ru.cws.betterchat.gui.widget.ListLeftWidget;
import ru.cws.betterchat.gui.widget.ListRightWidget;
import ru.cws.betterchat.gui.widget.chat.AllCategoryWidget;
import ru.cws.betterchat.gui.widget.chat.CategoryWidget;
import ru.cws.betterchat.gui.widget.chat.GlobalLocalWidget;
import ru.cws.betterchat.gui.widget.chat.SettingsWidget;
import ru.cws.betterchat.util.IChatScreen;
import ru.cws.betterchat.util.ITabListenScreen;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen implements IChatScreen, ITabListenScreen {
    @Shadow
    protected TextFieldWidget chatField;
    @Unique
    private List<CategoryWidget> BetterChat$tabs;
    @Unique
    private SettingsWidget BetterCombat$settingsWidget;
    @Unique
    private ListLeftWidget BetterChat$listLeftWidget;
    @Unique
    private ListRightWidget BetterChat$listRightWidget;
    @Unique
    private GlobalLocalWidget BetterChat$globalLocalWidget;
    @Unique
    private int tabListPosition = 0;

    protected ChatScreenMixin(Text title) {super(title);}

    @Override
    public void BetterCombat$setTabListPosition(int position) {
        this.tabListPosition = position;
    }

    @Override
    public int BetterCombat$getTabListPosition() {
        return this.tabListPosition;
    }

    @Override
    public List<CategoryWidget> BetterChat$tabs() {
        return this.BetterChat$tabs;
    }

    @Override
    public void BetterCombat$recalcTabsList() {
        if (this.BetterCombat$settingsWidget != null)
            this.remove(this.BetterCombat$settingsWidget);
        if (this.BetterChat$listLeftWidget != null)
            this.remove(this.BetterChat$listLeftWidget);
        if (this.BetterChat$listRightWidget != null)
            this.remove(this.BetterChat$listRightWidget);
        if (this.BetterChat$globalLocalWidget != null)
            this.remove(this.BetterChat$globalLocalWidget);
        //
        if (this.BetterChat$tabs != null) {
            this.BetterChat$tabs.forEach(this::remove);
            this.BetterChat$tabs.clear();
        } else {
            this.BetterChat$tabs = new ArrayList<>();
        }
        //
        var offset = 2;
        //
        this.BetterCombat$settingsWidget = new SettingsWidget(offset, this.height - 24);
        this.addDrawableChild(this.BetterCombat$settingsWidget);
        offset += this.BetterCombat$settingsWidget.getWidth() + 1;
        //
        var allCategoryWidget = AllCategoryWidget.create();
        this.BetterChat$tabs.add(allCategoryWidget);
        allCategoryWidget.setX(offset);
        allCategoryWidget.setY(this.height - 24);
        this.addDrawableChild(allCategoryWidget);
        offset += allCategoryWidget.getWidth() + 1;
        //
        this.BetterChat$globalLocalWidget = GlobalLocalWidget.create(offset, this.height - 24);
        this.addDrawableChild(this.BetterChat$globalLocalWidget);
        offset += this.BetterChat$globalLocalWidget.getWidth() + 1;
        //
        this.BetterChat$listLeftWidget = ListLeftWidget.create(this, true);
        this.BetterChat$listLeftWidget.setX(offset);
        this.BetterChat$listLeftWidget.setY(this.height - 24);
        this.addDrawableChild(this.BetterChat$listLeftWidget);
        offset += this.BetterChat$listLeftWidget.getWidth() + 1;
        //
        //
        if (BetterChatMod.SELECTED_CATEGORY == null) {
            BetterChatMod.SELECTED_CATEGORY = BetterChatMod.COMMON_CATEGORY;
        }
        //
        for (int i = 0, j = 0; i < Math.min(BetterChatMod.CATEGORIES.size(), BetterChatMod.CHAT_VIEW_TABS_COUNT) + j; i++) {
            var category = BetterChatMod.CATEGORIES.get(this.tabListPosition + i);
            if (category == BetterChatMod.ALL_CATEGORY) {
                j++;
                continue;
            }
            var tab = new CategoryWidget(category);
            this.BetterChat$tabs.add(tab);
            tab.setX(offset);
            tab.setY(this.height - 24);
            tab.updateActive();
            this.addDrawableChild(tab);
            offset += tab.getWidth() + 1;
        }
        //
        this.BetterChat$listRightWidget = ListRightWidget.create(this, true);
        this.BetterChat$listRightWidget.setX(offset);
        this.BetterChat$listRightWidget.setY(this.height - 24);
        this.addDrawableChild(this.BetterChat$listRightWidget);
        offset += this.BetterChat$listRightWidget.getWidth() + 1;
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        this.chatField.setY(this.height - 36);
        this.BetterCombat$recalcTabsList();
        BetterChatMod.CHAT_SCREEN = this;
    }

    @Inject(method = "removed", at = @At("TAIL"))
    public void removed(CallbackInfo ci) {
        BetterChatMod.CHAT_SCREEN = null;
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    public void render(DrawContext instance, int x1, int y1, int x2, int y2, int color, Operation<Void> original) {
        original.call(instance, x1, this.height - 38, x2, this.height - 26, color);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.chatField);
            return true;
        }
        return false;
    }
}

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
import ru.cws.betterchat.gui.widget.ChatWidget;
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
    private List<ChatWidget> BetterChat$widgets;
    @Unique
    private int BetterChat$tabListPosition = 0;

    protected ChatScreenMixin(Text title) {super(title);}

    @Override
    public void BetterChat$setTabListPosition(int position) {
        this.BetterChat$tabListPosition = position;
    }

    @Override
    public int BetterChat$getTabListPosition() {
        return this.BetterChat$tabListPosition;
    }

    @Override
    public int BetterChat$getTabListMaxPosition() {
        return BetterChatMod.CATEGORIES.size() - this.BetterChat$tabs.size() + 1;
    }

    @Override
    public List<CategoryWidget> BetterChat$tabs() {
        return this.BetterChat$tabs;
    }

    @Override
    public void BetterCombat$recalcTabsList() {
        if (this.BetterChat$tabs != null) {
            this.BetterChat$tabs.forEach(this::remove);
            this.BetterChat$tabs.clear();
        } else this.BetterChat$tabs = new ArrayList<>();
        if (this.BetterChat$widgets != null) {
            this.BetterChat$widgets.forEach(this::remove);
            this.BetterChat$widgets.clear();
        } else this.BetterChat$widgets = new ArrayList<>();
        //
        var offset = 2;
        var y = this.height - 24;
        var settings = new SettingsWidget(offset, y);
        this.BetterChat$widgets.add(settings);
        this.addDrawableChild(settings);
        offset += settings.getWidth() + 1;
        var allCategory = AllCategoryWidget.create(offset, y);
//        this.BetterChat$tabs.add(allCategory); // До перерасчёта не трогаем
        this.addDrawableChild(allCategory);
        offset += allCategory.getWidth() + 1;
        var globalLocal = GlobalLocalWidget.create(offset, y);
        this.BetterChat$widgets.add(globalLocal);
        this.addDrawableChild(globalLocal);
        offset += globalLocal.getWidth() + 1;
        var listLeft = ListLeftWidget.create(offset, y, this, true);
        this.BetterChat$widgets.add(listLeft);
        this.addDrawableChild(listLeft);
        offset += listLeft.getWidth() + 1;
        var listRight = ListRightWidget.create(offset, y, this, true);
        this.BetterChat$widgets.add(listRight);
        this.addDrawableChild(listRight);
        offset += listRight.getWidth() + 1;
        //
        if (BetterChatMod.SELECTED_CATEGORY == null) {
            BetterChatMod.SELECTED_CATEGORY = BetterChatMod.COMMON_CATEGORY;
        }
        // -- Добавляем вкладки -- //
        // Сохраняем сдвиг
        var offsetSave = offset;
        // Добавляем вкладки
        for (int i = 0; i < BetterChatMod.CATEGORIES.size() - this.BetterChat$tabListPosition; i++) {
            var category = BetterChatMod.CATEGORIES.get(this.BetterChat$tabListPosition + i);
            if (category == BetterChatMod.ALL_CATEGORY)
                continue;
            if (this.width - (offset + CategoryWidget.getWidth(category)) < 0)
                break;
            var tab = new CategoryWidget(offset, y, category);
            this.BetterChat$tabs.add(tab);
            tab.updateActive();
            this.addDrawableChild(tab);
            offset += tab.getWidth() + 1;
        }
        // Расширяем вкладки с конца
        cycle: for (var reverse = this.BetterChat$tabs.reversed();;) {
            for (var tab : reverse) {
                if (this.width - offset < 3)
                    break cycle;
                tab.setWidth(tab.getWidth() + 1);
                offset++;
            }
        }
        // Выполняем перерасчёт
        offset = offsetSave;
        for (var tab : this.BetterChat$tabs) {
            tab.setX(offset);
            offset += tab.getWidth() + 1;
        }
        // Чтобы не слетало - добавляем после перерасчёта
        this.BetterChat$tabs.add(allCategory);
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        this.chatField.setY(this.height - 36);
        this.BetterCombat$recalcTabsList();
        BetterChatMod.CHAT_SCREEN = this;
    }

    @Inject(method = "resize", at = @At("TAIL"))
    public void resize(CallbackInfo ci) {
        this.BetterCombat$recalcTabsList();
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

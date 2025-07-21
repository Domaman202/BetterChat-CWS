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
import ru.cws.betterchat.gui.widget.CategoryWidget;
import ru.cws.betterchat.gui.widget.GlobalLocalWidget;
import ru.cws.betterchat.gui.widget.SettingsWidget;
import ru.cws.betterchat.util.IChatScreen;

import java.util.List;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen implements IChatScreen {
    @Shadow protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {super(title);}

    @Unique
    private SettingsWidget BetterChat$settings;
    @Unique
    private GlobalLocalWidget BetterChat$gl;
    @Unique
    private List<CategoryWidget> BetterChat$tabs;

    @Override
    public List<CategoryWidget> BetterChat$tabs() {
        return BetterChat$tabs;
    }

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        this.chatField.setY(this.height - 36);
        //
        var offset = 2;
        //
        this.BetterChat$settings = new SettingsWidget(offset, this.height - 24);
        this.addDrawableChild(this.BetterChat$settings);
        offset += this.BetterChat$settings.getWidth() + 1;
        //
        this.BetterChat$gl = new GlobalLocalWidget(offset, this.height - 24);
        this.addDrawableChild(this.BetterChat$gl);
        offset += this.BetterChat$gl.getWidth() + 1;
        //
        if (BetterChatMod.SELECTED_CATEGORY == null)
            BetterChatMod.SELECTED_CATEGORY = BetterChatMod.CATEGORIES.getFirst();
        this.BetterChat$tabs = BetterChatMod.CATEGORIES.stream().map(CategoryWidget::new).toList();
        for (var tab : this.BetterChat$tabs) {
            tab.setX(offset);
            tab.setY(this.height - 24);
            tab.updateActive();
            this.addDrawableChild(tab);
            offset += tab.getWidth() + 1;
        }
        //
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

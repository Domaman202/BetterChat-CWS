package ru.cws.betterchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.cws.betterchat.category.AllChatCategory;
import ru.cws.betterchat.category.ChatCategory;
import ru.cws.betterchat.category.CommonChatCategory;
import ru.cws.betterchat.util.ConfigHelper;
import ru.cws.betterchat.util.IChatScreen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BetterChatMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(BetterChatMod.class);
    public static final String CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "betterchat/config.json").getAbsolutePath();
    public static final String DEFAULT_CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "betterchat/default.json").getAbsolutePath();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final int SETTINGS_VIEW_TABS_COUNT = 5;
    public static final int CHAT_VIEW_TABS_COUNT = 10;
    public static final int SETTINGS_VIEW_TAB_SIZE = 80;
    public static final int CHAT_VIEW_TAB_SIZE = 79;
    public static List<ChatCategory> CATEGORIES = new ArrayList<>();
    public static ChatCategory SELECTED_CATEGORY;
    public static CommonChatCategory COMMON_CATEGORY;
    public static AllChatCategory ALL_CATEGORY;
    public static IChatScreen CHAT_SCREEN = null;
    public static boolean ALL_CHAT_VANILLA = true;
    public static boolean GLOBAL_LOCAL = true;
    public static boolean FIXED_TAB_SIZE = true;
    public static boolean NO_FLEX = false;
    public static boolean NO_HEAVY_TEXTURES = false;
    public static boolean NO_THROW = true;
    public static boolean AUTOSAVE = true;

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (SELECTED_CATEGORY == null)
                SELECTED_CATEGORY = COMMON_CATEGORY;
            tryCommand(GLOBAL_LOCAL ? "gc" : "lc");
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            for (ChatCategory category : CATEGORIES) {
                category.messages.clear();
            }
        });

        if (new File(CONFIG_FILE).exists()) {
            load(CONFIG_FILE);
        } else {
            COMMON_CATEGORY = new CommonChatCategory();
            ALL_CATEGORY = new AllChatCategory();
            CATEGORIES.add(ALL_CATEGORY);
            CATEGORIES.add(COMMON_CATEGORY);
            CATEGORIES.add(new ChatCategory("Группа", "Чат группы", null, "@", "^\\[party]", true, true));
            CATEGORIES.add(new ChatCategory("Гильдия", "Чат поселения", "tc", null, "^\\[TC]", true, true));
            CATEGORIES.add(new ChatCategory("Альянс", "Чат нации", "nc", null, "^\\[NC]", true, true));
            CATEGORIES.add(new ChatCategory("1", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("2", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("3", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("4", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("5", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("6", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("7", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("8", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("9", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("10", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("11", "", null, null, null, false, false));
            CATEGORIES.add(new ChatCategory("12", "", null, null, null, false, false));
            save(CONFIG_FILE);
            save(DEFAULT_CONFIG_FILE);
        }
    }

    public static void autosave() {
        if (AUTOSAVE) {
            save(CONFIG_FILE);
        }
    }

    public static void save(String path) {
        try {
            var file = new File(path);
            new File(file.getParent()).mkdirs();
            Files.writeString(file.toPath(), GSON.toJson(ConfigHelper.fromMod()));
        } catch (IOException e) {
            if (BetterChatMod.NO_THROW)
                LOGGER.trace("Failed to write config file", e);
            else throw new RuntimeException(e);
        }
    }

    public static void load(String path) {
        try {
            GSON.fromJson(Files.readString(Path.of(path)), ConfigHelper.class).toMod();
        } catch (IOException e) {
            if (BetterChatMod.NO_THROW)
                LOGGER.trace("Failed to read config file", e);
            else throw new RuntimeException(e);
        }
    }

    public static void tryCommand(String command) {
        try {
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
        } catch (Exception ignored) {
        }
    }
}

package ru.cws.betterchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import groovy.lang.GroovyClassLoader;
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
import ru.cws.betterchat.gui.api.IChatScreen;
import ru.cws.betterchat.util.GroovyAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BetterChatMod implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(BetterChatMod.class);
    public static final String CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "betterchat/config.json").getAbsolutePath();
    public static final String SCRIPT_PATH = new File(FabricLoader.getInstance().getConfigDir().toFile(), "betterchat/scripts").getAbsolutePath();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final int SETTINGS_VIEW_TAB_SIZE = 80;
    public static final int CHAT_VIEW_TAB_SIZE = 80;
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
    public static boolean NO_THROW = false;
    public static boolean AUTOSAVE = true;

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (SELECTED_CATEGORY == null)
                SELECTED_CATEGORY = COMMON_CATEGORY;
            tryCommand(GLOBAL_LOCAL ? "g" : "lc");
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            for (ChatCategory category : CATEGORIES) {
                category.messages.clear();
            }
        });

        COMMON_CATEGORY = new CommonChatCategory();
        ALL_CATEGORY = new AllChatCategory();
        CATEGORIES.add(ALL_CATEGORY);
        CATEGORIES.add(COMMON_CATEGORY);

        if (!new File(SCRIPT_PATH).exists()) {
            new File(SCRIPT_PATH).mkdirs();
            try (var stream = BetterChatMod.class.getResourceAsStream("/global.groovy")) {
                Files.write(new File(SCRIPT_PATH, "global.groovy").toPath(), stream.readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        load();
    }

    public static void autosave() {
        if (AUTOSAVE) {
            save();
        }
    }

    public static void save() {
        try {
            var file = new File(CONFIG_FILE);
            new File(file.getParent()).mkdirs();
            Files.writeString(file.toPath(), GSON.toJson(ConfigHelper.fromMod()));
        } catch (IOException e) {
            if (BetterChatMod.NO_THROW)
                LOGGER.trace("Failed to write config file", e);
            else throw new RuntimeException(e);
        }
    }

    public static void load() {
        try (var loader = new GroovyClassLoader()) {
            for (File file : new File(SCRIPT_PATH).listFiles()) {
                try {
                    loader.parseClass(file).getMethod("main", GroovyAdapter.class).invoke(null, GroovyAdapter.INSTANCE);
                } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    if (BetterChatMod.NO_THROW)
                        LOGGER.trace("Failed to read script file {}", file.getName(), e);
                    else throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            if (BetterChatMod.NO_THROW)
                LOGGER.trace("Failed to read scripts ", e);
            else throw new RuntimeException(e);
        }

        try {
            GSON.fromJson(Files.readString(Path.of(CONFIG_FILE)), ConfigHelper.class).toMod();
        } catch (IOException e) {
            if (BetterChatMod.NO_THROW)
                LOGGER.trace("Failed to read config file", e);
            else throw new RuntimeException(e);
        }

        save();
    }

    public static void tryCommand(String command) {
        try {
            MinecraftClient.getInstance().player.networkHandler.sendChatCommand(command);
        } catch (Exception ignored) {
        }
    }
}

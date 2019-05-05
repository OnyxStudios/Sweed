package nerdhub.sweed.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nerdhub.sweed.Sweed;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/**
 * used to load and store config objects.<br/>
 * serialization to/from json files.
 *
 * @deprecated will be dropped when fabric gets it's own config system
 */
@Deprecated
public class ConfigHandler {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create();

    private static final Map<Class<?>, Object> CONFIG_OBJECTS = new IdentityHashMap<>();
    private static final Map<Class<?>, String> CONFIG_ID_LOOKUP = new IdentityHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getConfig(Class<T> clazz) {
        if(!CONFIG_OBJECTS.containsKey(clazz)) {
            throw new IllegalStateException("config not registered before accessing: " + clazz.getCanonicalName());
        }
        return (T) CONFIG_OBJECTS.get(clazz);
    }

    /**
     * @param configName the filename of the config (without .json extension)
     */
    public static <T> T registerConfig(String configName, Class<T> configClass) {
        CONFIG_ID_LOOKUP.put(configClass, configName + ".json");
        return reloadConfig(configClass);
    }

    public static <T> T reloadConfig(Class<T> configClass) {
        File configFile = new File(FabricLoader.getInstance().getConfigDirectory(), CONFIG_ID_LOOKUP.get(configClass));
        if(!configFile.exists()) {
            try(FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(configClass.newInstance(), writer);
            }
            catch (IOException | IllegalAccessException | InstantiationException e) {
                Sweed.logger.error("unable to write config file for {}", configClass::getCanonicalName);
                Sweed.logger.trace("file location: " + configFile.getAbsolutePath(), e);
            }
        }
        T config;
        try(FileReader reader = new FileReader(configFile)) {
            config = GSON.fromJson(reader, configClass);
        }
        catch (IOException e) {
            Sweed.logger.error("unable to read config file from " + configFile.getAbsolutePath() + ", falling back to default values!", e);
            try {
                config = configClass.newInstance();
            }
            catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }
        CONFIG_OBJECTS.put(configClass, config);
        return config;
    }

    public static Set<Class<?>> getRegisteredConfigs() {
        return CONFIG_OBJECTS.keySet();
    }
}
package ga.codesplash.scrumplex.sprummlbot.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginManager {

    public Map<File, Plugin> plugins = new HashMap<>();

    /**
     * Retruns a list of all active plugins
     * @return
     */
    public List<Plugin> getPlugins() {
        return new ArrayList<>(plugins.values());
    }

    /**
     * Retruns a Plugin by a File
     * @param f
     * File of tha Plugin
     * @return
     * Retruns a Plugin
     */
    public Plugin getPluginByFile(File f) {
        return plugins.get(f);
    }

    /**
     * Retruns a Plugin by a SprummlPlugin
     * @param sprummlPlugin
     * The SprummlPlugin
     * @return
     * Retruns a Plugin or null
     */
    public Plugin getPluginBySprummlPlugin(SprummlPlugin sprummlPlugin) {
        for (Plugin plugin : getPlugins()) {
            if (plugin.getPlugin().equals(sprummlPlugin))
                return plugin;
        }
        return null;
    }

    /**
     * Retruns a Plugin by a name
     * @param pluginName
     * Id of tha plugin, which will be returned
     * @return
     * Retruns a Plugin or null
     */
    public Plugin getPluginByName(String pluginName) {
        for (Plugin plugin : getPlugins()) {
            if (plugin.getName().equalsIgnoreCase(pluginName))
                return plugin;
        }
        return null;
    }

    /**
     * Retruns if Plugin is loaded or not
     * @param plugin
     * Plugin, which will be checked
     * @return
     */
    public boolean isLoaded(Plugin plugin) {
        return plugins.containsValue(plugin);
    }

    /**
     * Retruns if Plugin is loaded or not
     * @param plugin
     * Plugin, which will be checked
     * @return
     */
    public boolean isLoaded(String pluginName) {
        return (getPluginByName(pluginName) != null);
    }

}

package dk.plexhost.core;

import dk.plexhost.core.enums.Hook;
import dk.plexhost.core.hooks.IHook;
import dk.plexhost.core.hooks.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PlexPlugin extends JavaPlugin {

    private static final HashMap<String, Plugin> DEPENDANTS = new HashMap<>();
    private static final HashMap<Hook, Boolean> HOOKS = new HashMap<>();
    private static PlexPlugin INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getLogger().info("Loading dependant plugins.");
        for(Plugin dependant : getServer().getPluginManager().getPlugins()){
            PluginDescriptionFile pdf = dependant.getDescription();
            if(pdf.getDepend().contains(getName()) || pdf.getSoftDepend().contains(getName()))
                DEPENDANTS.put(dependant.getName(), dependant);
        }
        Bukkit.getLogger().info(String.format("Loaded dependants (%d): %s", DEPENDANTS.size(), DEPENDANTS.values()));

        Bukkit.getLogger().info("Initialising hooks...");
        initialiseHooks();
    }

    private void initialiseHooks(){
        IHook[] hooks = new IHook[]{
                new VaultHook()
        };
        for(IHook hook : hooks)
            HOOKS.put(hook.getEnum(), hook.init(this));
    }

    public static boolean isHookInitialised(Hook paramHook) {
        return HOOKS.getOrDefault(paramHook, false);
    }

    public static PlexPlugin getInstance(){
        return INSTANCE;
    }
}

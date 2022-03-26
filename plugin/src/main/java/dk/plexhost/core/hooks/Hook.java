package dk.plexhost.core.hooks;

import dk.plexhost.core.interfaces.IHook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public abstract class Hook implements IHook {

    private final String name;
    private final dk.plexhost.core.enums.Hook hook;
    private final boolean isEnabled;

    public Hook(String paramString, dk.plexhost.core.enums.Hook paramHook){
        this.name = paramString;
        this.hook = paramHook;
        if(paramHook.isBuiltIn())
            this.isEnabled = true;
        else this.isEnabled = Bukkit.getPluginManager().getPlugin(getName()) != null && Bukkit.getPluginManager().getPlugin(getName()).isEnabled();
    }

    @Override
    public boolean isEnabled(){
        return isEnabled;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull dk.plexhost.core.enums.Hook getEnum() {
        return hook;
    }

}

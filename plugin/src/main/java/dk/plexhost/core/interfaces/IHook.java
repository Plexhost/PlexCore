package dk.plexhost.core.interfaces;

import dk.plexhost.core.enums.Hook;
import org.bukkit.plugin.java.JavaPlugin;

public interface IHook {

    String getName();

    Hook getEnum();

    boolean isEnabled();

    boolean init(JavaPlugin plugin);
}

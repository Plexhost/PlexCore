package dk.plexhost.core.command;

import org.bukkit.command.CommandSender;

public abstract class ISubCommand {

    private final String name;
    private final String[] aliases;

    public ISubCommand(String name){
        this.name = name;
        this.aliases = new String[0];
    }

    public ISubCommand(String name, String... aliases){
        this.name = name;
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public abstract void onCommand(CommandSender sender, String[] arguments, String label);
}


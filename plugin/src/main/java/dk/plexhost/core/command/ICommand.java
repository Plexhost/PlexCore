package dk.plexhost.core.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class ICommand implements CommandExecutor {

    private final Collection<ISubCommand> subCommands = new ArrayList<>();
    private ISubCommand defaultCommand = null;
    public ICommand(JavaPlugin plugin, String command){
        if(plugin.getCommand(command) != null){
            plugin.getCommand(command).setExecutor(this);
        }
    }

    protected void setDefaultCommand(ISubCommand subCommand){
        this.defaultCommand = subCommand;
    }

    protected ISubCommand getDefaultCommand(){
        return this.defaultCommand;
    }

    protected Collection<ISubCommand> getSubCommands(){
        return this.subCommands;
    }

    protected void addSubCommand(ISubCommand subCommand){
        this.subCommands.add(subCommand);
    }

    protected void addSubCommands(ISubCommand... subCommands){
        this.subCommands.addAll(Arrays.asList(subCommands));
    }


    protected ISubCommand findSubCommand(String command){
        for(ISubCommand subCommand : getSubCommands()){
            if(subCommand.getName().equalsIgnoreCase(command)) return subCommand;
            for(String alias : subCommand.getAliases())
                if(alias.equalsIgnoreCase(command))
                    return subCommand;
        }
        return getDefaultCommand();
    }

    protected void execute(CommandSender sender, ISubCommand subCommand, String[] arguments) {
        String[] newArguments = null;
        if (arguments.length > 0) {
            // Remove first element of list.
            newArguments = new String[arguments.length - 1];
            System.arraycopy(arguments, 1, newArguments, 0, arguments.length - 1);
        }
        subCommand.onCommand(sender,
                newArguments != null ? newArguments : new String[0],
                arguments.length > 0 ? arguments[0] : ""
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length > 0) execute(sender, findSubCommand(args[0]), args);
        else execute(sender, getDefaultCommand(), args);
        return true;
    }
}


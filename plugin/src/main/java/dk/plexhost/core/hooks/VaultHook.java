package dk.plexhost.core.hooks;

import dk.plexhost.core.exception.HookNotEnabledException;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Hook for interacting with Vault (Economy, Chat & Permission)
 */
public class VaultHook extends Hook {

    private static Economy ECONOMY = null;
    private static Chat CHAT = null;
    private static Permission PERMISSION = null;

    private static String ECONOMY_EXCEPTION = "Trying to use Vault Economy Provider, but non was provided during initialising.";

    public VaultHook() {
        super("Vault", dk.plexhost.core.enums.Hook.VAULT);
    }

    /**
     *
     * Initialising the {@link VaultHook}.
     *
     * @param paramPlugin The core plugin.
     * @return if the hook is established currently.
     */
    @Override
    public boolean init(JavaPlugin paramPlugin) {
        if(!super.isEnabled()) return false;

        RegisteredServiceProvider<Economy> rspEconomy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<Chat> rspChat = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        RegisteredServiceProvider<Permission> rspPermission = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if(rspEconomy != null) ECONOMY = rspEconomy.getProvider();
        else Bukkit.getLogger().severe("[VaultHook] No Economy Provider was found.");

        if(rspChat != null) CHAT = rspChat.getProvider();
        else Bukkit.getLogger().severe("[VaultHook] No Chat Provider was found.");

        if(rspPermission != null) PERMISSION = rspPermission.getProvider();
        else Bukkit.getLogger().severe("[VaultHook] No Permission Provider was found.");

        return (
                ECONOMY != null
                && CHAT != null
                && PERMISSION != null
        );
    }

    /**
     *
     * Get the raw Vault {@link Economy} instance if initialised.
     *
     * @return Raw Vault {@link Economy} instance.
     */
    public Economy getEconomy(){
        return ECONOMY;
    }

    /**
     *
     * Check if an {@link OfflinePlayer} has a specific amount of money.
     *
     * @param paramOfflinePlayer The {@link OfflinePlayer} to check.
     * @param paramDouble The amount of money to check
     * @return true if the {@link OfflinePlayer}'s balance is greater or equal
     */
    public static boolean canAfford(OfflinePlayer paramOfflinePlayer, double paramDouble){
        if(ECONOMY == null)
            throw new HookNotEnabledException(ECONOMY_EXCEPTION);
        return getBalance(paramOfflinePlayer) >= paramDouble;
    }

    /**
     *
     * Get the balance of an {@link OfflinePlayer}
     *
     * @param paramOfflinePlayer The {@link OfflinePlayer} to get the balance of.
     * @return the balance of the {@link OfflinePlayer}
     */
    public static double getBalance(OfflinePlayer paramOfflinePlayer){
        if(ECONOMY == null)
            throw new HookNotEnabledException(ECONOMY_EXCEPTION);
        return ECONOMY.getBalance(paramOfflinePlayer);
    }

    /**
     *
     * Withdraw money from an {@link OfflinePlayer}'s balance.
     *
     * @param paramOfflinePlayer the {@link OfflinePlayer} to withdraw from.
     * @param paramDouble The amount to withdraw
     */
    public static void withdrawBalance(OfflinePlayer paramOfflinePlayer, double paramDouble){
        if(ECONOMY == null)
            throw new HookNotEnabledException(ECONOMY_EXCEPTION);
        ECONOMY.withdrawPlayer(paramOfflinePlayer, paramDouble);
    }

    public static void removeBalance(OfflinePlayer paramOfflinePlayer, double paramDouble){
        withdrawBalance(paramOfflinePlayer, paramDouble);
    }

    /**
     *
     * Deposit money from to {@link OfflinePlayer}'s balance.
     *
     * @param paramOfflinePlayer the {@link OfflinePlayer} to deposit to.
     * @param paramDouble The amount to deposit
     */
    public static void depositBalance(OfflinePlayer paramOfflinePlayer, double paramDouble){
        if(ECONOMY == null)
            throw new HookNotEnabledException(ECONOMY_EXCEPTION);
        ECONOMY.depositPlayer(paramOfflinePlayer, paramDouble);
    }

    public static void addBalance(OfflinePlayer paramOfflinePlayer, double paramDouble){
        depositBalance(paramOfflinePlayer, paramDouble);
    }

}

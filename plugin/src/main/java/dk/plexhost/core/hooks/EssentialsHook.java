package dk.plexhost.core.hooks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.Kit;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public class EssentialsHook extends Hook{

    private static JavaPlugin PLUGIN = null;
    private static Essentials INSTANCE = null;

    public EssentialsHook() {
        super("Essentials", dk.plexhost.core.enums.Hook.ESSENTIALS);
    }

    @Override
    public boolean init(JavaPlugin plugin) {
        if(!plugin.isEnabled()) return false;
        PLUGIN = plugin;
        INSTANCE = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        return true;
    }

    public static User getUser(Player player){
        return INSTANCE.getUser(player);
    }

    public static void spawnUser(Player player){
        getUser(player).getAsyncTeleport().now(player, false, PlayerTeleportEvent.TeleportCause.COMMAND, new CompletableFuture<>());
    }

    public static Kit getKit(String kit){
        try{
            return new Kit(kit, INSTANCE);
        } catch(Exception e){
            return null;
        }
    }

    public static boolean isKitDelayed(Player player, Kit kit){
        try {
            long nextUse = kit.getNextUse(getUser(player));
            if(nextUse == 0) return false;
            return true;
        } catch (Exception ignored){
            return false;
        }
    }

    public static long getKitDelay(Player player, Kit kit){
        try {
            return kit.getNextUse(getUser(player));
        } catch (Exception ignored){
            return 0L;
        }
    }

    public static void giveKit(Player player, Kit kit) {
        try{
            User user = getUser(player);
            kit.setTime(user);
            kit.expandItems(user);
        } catch (Exception ignored) {}
    }

    public static net.ess3.api.IItemDb getItemDB() {
        return INSTANCE.getItemDb();
    }

    public static int getUniqueUsers(){
        return INSTANCE.getUserMap().getUniqueUsers();
    }

    public static Essentials getInstance() {
        return INSTANCE;
    }
}

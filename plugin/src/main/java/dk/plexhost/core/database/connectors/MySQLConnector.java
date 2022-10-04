package dk.plexhost.core.database.connectors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnector implements DatabaseConnector {
    private final Plugin plugin;
    private HikariDataSource hikari;
    private boolean initializedSuccessfully;

    public MySQLConnector(Plugin plugin, String hostname, int port, String database, String username, String password, boolean useSSL) {
        this.plugin = plugin;

        plugin.getLogger().info("connecting to " + hostname + " : " + port);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(3);

        try {
            this.hikari = new HikariDataSource(config);
            this.initializedSuccessfully = true;
        } catch (Exception ex) {
            this.initializedSuccessfully = false;
        }
    }

    @Override
    public boolean isInitialized() {
        return this.initializedSuccessfully;
    }

    @Override
    public void closeConnection() {
        this.hikari.close();
    }

    @Override
    public void connect(ConnectionCallback callback) {
        try (Connection connection = this.hikari.getConnection()) {
            callback.accept(connection);
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

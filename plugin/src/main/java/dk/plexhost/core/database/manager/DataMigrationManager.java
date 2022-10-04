package dk.plexhost.core.database.manager;

import dk.plexhost.core.database.connectors.DatabaseConnector;
import dk.plexhost.core.database.connectors.MySQLConnector;
import dk.plexhost.core.database.connectors.SQLiteConnector;
import dk.plexhost.core.database.migration.DataMigration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataMigrationManager {
    private final List<DataMigration> migrations;
    private final DatabaseConnector databaseConnector;
    private final DataManagerAbstract dataManagerAbstract;

    public DataMigrationManager(DatabaseConnector databaseConnector, DataManagerAbstract dataManagerAbstract, DataMigration... migrations) {
        this.databaseConnector = databaseConnector;
        this.dataManagerAbstract = dataManagerAbstract;

        this.migrations = Arrays.asList(migrations);
    }

    /**
     * Runs any needed data migrations
     */
    public void runMigrations() {
        this.databaseConnector.connect((connection -> {
            int currentMigration = -1;
            boolean migrationsExist;

            String query;
            if (this.databaseConnector instanceof SQLiteConnector) {
                query = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";
            } else {
                query = "SHOW TABLES LIKE ?";
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, this.getMigrationsTableName());
                migrationsExist = statement.executeQuery().next();
            }

            if (!migrationsExist) {
                // No migration table exists, create one
                String createTable = "CREATE TABLE " + this.getMigrationsTableName() + " (migration_version INT NOT NULL)";
                try (PreparedStatement statement = connection.prepareStatement(createTable)) {
                    statement.execute();
                }

                // Insert primary row into migration table
                String insertRow = "INSERT INTO " + this.getMigrationsTableName() + " VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(insertRow)) {
                    statement.setInt(1, -1);
                    statement.execute();
                }
            } else {
                // Grab the current migration version
                String selectVersion = "SELECT migration_version FROM " + this.getMigrationsTableName();
                try (PreparedStatement statement = connection.prepareStatement(selectVersion)) {
                    ResultSet result = statement.executeQuery();
                    result.next();
                    currentMigration = result.getInt("migration_version");
                }
            }

            // Grab required migrations
            int finalCurrentMigration = currentMigration;
            List<DataMigration> requiredMigrations = this.migrations.stream()
                    .filter(x -> x.getRevision() > finalCurrentMigration)
                    .sorted(Comparator.comparingInt(DataMigration::getRevision))
                    .collect(Collectors.toList());

            // Nothing to migrate, abort
            if (requiredMigrations.isEmpty()) {
                return;
            }

            // Migrate the data
            for (DataMigration dataMigration : requiredMigrations) {
                dataMigration.migrate(this.databaseConnector instanceof MySQLConnector, connection, this.dataManagerAbstract.getTablePrefix());
            }

            // Set the new current migration to be the highest migrated to
            currentMigration = requiredMigrations.stream()
                    .map(DataMigration::getRevision)
                    .max(Integer::compareTo)
                    .orElse(-1);

            String updateVersion = "UPDATE " + this.getMigrationsTableName() + " SET migration_version = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateVersion)) {
                statement.setInt(1, currentMigration);
                statement.execute();
            }
        }));
    }

    /**
     * @return the name of the migrations table
     */
    private String getMigrationsTableName() {
        return this.dataManagerAbstract.getTablePrefix() + "migrations";
    }
}


package org.databasemanager;

import org.model.CsvRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseManager {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void upsertRecords(List<CsvRecord> csvRecords) throws SQLException {
        String sql = "INSERT INTO data_from_csv (code, name, description, start_date, modified_on) VALUES (?, ?, ?, ?, NOW()) " +
                "ON CONFLICT (code) DO UPDATE SET " +
                "name = EXCLUDED.name, " +
                "description = EXCLUDED.description, " +
                "start_date = EXCLUDED.start_date, " +
                "modified_on = NOW()";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (CsvRecord csvRecord : csvRecords) {
                statement.setString(1, csvRecord.getCode());
                statement.setString(2, csvRecord.getName());
                statement.setString(3, csvRecord.getDescription());
                statement.setObject(4, csvRecord.getStartDate());
                statement.addBatch();

                if (csvRecords.indexOf(csvRecord) % 100 == 0) {
                    statement.executeBatch();
                }
            }
            statement.executeBatch();
        }
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

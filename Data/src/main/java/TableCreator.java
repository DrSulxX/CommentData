import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {
    public static void createTable() {
        Connection connection = SQLiteConnection.getConnection();
        try {
            Statement statement = connection.createStatement();
            String createTableQuery = "CREATE TABLE IF NOT EXISTS comments (" +
                                      "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                      "name TEXT, " +
                                      "email TEXT, " +
                                      "comment TEXT, " +
                                      "creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"; // Add creation_date column
            statement.executeUpdate(createTableQuery);
            statement.close();
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
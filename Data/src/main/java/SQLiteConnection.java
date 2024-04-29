import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SQLiteConnection {
    private static Connection connection;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:comments.db");
            System.out.println("Connected to the SQLite database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Closed SQLite database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void addComment(String name, String email, String comment, Timestamp creationDate) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO comments (name, email, comment, creation_date) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, comment);
            preparedStatement.setTimestamp(4, creationDate); // Set the creation date
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Comment added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
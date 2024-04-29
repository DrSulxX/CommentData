import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class InsertRandomData {

    public static void main(String[] args) {
        Connection connection = null;

        try {
            SQLiteConnection.connect(); // Establish the database connection
            connection = SQLiteConnection.getConnection();

            if (connection == null) {
                System.err.println("Failed to connect to the database.");
                return;
            }

            createTable(connection); // Create the 'comments' table if not exists
            insertRandomData(connection);
            System.out.println("Successfully inserted records.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS comments (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "name TEXT NOT NULL," +
                     "email TEXT NOT NULL," +
                     "comment TEXT NOT NULL," +
                     "creation_date TIMESTAMP NOT NULL" +
                     ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    private static void insertRandomData(Connection connection) throws SQLException {
        String insertSql = "INSERT INTO comments (name, email, comment, creation_date) VALUES (?, ?, ?, ?)";
        RandomDataGenerator generator = new RandomDataGenerator();
        Random random = new Random();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
            connection.setAutoCommit(false); // Begin transaction

            for (int i = 0; i < 100; i++) {
                String name = generator.generateRandomName();
                String email = generator.generateRandomEmail(name);
                String comment = generator.generateRandomComment();
                Timestamp creationDate = Timestamp.from(Instant.now());

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, comment);
                preparedStatement.setTimestamp(4, creationDate);

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            connection.commit(); // Commit the transaction
        } catch (SQLException e) {
            connection.rollback(); // Rollback the transaction on error
            throw e;
        }
    }
}

class RandomDataGenerator {
    private Random random = new Random();

    // Generate random names
    public String generateRandomName() {
        String[] names = {"John", "Mary", "David", "Lisa", "Michael", "Sarah", "James", "Emily", "Robert", "Emma"};
        return names[random.nextInt(names.length)];
    }

    // Generate random emails based on the name
    public String generateRandomEmail(String name) {
        String[] domains = {"example.com", "test.com", "company.com", "email.com"};
        String randomDomain = domains[random.nextInt(domains.length)];
        return name.toLowerCase() + "@" + randomDomain;
    }

    // Generate random comments
    public String generateRandomComment() {
        String[] comments = {
            "Great product!",
            "Very helpful information.",
            "I enjoyed reading this.",
            "Could be improved.",
            "Not what I expected.",
            "Interesting topic.",
            "Well written.",
            "Useful insights."
        };
        return comments[random.nextInt(comments.length)];
    }
}


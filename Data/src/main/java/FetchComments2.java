import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FetchComments2 extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable commentsTable;
    private JButton returnButton;

    public FetchComments2() {
        setTitle("Fetch Comments");
        setSize(800, 400); // Adjusted size for better layout
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Create a table to display comments
        commentsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(commentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(e -> {
            dispose(); // Close the current window
            new MainGui(); // Open the main menu
        });
        panel.add(returnButton, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);

        fetchComments();
    }

    private void fetchComments() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            SQLiteConnection.connect(); // Establish the database connection
            connection = SQLiteConnection.getConnection();

            if (connection == null) {
                System.err.println("Failed to connect to the database.");
                JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
                return;
            }

            statement = connection.createStatement();

            // Execute the query to fetch comments, names, emails, and creation dates
            resultSet = statement.executeQuery("SELECT name, email, comment, creation_date FROM comments");

            // Create a table model with columns for names, emails, comments, and creation dates
            DefaultTableModel tableModel = new DefaultTableModel(
                    new Object[]{"Name", "Email", "Comment", "Creation Date"}, 0);

            // Populate the table model with data from the result set
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String comment = resultSet.getString("comment");
                Timestamp creationDate = resultSet.getTimestamp("creation_date");
                tableModel.addRow(new Object[]{name, email, comment, creationDate});
            }

            // Set the table model to the JTable
            commentsTable.setModel(tableModel);

            // Set column widths to accommodate content
            commentsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Name
            commentsTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Email
            commentsTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Comment
            commentsTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Creation Date

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch comments. Error: " + e.getMessage());
        } finally {
            // Close the resources in the finally block to ensure they are always closed
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FetchComments2::new);
    }
}
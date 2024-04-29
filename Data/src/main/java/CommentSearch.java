import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CommentSearch extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField searchField;
    private JButton searchButton;
    private JButton backButton;
    private JTable commentsTable;
    private JLabel noDataLabel;

    public CommentSearch() {
        setTitle("Search Comments");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        backButton = new JButton("Back to Main");

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                searchComments(searchText);
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainGui(); // Open the Main GUI window when Back button is clicked
            }
        });

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(backButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Initialize table and label
        commentsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(commentsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        noDataLabel = new JLabel("No data found.");
        noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(noDataLabel, BorderLayout.SOUTH);

        // Initially hide the table and "No data found" label
        commentsTable.setVisible(false);
        noDataLabel.setVisible(false);

        add(panel);
        setVisible(true);
    }

    private void searchComments(String searchText) {
        SQLiteConnection.connect();
        Connection connection = SQLiteConnection.getConnection();
        if (connection == null) {
            System.err.println("Failed to connect to the database.");
            return;
        }

        try {
            String sqlQuery = "SELECT name, email, comment FROM comments WHERE name LIKE ? OR email LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, "%" + searchText + "%");
            preparedStatement.setString(2, "%" + searchText + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.setColumnIdentifiers(new String[]{"Name", "Email", "Comment"});

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String comment = resultSet.getString("comment");
                tableModel.addRow(new String[]{name, email, comment});
            }

            commentsTable.setModel(tableModel);

            // Show or hide the table and "No data found" label based on search results
            if (tableModel.getRowCount() == 0) {
                commentsTable.setVisible(false);
                noDataLabel.setVisible(true);
            } else {
                commentsTable.setVisible(true);
                noDataLabel.setVisible(false);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve comments. Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CommentSearch::new);
    }
}









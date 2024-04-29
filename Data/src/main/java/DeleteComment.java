import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteComment extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField idField;
    private JButton deleteButton;
    private JButton returnButton; // Button to return to the main GUI

    public DeleteComment() {
        setTitle("Delete Comment");
        setSize(400, 200); // Adjusted size for better layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout());

        panel.add(new JLabel("Enter Comment ID to Delete:"));
        idField = new JTextField(15);
        panel.add(idField);

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                if (!id.isEmpty()) {
                    deleteComment(id);
                } else {
                    JOptionPane.showMessageDialog(DeleteComment.this, "Please enter a valid Comment ID.");
                }
            }
        });
        panel.add(deleteButton);

        returnButton = new JButton("Return to Main Menu");
        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current Delete Comment window
                new MainGui(); // Open the Main GUI window
            }
        });
        panel.add(returnButton);

        add(panel);
        setVisible(true);
    }

    private void deleteComment(String id) {
        try {
            SQLiteConnection.connect(); // Ensure that the connection is established
            Connection connection = SQLiteConnection.getConnection();

            if (connection == null) {
                System.err.println("Failed to connect to the database.");
                return;
            }

            String sql = "DELETE FROM comments WHERE name LIKE ? OR email LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + id + "%");
            preparedStatement.setString(2, "%" + id + "%");

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Comment(s) deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No matching comment(s) found for the provided Name or Email.");
            }

            preparedStatement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to delete comment(s). Error: " + ex.getMessage());
        } finally {
            SQLiteConnection.closeConnection(); // Close the database connection
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DeleteComment();
            }
        });
    }
}
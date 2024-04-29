import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.Instant;

public class CommentForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField commentField;
    private JButton addButton;
    private JButton backButton;

    public CommentForm() {
        setTitle("Add Comment");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Comment:"));
        commentField = new JTextField();
        panel.add(commentField);

        addButton = new JButton("Add Comment");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String comment = commentField.getText();

                // Check if any of the required fields are empty
                if (name.isEmpty() || email.isEmpty() || comment.isEmpty()) {
                    // Display an error message if any field is empty
                    String errorMessage = "Please fill out all fields: ";
                    if (name.isEmpty()) {
                        errorMessage += "Name ";
                    }
                    if (email.isEmpty()) {
                        errorMessage += "Email ";
                    }
                    if (comment.isEmpty()) {
                        errorMessage += "Comment";
                    }
                    errorMessage += ".";
                    
                    // Show a dialog with the error message
                    JOptionPane.showMessageDialog(CommentForm.this, errorMessage, "Incomplete Fields", JOptionPane.ERROR_MESSAGE);
                } else {
                    // All fields are filled, proceed to add the comment
                    Timestamp currentDate = Timestamp.from(Instant.now());

                    SQLiteConnection.connect();
                    TableCreator.createTable();
                    SQLiteConnection.addComment(name, email, comment, currentDate); // Pass currentDate

                    dispose(); // Close the current CommentForm window
                    new MainGui(); // Open the MainGui window
                }
            }
        });
        panel.add(addButton);

        backButton = new JButton("Back to Main");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the current CommentForm window
                dispose();
                
                // Open the MainGui window
                new MainGui().setVisible(true);
            }
        });
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new CommentForm();
    }
}
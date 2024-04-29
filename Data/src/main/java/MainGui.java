import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainGui extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton addCommentButton;
    private JButton viewCommentsButton;
    private JButton searchCommentsButton;
    private JButton deleteCommentButton;
    private JButton exitButton; // New button for exiting the program

    public MainGui() {
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10)); // Increased spacing and added an extra row

        addCommentButton = new JButton("Add Comment");
        addCommentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CommentForm().setVisible(true);
            }
        });
        panel.add(addCommentButton);

        viewCommentsButton = new JButton("View Comments");
        viewCommentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new FetchComments2().setVisible(true);
            }
        });
        panel.add(viewCommentsButton);

        searchCommentsButton = new JButton("Search Comments");
        searchCommentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CommentSearch().setVisible(true);
            }
        });
        panel.add(searchCommentsButton);

        deleteCommentButton = new JButton("Delete Comment");
        deleteCommentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeleteComment().setVisible(true);
            }
        });
        panel.add(deleteCommentButton);

        exitButton = new JButton("Exit"); // New exit button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(MainGui.this, "Are you sure you want to exit?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0); // Terminate the application
                }
            }
        });
        panel.add(exitButton);

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainGui();
            }
        });
    }
}
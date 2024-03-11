import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizLogin extends JFrame {

    private JLabel usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public QuizLogin() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Quiz Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameLabel = new JLabel("Username:");
        panel.add(usernameLabel);

        usernameField = new JTextField();
        panel.add(usernameField);

        passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (isValidUser(username, password)) {
                    openQuizApplication();
                } else {
                    JOptionPane.showMessageDialog(QuizLogin.this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(loginButton);

        add(panel);
    }

    private boolean isValidUser(String username, String password) {
        // You can implement your own logic to validate the username and password here
        // For simplicity, let's use a hard-coded username and password
        return username.equals("admin") && password.equals("admin");
    }

    private void openQuizApplication() {
        new QuizApplication().setVisible(true);
        dispose(); // Close the login window after successful login
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizLogin().setVisible(true);
            }
        }
        );
        
    }
}

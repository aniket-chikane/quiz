import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class QuizApplication extends JFrame {

    private static final String[][] QUESTIONS = {
            {"What is the capital of France?", "A. London", "B. Paris", "C. Rome", "D. Berlin", "B"},
            {"What is the largest planet in our solar system?", "A. Earth", "B. Jupiter", "C. Mars", "D. Saturn", "B"},
            {"Who wrote 'To Kill a Mockingbird'?", "A. J.K. Rowling", "B. Harper Lee", "C. William Shakespeare", "D. Mark Twain", "B"},
            // Add more questions here
    };

    private int currentIndex = 0;
    private int score = 0;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private JButton nextButton, prevButton; // Added previous button

    public QuizApplication() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Quiz Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 230, 230)); // Set background color

        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(230, 230, 230)); // Set panel background color

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        questionLabel = new JLabel(QUESTIONS[currentIndex][0]);
        questionLabel.setForeground(Color.WHITE); // Set text color
        centerPanel.add(questionLabel, gbc);

        ButtonGroup optionGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        gbc.gridwidth = 1;
        gbc.gridy++;
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton(QUESTIONS[currentIndex][i + 1]);
            optionButtons[i].setForeground(Color.WHITE); // Set text color
            optionButtons[i].setBackground(new Color(100, 100, 100)); // Set button background color
            optionButtons[i].setOpaque(true); // Ensure background color is visible
            optionGroup.add(optionButtons[i]);
            centerPanel.add(optionButtons[i], gbc);
            gbc.gridy++;
        }

        add(centerPanel, BorderLayout.CENTER);

        prevButton = new JButton("Previous"); // Added previous button
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex > 0) {
                    currentIndex--;
                    displayQuestion(currentIndex);
                }
            }
        });
        add(prevButton, BorderLayout.WEST);

        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(0, 153, 204)); // Set button background color
        nextButton.setForeground(Color.WHITE); // Set text color
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
                currentIndex++;

                if (currentIndex < QUESTIONS.length) {
                    displayQuestion(currentIndex);
                } else {
                    showResult();
                }
            }
        });
        add(nextButton, BorderLayout.SOUTH);
    }

    private void displayQuestion(int index) {
        questionLabel.setText(QUESTIONS[index][0]);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(QUESTIONS[index][i + 1]);
            optionButtons[i].setSelected(false);
        }
    }

    private void checkAnswer() {
        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected() && optionButtons[i].getText().charAt(0) == QUESTIONS[currentIndex][5].charAt(0)) {
                score++;
                break;
            }
        }
    }

    private void showResult() {
    String username = "example_user"; // You need to get the username from your login mechanism
    String connectionString = "jdbc:sqlite:quiz.db";
    String insertQuery = "INSERT INTO quiz_results (username, score, timestamp) VALUES (?, ?, ?)";

    try (Connection connection = DriverManager.getConnection(connectionString);
         PreparedStatement statement = connection.prepareStatement(insertQuery)) {

        // Set parameters for the query
        statement.setString(1, username);
        statement.setInt(2, score);
        statement.setLong(3, new Date().getTime()); // Use current timestamp

        // Execute the query
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            JOptionPane.showMessageDialog(this, "Quiz completed!\nYour score: " + score + "/" + QUESTIONS.length +
                    "\nQuiz results stored in the database.");
        } else {
            JOptionPane.showMessageDialog(this, "Quiz completed!\nYour score: " + score + "/" + QUESTIONS.length +
                    "\nFailed to store quiz results in the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Quiz completed!\nYour score: " + score + "/" + QUESTIONS.length +
                "\nFailed to store quiz results in the database.\nError: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QuizApplication().setVisible(true);
            }
        });
    }
}

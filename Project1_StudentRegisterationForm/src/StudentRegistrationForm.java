import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistrationForm extends JFrame {
    private JTextField nameField, ageField, emailField, courseField;
    private JButton submitButton;

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/StudentDB?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASS = "vineet12@11";

    private Connection conn;

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Labels and Fields
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Course:"));
        courseField = new JTextField();
        add(courseField);

        submitButton = new JButton("Submit");
        add(submitButton);

        submitButton.addActionListener(e -> submitForm());

        setVisible(true);

        // Fix: Load MySQL driver explicitly
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load driver
            conn = DriverManager.getConnection(URL, USER, PASS); // Connect to DB
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "MySQL Driver not found: " + e.getMessage());
            System.exit(0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + ex.getMessage());
            System.exit(0);
        }
    }

    private void submitForm() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String email = emailField.getText();
        String course = courseField.getText();

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty() || course.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
            return;
        }

        String sql = "INSERT INTO students (name, age, email, course) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, email);
            ps.setString(4, course);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student registered successfully!");
            nameField.setText("");
            ageField.setText("");
            emailField.setText("");
            courseField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistrationForm::new);
    }
}

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class StudentRegistrationForm extends JFrame {

    private JTextField nameField, ageField, emailField;
    private JComboBox<String> courseCombo;
    private JButton submitButton;
    private JTable studentTable;

    private static final String URL = "jdbc:mysql://localhost:3306/StudentDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "vineet12@11";

    private Connection conn;

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        formPanel.add(ageField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Course:"));
        courseCombo = new JComboBox<>();
        formPanel.add(courseCombo);

        submitButton = new JButton("Submit");
        formPanel.add(new JLabel());
        formPanel.add(submitButton);

        add(formPanel, BorderLayout.NORTH);

        // Table panel
        studentTable = new JTable();
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        submitButton.addActionListener(e -> submitForm());

        setVisible(true);

        // Connect to DB
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            loadCourses();
            loadStudents();
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            System.exit(0);
        }
    }

    private void loadCourses() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT course_id, course_name FROM courses")) {
            courseCombo.removeAllItems();
            while (rs.next()) {
                int id = rs.getInt("course_id");
                String name = rs.getString("course_name");
                courseCombo.addItem(id + " - " + name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading courses: " + e.getMessage());
        }
    }

    private void loadStudents() {
        String sql = "SELECT s.name, s.age, s.email, c.course_name " +
                     "FROM students s " +
                     "JOIN enrollments e ON s.student_id = e.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            studentTable.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }
    }

    private void submitForm() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty() || courseCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "All fields are required!");
            return;
        }

        int age;
        try { age = Integer.parseInt(ageText); }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!");
            return;
        }

        int courseId = Integer.parseInt(courseCombo.getSelectedItem().toString().split(" - ")[0]);

        try {
            // Insert student
            String sqlStudent = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
            PreparedStatement psStudent = conn.prepareStatement(sqlStudent, Statement.RETURN_GENERATED_KEYS);
            psStudent.setString(1, name);
            psStudent.setInt(2, age);
            psStudent.setString(3, email);
            psStudent.executeUpdate();

            // Get generated student_id
            ResultSet keys = psStudent.getGeneratedKeys();
            keys.next();
            int studentId = keys.getInt(1);

            // Insert enrollment
            String sqlEnroll = "INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)";
            PreparedStatement psEnroll = conn.prepareStatement(sqlEnroll);
            psEnroll.setInt(1, studentId);
            psEnroll.setInt(2, courseId);
            psEnroll.executeUpdate();

            JOptionPane.showMessageDialog(this, "Student registered successfully!");

            // Clear fields
            nameField.setText("");
            ageField.setText("");
            emailField.setText("");
            courseCombo.setSelectedIndex(0);

            // Reload students in table
            loadStudents();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // Utility method to build JTable from ResultSet
    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = meta.getColumnCount();
        for (int i = 1; i <= columnCount; i++) columnNames.add(meta.getColumnName(i));

        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnCount; i++) row.add(rs.getObject(i));
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistrationForm::new);
    }
}

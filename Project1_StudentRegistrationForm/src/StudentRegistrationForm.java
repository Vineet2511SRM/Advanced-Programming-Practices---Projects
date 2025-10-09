import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.BorderFactory;
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
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private static final String URL = "jdbc:mysql://localhost:3306/StudentDB?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "vineet12@11";

    private Connection conn;

    public StudentRegistrationForm() {
        setTitle("Student Registration Form");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // Center window
        setLocationRelativeTo(null);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5,2,10,10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Form"));

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

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        formPanel.add(addButton);
        formPanel.add(updateButton);

        add(formPanel, BorderLayout.NORTH);

        // Table panel
        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel();
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        bottomPanel.add(deleteButton);
        bottomPanel.add(refreshButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Connect to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            loadCourses();
            loadStudents();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Error: " + e.getMessage());
            System.exit(0);
        }

        // Button actions
        addButton.addActionListener(e -> addStudent());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        refreshButton.addActionListener(e -> loadStudents());

        // Table row click fills form
        studentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    nameField.setText(tableModel.getValueAt(row, 0).toString());
                    ageField.setText(tableModel.getValueAt(row, 1).toString());
                    emailField.setText(tableModel.getValueAt(row, 2).toString());
                    String courseName = tableModel.getValueAt(row, 3).toString();
                    for (int i = 0; i < courseCombo.getItemCount(); i++) {
                        if (courseCombo.getItemAt(i).contains(courseName)) {
                            courseCombo.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });

        // Make frame visible
        setVisible(true);
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
        String sql = "SELECT s.student_id, s.name, s.age, s.email, c.course_name " +
                     "FROM students s " +
                     "JOIN enrollments e ON s.student_id = e.student_id " +
                     "JOIN courses c ON e.course_id = c.course_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            tableModel.setDataVector(buildTableData(rs), new Vector<>(java.util.List.of("Name","Age","Email","Course","ID")));
            studentTable.getColumnModel().getColumn(4).setMinWidth(0);
            studentTable.getColumnModel().getColumn(4).setMaxWidth(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage());
        }
    }

    private Vector<Vector<Object>> buildTableData(ResultSet rs) throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("name"));
            row.add(rs.getInt("age"));
            row.add(rs.getString("email"));
            row.add(rs.getString("course_name"));
            row.add(rs.getInt("student_id"));
            data.add(row);
        }
        return data;
    }

    private void addStudent() {
        String name = nameField.getText(), email = emailField.getText();
        int age; 
        try { age = Integer.parseInt(ageField.getText()); } catch (Exception e) { JOptionPane.showMessageDialog(this,"Invalid age"); return;}
        if (courseCombo.getSelectedItem()==null || name.isEmpty() || email.isEmpty()) { JOptionPane.showMessageDialog(this,"All fields required"); return; }
        int courseId = Integer.parseInt(courseCombo.getSelectedItem().toString().split(" - ")[0]);
        try {
            String sql = "INSERT INTO students(name,age,email) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,name); ps.setInt(2,age); ps.setString(3,email);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys(); keys.next();
            int studentId = keys.getInt(1);
            String enroll = "INSERT INTO enrollments(student_id,course_id) VALUES(?,?)";
            PreparedStatement psEnroll = conn.prepareStatement(enroll);
            psEnroll.setInt(1, studentId); psEnroll.setInt(2, courseId);
            psEnroll.executeUpdate();
            JOptionPane.showMessageDialog(this,"Student Added Successfully");
            loadStudents();
        } catch(SQLException e){ JOptionPane.showMessageDialog(this,"Error: "+e.getMessage()); }
    }

    private void updateStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select a student first"); return;}
        int studentId = (int) tableModel.getValueAt(row,4);
        String name = nameField.getText(), email = emailField.getText();
        int age;
        try { age = Integer.parseInt(ageField.getText()); } catch (Exception e){ JOptionPane.showMessageDialog(this,"Invalid age"); return;}
        int courseId = Integer.parseInt(courseCombo.getSelectedItem().toString().split(" - ")[0]);
        try {
            String sql = "UPDATE students SET name=?, age=?, email=? WHERE student_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,name); ps.setInt(2,age); ps.setString(3,email); ps.setInt(4,studentId);
            ps.executeUpdate();
            String enroll = "UPDATE enrollments SET course_id=? WHERE student_id=?";
            PreparedStatement psEnroll = conn.prepareStatement(enroll);
            psEnroll.setInt(1,courseId); psEnroll.setInt(2,studentId);
            psEnroll.executeUpdate();
            JOptionPane.showMessageDialog(this,"Student Updated Successfully");
            loadStudents();
        } catch(SQLException e){ JOptionPane.showMessageDialog(this,"Error: "+e.getMessage()); }
    }

    private void deleteStudent() {
        int row = studentTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Select a student first"); return;}
        int studentId = (int) tableModel.getValueAt(row,4);
        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete?","Confirm Delete",JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            String sql = "DELETE FROM students WHERE student_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,studentId); ps.executeUpdate();
            JOptionPane.showMessageDialog(this,"Student Deleted Successfully");
            loadStudents();
        } catch(SQLException e){ JOptionPane.showMessageDialog(this,"Error: "+e.getMessage()); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentRegistrationForm::new);
    }
}

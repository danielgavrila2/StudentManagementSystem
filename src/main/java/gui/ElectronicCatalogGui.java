package gui;

import db_objs.User;
import db_objs.MyJDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ElectronicCatalogGui extends BaseFrame {

    public ElectronicCatalogGui(User user) {
        super("Electronic Catalog", user);
    }

    @Override
    protected void addGuiComponents() {
        this.getContentPane().setBackground(bgColor);

        // Check if the user is TEACHER or ADMIN
        if (!(user.getRole().equals("TEACHER") || user.getRole().equals("ADMIN"))) {
            JOptionPane.showMessageDialog(this, "You don't have permission to access this page.");
            dispose();
            return;
        }

        // Top Section: User Information
        JPanel topPanel = new JPanel();
        topPanel.setBounds(20, 20, getWidth() - 40, 100);
        topPanel.setBackground(bgColor);
        topPanel.setLayout(null);

        String[] name = user.getName();

        JLabel nameLabel = new JLabel("Welcome: " + name[0] + " " + name[1]);
        nameLabel.setBounds(20, 20, 300, 40);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        topPanel.add(nameLabel);

        JLabel roleLabel = new JLabel("Role: " + user.getRole());
        roleLabel.setBounds(20, 60, 300, 30);
        roleLabel.setFont(new Font("Dialog", Font.ITALIC, 20));
        topPanel.add(roleLabel);

        add(topPanel);

        // Title Label
        JLabel titleLabel = new JLabel("Electronic Catalog");
        titleLabel.setBounds(0, 140, getWidth(), 40);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Table Section
        String[] columnNames = {"Student Name", "Course Name", "Grade"};
        Object[][] data = fetchStudentsForCourses(user); // Fetch students and their courses from the database
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        JTable catalogTable = new JTable(tableModel);
        catalogTable.setFont(new Font("Dialog", Font.PLAIN, 18));
        catalogTable.setRowHeight(30);
        catalogTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 20));
        catalogTable.getTableHeader().setBackground(new Color(176, 231, 224));

        // Make the grade column editable
        catalogTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));

        JScrollPane tableScrollPane = new JScrollPane(catalogTable);
        tableScrollPane.setBounds(50, 200, getWidth() - 100, 400);
        add(tableScrollPane);

        // Save Button
        JButton saveButton = new JButton("Save Grades");
        saveButton.setBounds(getWidth() - 250, 620, 200, 40);
        saveButton.setFont(new Font("Dialog", Font.BOLD, 20));
        saveButton.setBackground(new Color(0, 128, 0));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the grades for each student in the table
                for (int row = 0; row < catalogTable.getRowCount(); row++) {
                    String studentName = (String) catalogTable.getValueAt(row, 0);
                    String courseName = (String) catalogTable.getValueAt(row, 1);
                    String grade = (String) catalogTable.getValueAt(row, 2);

                    // Validate grade (optional)
                    if (!grade.matches("[A-Fa-f]")) {
                        JOptionPane.showMessageDialog(ElectronicCatalogGui.this, "Invalid grade for student " + studentName);
                        return;
                    }

                    // Update the grade in the database
//                    if (MyJDBC.updateStudentGrade(studentName, courseName, grade)) {
//                        JOptionPane.showMessageDialog(ElectronicCatalog.this, "Grades saved successfully.");
//                    } else {
//                        JOptionPane.showMessageDialog(ElectronicCatalog.this, "Error saving grades.");
//                    }
                }
            }
        });
        add(saveButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 620, 150, 40);
        backButton.setFont(new Font("Dialog", Font.BOLD, 20));
        backButton.setBackground(new Color(200, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new MainMenuGui(user).setVisible(true);
            dispose();
        });
        add(backButton);
    }

    private Object[][] fetchStudentsForCourses(User user) {
        // Simulate database fetch
        // Replace with the actual database logic to fetch students enrolled in the courses taught by the teacher
        //List<Object[]> studentsData = MyJDBC.getStudentsAndGradesForTeacher(user.getId());

        List<Object[]> studentsData = new ArrayList<Object[]>();

        // Convert the list to a 2D array for JTable
        return studentsData.toArray(new Object[0][0]);
    }
}

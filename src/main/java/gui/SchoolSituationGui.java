package gui;

import db_objs.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import static db_objs.MyJDBC.getGradesForStudent;

public class SchoolSituationGui extends BaseFrame {

    public SchoolSituationGui(User user) {
        super("School Situation", user);
    }

    @Override
    protected void addGuiComponents() {
        this.getContentPane().setBackground(bgColor);

        // Top Section: User Information
        JPanel topPanel = new JPanel();
        topPanel.setBounds(20, 20, getWidth() - 40, 100);
        topPanel.setBackground(bgColor);
        topPanel.setLayout(null);

        String[] name = user.getName();

        JLabel nameLabel = new JLabel("Student: " + name[0] + " " + name[1]);
        nameLabel.setBounds(20, 20, 300, 40);
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        topPanel.add(nameLabel);

        JLabel roleLabel = new JLabel("Role: " + user.getRole());
        roleLabel.setBounds(20, 60, 300, 30);
        roleLabel.setFont(new Font("Dialog", Font.ITALIC, 20));
        topPanel.add(roleLabel);

        add(topPanel);

        // Table Section
        JLabel titleLabel = new JLabel("School Situation");
        titleLabel.setBounds(0, 140, getWidth(), 40);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Table Columns and Data
        String[] columnNames = {"Course Name", "Grade", "Date Assigned"};
        Object[][] data = fetchGradesForUser(user); // Fetch grades from the database
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        JTable gradesTable = new JTable(tableModel);
        gradesTable.setFont(new Font("Dialog", Font.PLAIN, 18));
        gradesTable.setRowHeight(30);
        gradesTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 20));
        gradesTable.getTableHeader().setBackground(new Color(176, 231, 224));
        gradesTable.setRowSelectionAllowed(false);
        gradesTable.setColumnSelectionAllowed(false);
        gradesTable.setCellSelectionEnabled(false);
        gradesTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(gradesTable);
        tableScrollPane.setBounds(50, 200, getWidth() - 100, 400);
        add(tableScrollPane);

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

    private Object[][] fetchGradesForUser(User user) {
        // Use the method to fetch grades for the user
        List<String[]> gradesList = getGradesForStudent(user.getId());

        if (gradesList == null || gradesList.isEmpty()) {
            return new Object[0][3]; // Return empty array if no grades found
        }

        // Transform the list into a 2D Object array
        Object[][] gradesArray = new Object[gradesList.size()][3];

        for (int i = 0; i < gradesList.size(); i++) {
            String[] gradeInfo = gradesList.get(i);
            gradesArray[i][0] = gradeInfo[0]; // Course Name
            gradesArray[i][1] = gradeInfo[1]; // Grade
            gradesArray[i][2] = gradeInfo[2];
        }

        return gradesArray; // Return the constructed array
    }
}

package gui;

import db_objs.MyJDBC;
import db_objs.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportExcelGui extends BaseFrame {

    public ExportExcelGui(User user) {
        super("Export to Excel", user);
    }

    @Override
    protected void addGuiComponents() {
        this.getContentPane().setBackground(bgColor);

        // Title Label
        JLabel titleLabel = new JLabel("Export to Excel");
        titleLabel.setBounds(0, 20, getWidth(), 40);
        titleLabel.setFont(new Font("Dialog", Font.PLAIN, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Checkboxes for export options
        JCheckBox coursesCheckbox = new JCheckBox("Courses");
        coursesCheckbox.setFont(new Font("Dialog", Font.PLAIN, 18));
        coursesCheckbox.setBounds(50, 100, 200, 40);
        add(coursesCheckbox);

        JCheckBox usersCheckbox = new JCheckBox("Users");
        usersCheckbox.setFont(new Font("Dialog", Font.PLAIN, 18));
        usersCheckbox.setBounds(50, 150, 200, 40);
        add(usersCheckbox);

        JCheckBox gradesCheckbox = new JCheckBox("Grades");
        gradesCheckbox.setFont(new Font("Dialog", Font.PLAIN, 18));
        gradesCheckbox.setBounds(50, 200, 200, 40);
        add(gradesCheckbox);


        // Export Button
        JButton exportButton = new JButton("Export");
        exportButton.setBounds(50, 300, 200, 40);
        exportButton.setFont(new Font("Dialog", Font.BOLD, 20));
        exportButton.setBackground(new Color(0, 153, 0));
        exportButton.setForeground(Color.WHITE);
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Create a new workbook and sheet
                    Workbook workbook = new XSSFWorkbook();
                    Sheet sheet = workbook.createSheet("Exported Data");

                    // Start row for data
                    int rowCount = 0;

                    // Export courses data if selected
                    if (coursesCheckbox.isSelected()) {
                        rowCount = exportCourses(workbook, sheet, rowCount);
                    }

                    // Export users data if selected
                    if (usersCheckbox.isSelected()) {
                        rowCount = exportUsers(workbook, sheet, rowCount);
                    }

                    // Export grades data if selected
                    if (gradesCheckbox.isSelected()) {
                        rowCount = exportGrades(workbook, sheet, rowCount);
                    }


                    // Create the file and save it
                    FileOutputStream fileOut = new FileOutputStream(new File("ExportedData.xlsx"));
                    workbook.write(fileOut);
                    fileOut.close();

                    // Show a success message after export
                    JOptionPane.showMessageDialog(ExportExcelGui.this, "Data exported successfully!");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(ExportExcelGui.this, "Error during export: " + ioException.getMessage());
                }
            }
        });
        add(exportButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 400, 150, 40);
        backButton.setFont(new Font("Dialog", Font.BOLD, 20));
        backButton.setBackground(new Color(200, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            new MainMenuGui(user).setVisible(true);
            dispose();
        });
        add(backButton);
    }

    // Method to export courses data
    private int exportCourses(Workbook workbook, Sheet sheet, int rowCount) {
        Row headerRow = sheet.createRow(rowCount++);
        headerRow.createCell(0).setCellValue("Course Name");
        headerRow.createCell(1).setCellValue("Date");
        headerRow.createCell(2).setCellValue("Hour");

        // Fetch courses data from the database
        var courses = MyJDBC.getCoursesForStudent(user.getId());

        for (var course : courses) {
            Row row = sheet.createRow(rowCount++);
            String[] data = user.getName();
            row.createCell(0).setCellValue(course[0]);
            row.createCell(1).setCellValue(course[3]);
            row.createCell(2).setCellValue(course[4]);
            row.createCell(2).setCellValue(course[5]);
        }
        return rowCount;
    }

    // Method to export users data
    private int exportUsers(Workbook workbook, Sheet sheet, int rowCount) {
        Row headerRow = sheet.createRow(rowCount++);
        headerRow.createCell(0).setCellValue("Username");
        headerRow.createCell(1).setCellValue("Role");

        // Fetch users data from the database
        var users = MyJDBC.getAllUsers();

        for (var user : users) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(user[0]);
            row.createCell(1).setCellValue(user[1]);
        }
        return rowCount;
    }

    // Method to export grades data
    private int exportGrades(Workbook workbook, Sheet sheet, int rowCount) {
        Row headerRow = sheet.createRow(rowCount++);
        headerRow.createCell(0).setCellValue("Student Name");
        headerRow.createCell(1).setCellValue("Course");
        headerRow.createCell(2).setCellValue("Grade");

        // Fetch grades data from the database
        var grades = MyJDBC.getGradesForStudent(user.getId());

        for (var grade : grades) {
            Row row = sheet.createRow(rowCount++);
            row.createCell(0).setCellValue(user.getUsername());
            row.createCell(1).setCellValue(grade[1]);
            row.createCell(2).setCellValue(grade[0]);
        }
        return rowCount;
    }

}

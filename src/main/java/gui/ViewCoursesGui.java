package gui;

import db_objs.User;
import db_objs.MyJDBC;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ViewCoursesGui extends BaseFrame {

    public ViewCoursesGui(User user) {
        super("View Courses", user);
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
        JLabel titleLabel = new JLabel("Courses List");
        titleLabel.setBounds(0, 140, getWidth(), 40);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Table Section
        String[] columnNames = {"Course Name", "Description", "Lecturer", "Date"};
        Object[][] data = fetchCoursesForUser(user); // Fetch courses from the database
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        JTable coursesTable = new JTable(tableModel);
        coursesTable.setFont(new Font("Dialog", Font.PLAIN, 18));
        coursesTable.setRowHeight(30);
        coursesTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 20));
        coursesTable.getTableHeader().setBackground(new Color(176, 231, 224));

        JScrollPane tableScrollPane = new JScrollPane(coursesTable);
        tableScrollPane.setBounds(50, 200, getWidth() - 100, 400);
        add(tableScrollPane);

        // Add Course Button (only for ADMIN or TEACHER)
        if (user.getRole().equals("ADMIN") || user.getRole().equals("TEACHER")) {
            JButton addCourseButton = new JButton("Add New Course");
            addCourseButton.setBounds(getWidth() - 250, 620, 200, 40);
            addCourseButton.setFont(new Font("Dialog", Font.BOLD, 20));
            addCourseButton.setBackground(new Color(0, 128, 0));
            addCourseButton.setForeground(Color.WHITE);
            addCourseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Open the Add Course dialog
                    //new AddCourseDialog(ViewCourses.this, user).setVisible(true);
                }
            });
            add(addCourseButton);
        }

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 620, 150, 40);
        backButton.setFont(new Font("Dialog", Font.BOLD, 20));
        backButton.setBackground(new Color(200, 0, 0));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            // Navigate back to the previous page
            dispose();
        });
        add(backButton);
    }

    private Object[][] fetchCoursesForUser(User user) {
        // Simulating database fetch
        // Replace this with real database logic to fetch courses, lecturers, and dates
        List<String[]> courses = MyJDBC.getCourses();


        // Convert the list to a 2D array for JTable
        return courses.toArray(new Object[0][0]);
    }

    // AddCourseDialog - Dialog to add a new course
    public static class AddCourseDialog extends JDialog {
        private User user;

        public AddCourseDialog(JFrame parent, User user) {
            super(parent, "Add New Course", true);
            this.user = user;
            setSize(400, 300);
            setLocationRelativeTo(parent);
            setLayout(null);
            setResizable(false);

            JLabel courseNameLabel = new JLabel("Course Name:");
            courseNameLabel.setBounds(30, 30, 150, 30);
            courseNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
            add(courseNameLabel);

            JTextField courseNameField = new JTextField();
            courseNameField.setBounds(180, 30, 180, 30);
            courseNameField.setFont(new Font("Dialog", Font.PLAIN, 20));
            add(courseNameField);

            JLabel dateLabel = new JLabel("Date:");
            dateLabel.setBounds(30, 80, 150, 30);
            dateLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
            add(dateLabel);

            // Date Picker
            JDatePickerImpl datePicker = createDatePicker();
            datePicker.setBounds(180, 80, 180, 30);
            add(datePicker);

            JLabel hourLabel = new JLabel("Hour (HH:MM):");
            hourLabel.setBounds(30, 130, 150, 30);
            hourLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
            add(hourLabel);

            JTextField hourField = new JTextField();
            hourField.setBounds(180, 130, 180, 30);
            hourField.setFont(new Font("Dialog", Font.PLAIN, 20));
            add(hourField);

            // Submit Button
            JButton submitButton = new JButton("Submit");
            submitButton.setBounds(120, 180, 150, 40);
            submitButton.setFont(new Font("Dialog", Font.BOLD, 20));
            submitButton.setBackground(new Color(0, 128, 0));
            submitButton.setForeground(Color.WHITE);
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String courseName = courseNameField.getText();
                    String hour = hourField.getText();
                    java.util.Date date = (java.util.Date) datePicker.getModel().getValue();

                    if (courseName.isEmpty() || hour.isEmpty() || date == null || !hour.matches("\\d{2}:\\d{2}")) {
                        JOptionPane.showMessageDialog(AddCourseDialog.this, "Please fill all fields correctly.");
                        return;
                    }

                    // Convert the hour to time format
                    String[] hourParts = hour.split(":");
                    String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    String courseTime = formattedDate + " " + hourParts[0] + ":" + hourParts[1];

                    // Insert the new course into the database
//                    if (MyJDBC.addCourse(courseName, user.getId(), courseTime)) {
//                        JOptionPane.showMessageDialog(AddCourseDialog.this, "Course added successfully.");
//                        dispose();
//                    } else {
//                        JOptionPane.showMessageDialog(AddCourseDialog.this, "Error adding course.");
//                    }
                }
            });
            add(submitButton);

            // Cancel Button
            JButton cancelButton = new JButton("Cancel");
            cancelButton.setBounds(120, 230, 150, 40);
            cancelButton.setFont(new Font("Dialog", Font.BOLD, 20));
            cancelButton.setBackground(new Color(255, 0, 0));
            cancelButton.setForeground(Color.WHITE);
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);
        }

        private JDatePickerImpl createDatePicker() {
            UtilDateModel model = new UtilDateModel();
            Properties properties = new Properties();
            properties.put("text.today", "Today");
            properties.put("text.month", "Month");
            properties.put("text.year", "Year");
            JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
            return new JDatePickerImpl(datePanel, new DateLabelFormatter());
        }
    }

    // DateLabelFormatter for JDatePicker
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormat.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof java.util.GregorianCalendar) {
                    java.util.GregorianCalendar calendar = (java.util.GregorianCalendar) value;
                    return dateFormat.format(calendar.getTime());
                }
            }
            return "";
        }
    }
}

package gui;

import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuGui extends BaseFrame implements ActionListener {

   // private JTextField currentBalanceField;
    private static final int buttonWidth = 1280 / 3 - 40;
    private static final int buttonHeight = 400/2 - 20;

    public MainMenuGui(User user){
        super("UTCN Students Management System", user);
    }

    @Override
    protected void addGuiComponents() {

        ImageIcon utcn = new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\Logo_Universitatea_Tehnică_din_Cluj-Napoca.svg.png");
        JLabel logoLabel = new JLabel(utcn);
        logoLabel.setBounds(getWidth()-400, 10, 400, 130);
        add(logoLabel);

        String welcomeMessage = "";

        if (user.getRole().equals("ADMIN")) {
            welcomeMessage = "<html>" + "<body style = 'text-align:left'>" +
                    "<b>Hello, " + user.getUsername() + "</b><br>" +
                    "You are logged in as " + user.getRole() + "</body></html>";
        }
        else {
            String[] names = user.getName();

            welcomeMessage = "<html>" + "<body style = 'text-align:left'>" +
                    "<b>Hello, " + names[0] + " " + names[1] + "</b><br>" +
                    "You are logged in as " + user.getRole() + "</body></html>";
        }


        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(80, 15, getWidth() / 2, 60);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(welcomeMessageLabel);

        // Add user role-specific image
        ImageIcon studentImage = new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\student.jpg");
        ImageIcon adminImage = new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\admin.jpg");
        ImageIcon teacherImage = new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\teacher.jpg");

        JLabel imageLabel = new JLabel();
        if (user.getRole().equals("ADMIN")) {
            imageLabel.setIcon(adminImage);
        } else if (user.getRole().equals("TEACHER")) {
            imageLabel.setIcon(teacherImage);
        } else {
            imageLabel.setIcon(studentImage);
        }

        imageLabel.setBounds(5, 10, 70, 70);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(imageLabel);

        this.getContentPane().setBackground(bgColor);

        // Define buttons
        JButton manageProfileButton = createButton("Manage Profile", 40, 180);
        ImageIcon profile = new ImageIcon(
                new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\profile.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        manageProfileButton.setIcon(profile);


        JButton schoolSituationButton = createButton("School Situation", buttonWidth + 50, 180);
        ImageIcon school = new ImageIcon(
                new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\5231793.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        schoolSituationButton.setIcon(school);

        JButton viewCoursesButton = createButton("View Courses", 2 * buttonWidth + 60, 180);
        ImageIcon courses = new ImageIcon(
                new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\73531.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        viewCoursesButton.setIcon(courses);


        JButton catalogButton = createButton("Electronic Catalog", 40, 2 * buttonHeight + 10);
        ImageIcon catalog = new ImageIcon(
                new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\high-school-icon-png-8.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        catalogButton.setIcon(catalog);


        JButton manageUsersButton = createButton("Manage Users", buttonWidth + 50, 2 * buttonHeight + 10);
        ImageIcon manage = new ImageIcon(
                new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\3677898.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        manageUsersButton.setIcon(manage);

        JButton extractExcelButton = createButton("Extract Datas", 2 * buttonWidth + 60, 2 * buttonHeight + 10);
        ImageIcon excel = new ImageIcon(
                new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\ms-excel.png").getImage().getScaledInstance(80,80, Image.SCALE_DEFAULT));
        extractExcelButton.setIcon(excel);


        JButton logoutButton = createButton("Exit", getWidth() / 3, 600, getWidth() / 3, 50, Color.RED);
        ImageIcon exit = new ImageIcon(
                new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\free-exit-icon-2860-thumb.png").getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT));
        logoutButton.setIcon(exit);

        add(manageProfileButton);
        add(schoolSituationButton);
        add(viewCoursesButton);
        add(catalogButton);
        add(extractExcelButton);
        add(manageUsersButton);

        // Add logout button
        add(logoutButton);
    }

    // Helper method to create buttons
    private JButton createButton(String text, int x, int y) {
        return createButton(text, x, y, buttonWidth, buttonHeight, new Color(0, 169, 0));
    }

    private JButton createButton(String text, int x, int y, int width, int height, Color bgColor) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Dialog", Font.BOLD, 22));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.addActionListener(this);
        button.setBackground(bgColor);
        return button;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // Handle logout button
        if (buttonPressed.equalsIgnoreCase("Exit")) {
            new LoginGui().setVisible(true);
            this.dispose();
            return;
        }

        // Handle Manage Profile button
        if (buttonPressed.equalsIgnoreCase("Manage Profile")) {
            if (user.getRole().equals("STUDENT") || user.getRole().equals("TEACHER")) {
                new ManageProfileGui(user).setVisible(true);
                this.dispose();
                return;
            }
            else {
                JOptionPane.showMessageDialog(this, "You are an Admin, use MANAGE USERS instead.");
            }

        }

        // Handle School Situation button
        if (buttonPressed.equalsIgnoreCase("School Situation")) {
            if (user.getRole().equals("STUDENT")) {
                new SchoolSituationGui(user).setVisible(true);
                this.dispose();
                return;
            }
            else {
                JOptionPane.showMessageDialog(this, "You don't have permission to access this page.");
            }

        }

        // Handle View Courses button
        if (buttonPressed.equalsIgnoreCase("View Courses")) {
            if (user.getRole().equals("STUDENT") || user.getRole().equals("TEACHER")) {
                new ViewCoursesGui(user).setVisible(true);
                this.dispose();
                return;
            }
            else {
                JOptionPane.showMessageDialog(this, "You are not STUDENT or TEACHER.");
            }

        }

        // Handle Electronic Catalog button
        if (buttonPressed.equalsIgnoreCase("Electronic Catalog")) {
            if (user.getRole().equals("ADMIN") || user.getRole().equals("TEACHER")) {
                new ElectronicCatalogGui(user).setVisible(true);
                this.dispose();
                return;
            }
            else {
                JOptionPane.showMessageDialog(this, "You don't have permission to access this page.");
            }

        }

        // Handle Extract Datas button
        if (buttonPressed.equalsIgnoreCase("Extract Datas")) {
            if (user.getRole().equals("ADMIN") || user.getRole().equals("TEACHER")) {
                new ExportExcelGui(user).setVisible(true);
                this.dispose();
                return;
            }
            else {
                JOptionPane.showMessageDialog(this, "You don't have permission to access this page.");
            }
        }

        // Handle Manage Users button
        if (buttonPressed.equalsIgnoreCase("Manage Users")) {

            if (user.getRole().equals("ADMIN")) {
                new ManageUsersGui(user).setVisible(true);
                this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "You don't have permission to access this page.");
            }


        }
    }
}

package gui;

import db_objs.MyJDBC;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends BaseFrame{

    protected String role;

    public RegisterGui(){
        super("Register");
    }

    @Override
    protected void addGuiComponents() {

        JLabel bankingAppLabel = new JLabel("Register a new account");
        bankingAppLabel.setBounds(0,20,super.getWidth(),60);
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 40));
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bankingAppLabel);

        //username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(getWidth()/4,120,getWidth()/2, 24);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(usernameLabel);

        //username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(getWidth()/4,160,getWidth()/2, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN , 28));
        usernameField.setBackground(fieldBgColor);
        add(usernameField);

        //password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(getWidth()/4,220,getWidth()/2, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        //password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(getWidth()/4,260,getWidth()/2, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN , 28));
        passwordField.setBackground(fieldBgColor);
        add(passwordField);

        //retype password label
        JLabel rePasswordLabel = new JLabel("Re-type Password");
        rePasswordLabel.setBounds(getWidth()/4,320,getWidth()/2, 40);
        rePasswordLabel.setFont(new Font("Dialog", Font.PLAIN , 20));
        add(rePasswordLabel);

        //re-type password field
        JPasswordField rePasswordField = new JPasswordField();
        rePasswordField.setBounds(getWidth()/4,360,getWidth()/2, 40);
        rePasswordField.setFont(new Font("Dialog", Font.PLAIN , 28));
        rePasswordField.setBackground(fieldBgColor);
        add(rePasswordField);


        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setBounds(getWidth() / 4, 420, getWidth() / 2, 40);
        roleLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(roleLabel);


        // Create a ButtonGroup for the role selector
        ButtonGroup roleGroup = new ButtonGroup();

        // Admin role radio button
        JRadioButton adminRadio = new JRadioButton("Admin");
        adminRadio.setBounds(getWidth() / 4, 460, 120, 40);
        adminRadio.setFont(new Font("Dialog", Font.PLAIN, 18));
        adminRadio.setSelected(true); // Default selection
        adminRadio.setBackground(fieldBgColor);
        roleGroup.add(adminRadio);
        add(adminRadio);

        // Teacher role radio button
        JRadioButton teacherRadio = new JRadioButton("Teacher");
        teacherRadio.setBounds(getWidth() / 4 + 260, 460, 120, 40);
        teacherRadio.setFont(new Font("Dialog", Font.PLAIN, 18));
        teacherRadio.setBackground(fieldBgColor);
        roleGroup.add(teacherRadio);
        add(teacherRadio);

        // Student role radio button
        JRadioButton studentRadio = new JRadioButton("Student");
        studentRadio.setBounds(getWidth() / 4 + 500, 460, 120, 40);
        studentRadio.setFont(new Font("Dialog", Font.PLAIN, 18));
        studentRadio.setBackground(fieldBgColor);
        roleGroup.add(studentRadio);
        add(studentRadio);


        adminRadio.addActionListener(e -> role = "ADMIN");
        teacherRadio.addActionListener(e -> role = "TEACHER");
        studentRadio.addActionListener(e -> role = "STUDENT");

        //register button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(getWidth()/3,540,getWidth()/3,40);
        registerButton.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerButton.setBackground(new Color(0, 255, 0));

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String rePassword = String.valueOf(rePasswordField.getPassword());

                //validate the pass
                if(validateUserInput(username, password, rePassword)){
                    if(MyJDBC.register(username, password, role)){

                        RegisterGui.this.dispose();

                        User newUser = MyJDBC.validateLogic(username, password);
                        FormularRegister formularRegister = new FormularRegister(newUser);
                        formularRegister.setVisible(true);

                        JOptionPane.showMessageDialog(formularRegister,"Your account was registered successfully.");
                    }else{
                        JOptionPane.showMessageDialog(RegisterGui.this,"Error! The username is already taken!");

                    }
                }
                else{
                    JOptionPane.showMessageDialog(RegisterGui.this,"Error! Username must be at least 6 characters!\nThe Password must match both fields!");
                }
            }
        });

        add(registerButton);



        //login label
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Already have an account? Sign-in here</a></html>");
        loginLabel.setBounds(0,600,getWidth()-10,30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterGui.this.dispose();
                new LoginGui().setVisible(true);
            }
        });

        add(loginLabel);
        this.getContentPane().setBackground(bgColor);
    }

    private boolean validateUserInput(String username, String password, String rePassword){

        if(username.length() == 0 || password.length() == 0 || rePassword.length() == 0){
            return false;
        }

        if(username.length() < 6 || username.length() > 20){
            return false;
        }

        if(!password.equals(rePassword)){
            return false;
        }

        return true;
    }

}

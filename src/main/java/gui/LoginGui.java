package gui;

import db_objs.MyJDBC;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGui extends BaseFrame {

    public LoginGui(){
        super("Student Management System");
    }

    @Override
    protected void addGuiComponents(){

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setBounds(0,20,super.getWidth(),60);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBackground(new Color(255, 165, 142));
        add(titleLabel);

        //username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(getWidth()/4,120,getWidth()/2, 24);
        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        usernameLabel.setBackground(new Color(255, 165, 142));
        add(usernameLabel);

        //username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(getWidth()/4,160,getWidth()/2, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN , 28));
        usernameField.setBackground(fieldBgColor);
        add(usernameField);

        //password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(getWidth()/4,280,getWidth()/2, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        passwordLabel.setBackground(new Color(255, 165, 142));
        add(passwordLabel);

        //password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(getWidth()/4,320,getWidth()/2, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN , 28));
        passwordField.setBackground(fieldBgColor);
        add(passwordField);

        //login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(getWidth()/3,460,getWidth()/3,40);
        loginButton.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginButton.setBackground(new Color(0, 255, 0));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get username
                String username = usernameField.getText();

                //get password
                String password = String.valueOf(passwordField.getPassword());

                //validare
                User user = MyJDBC.validateLogic(username, password);

                //daca user == null atunci e invalid, altfel este un cont valid
                if (user != null){
                    //logare valida

                    //schimbam interfata, adica se intra in aplicatie
                    LoginGui.this.dispose();

                    //pornim interfata din meniul de banca
                    MainMenuGui mainMenuGui = new MainMenuGui(user);
                    mainMenuGui.setVisible(true);

                    //afisam mesaj de succes
                    JOptionPane.showMessageDialog(mainMenuGui,"Login successfully!");
                }else{
                    JOptionPane.showMessageDialog(LoginGui.this, "Invalid username or password!");
                }
            }
        });

        add(loginButton);

        //register label
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register Here</a></html>");
        registerLabel.setBounds(0,510,getWidth()-10,30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setForeground(Color.BLACK);

        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginGui.this.dispose();
                new RegisterGui().setVisible(true);
            }
        });

        this.getContentPane().setBackground(bgColor);
        add(registerLabel);

    }
}

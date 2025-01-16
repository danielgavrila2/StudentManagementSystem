package gui;

import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public abstract class BaseFrame extends JFrame {

    public final static Color bgColor = new Color(255, 255, 255);
    public final static Color fieldBgColor = new Color(176, 231, 224);

    protected User user;

    public BaseFrame(String title) {
        initialize(title);
        setBackground(bgColor);
    }

    public BaseFrame(String title, User user){
        this.user = user;
        initialize(title);
        setBackground(bgColor);
    }

    private void initialize(String title){
        setTitle(title);
        setSize(1280,720);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        ImageIcon icon = new ImageIcon("D:\\Backup\\IdeaProjects\\Login_BoilerPlate\\src\\main\\java\\images\\logo.png");
        setIconImage(icon.getImage());

        setLocationRelativeTo(null);
        setBackground(bgColor);
        addGuiComponents();

    }

    protected abstract void addGuiComponents();

}

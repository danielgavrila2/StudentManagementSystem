package gui;

import db_objs.User;

import javax.swing.*;
import java.awt.*;

public class BankingAppDialog extends JDialog {
    private User user;
    private MainMenuGui mainMenuGui;
    private JLabel balanceLabel, enterAmountLabel;
    private JTextField enterAmountField;
    private JButton actionButton;

    public BankingAppDialog(MainMenuGui mainMenuGui, User user){
        setSize(400,400);
        setModal(true);
        setLocationRelativeTo(mainMenuGui);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        this.mainMenuGui = mainMenuGui;
        this.user = user;

    }

    public void addCurrentBalanceAndAmount(){
        //balance label
       // balanceLabel = new JLabel("Suma curenta: " + user.getCurrentBalance() + " lei");
        balanceLabel.setBounds(0,10,getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        //introducere suma label
        enterAmountLabel = new JLabel("Introduceti suma: ");
        enterAmountLabel.setBounds(0,50,getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        //introducere suma label
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15,80,getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.RIGHT);
        add(enterAmountField);
    }

    public void addActionButton(String actionButtonType){
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15,300,getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        add(actionButton);
    }
}

package gui;

import db_objs.User;
import db_objs.MyJDBC;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersGui extends BaseFrame {

    public static User adminUser = null;

    public ManageUsersGui(User user) {
        super("Manage Users", user);
    }

    @Override
    protected void addGuiComponents() {
        this.getContentPane().setBackground(bgColor);

        adminUser = new User(user.getId(), user.getUsername(), user.getPassword(), user.getRole());

        // Check if the user is ADMIN
        if (!user.getRole().equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "You don't have permission to access this page.");
            dispose();
            return;
        }

        // Title Label
        JLabel titleLabel = new JLabel("Manage Users");
        titleLabel.setBounds(0, 20, getWidth(), 40);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        // Table Section: Display Users
        String[] columnNames = {"Username", "Role"};
        Object[][] data = fetchUsersData(); // Fetch all users and their roles from the database
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        JTable usersTable = new JTable(tableModel);
        usersTable.setFont(new Font("Dialog", Font.PLAIN, 18));
        usersTable.setRowHeight(30);
        usersTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 20));
        usersTable.getTableHeader().setBackground(new Color(176, 231, 224));

        JScrollPane tableScrollPane = new JScrollPane(usersTable);
        tableScrollPane.setBounds(50, 80, getWidth() - 100, 300);
        add(tableScrollPane);

        // Pending Requests Section
        JLabel pendingLabel = new JLabel("Pending Registration Requests");
        pendingLabel.setBounds(50, 400, getWidth(), 30);
        pendingLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        add(pendingLabel);

        // Table Section: Pending Requests
        String[] pendingColumnNames = {"Username", "Role"};
        Object[][] pendingData = fetchPendingRequests(); // Fetch pending registration requests from the database
        DefaultTableModel pendingTableModel = new DefaultTableModel(pendingData, pendingColumnNames);

        JTable pendingRequestsTable = new JTable(pendingTableModel);
        pendingRequestsTable.setFont(new Font("Dialog", Font.PLAIN, 18));
        pendingRequestsTable.setRowHeight(30);
        pendingRequestsTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 20));
        pendingRequestsTable.getTableHeader().setBackground(new Color(176, 231, 224));

        JScrollPane pendingTableScrollPane = new JScrollPane(pendingRequestsTable);
        pendingTableScrollPane.setBounds(50, 440, getWidth() - 100, 150);
        add(pendingTableScrollPane);

        // Add a listener for the table rows
        usersTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) usersTable.getValueAt(selectedRow, 0);
                String role = (String) usersTable.getValueAt(selectedRow, 1);

                // Open the Manage Profile page for the selected user
                User selectedUser = MyJDBC.getUserByUsername(username);


                if (selectedUser != null) {
                    new ManageProfileGui(selectedUser).setVisible(true);
                    dispose();
                }
            }
        });

        // Add listener for pending requests
        pendingRequestsTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = pendingRequestsTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) pendingRequestsTable.getValueAt(selectedRow, 0);
                String role = (String) pendingRequestsTable.getValueAt(selectedRow, 1);

                // Show a dialog to approve or reject the pending request
                int response = JOptionPane.showOptionDialog(this,
                        "Approve or Reject Registration for " + username + " (Role: " + role + ")?",
                        "Pending Registration Request",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Approve", "Reject"},
                        "Approve");

//                if (response == JOptionPane.YES_OPTION) {
//                    MyJDBC.approveRegistrationRequest(username);
//                    JOptionPane.showMessageDialog(this, "Registration Approved.");
//                    // Refresh the pending requests table
//                    refreshPendingRequestsTable();
//                } else if (response == JOptionPane.NO_OPTION) {
//                    MyJDBC.rejectRegistrationRequest(username);
//                    JOptionPane.showMessageDialog(this, "Registration Rejected.");
//                    // Refresh the pending requests table
//                    refreshPendingRequestsTable();
//                }
            }
        });

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

    private Object[][] fetchUsersData() {
        List<String[]> users = MyJDBC.getAllUsers();

        return users.toArray(new Object[0][0]);
    }

    private Object[][] fetchPendingRequests() {
        // Simulate fetching pending requests data from the database
        // Replace with actual database logic to fetch pending registration requests
     //   List<Object[]> pendingRequests = MyJDBC.getPendingRequests();

        List<Object[]> pendingRequests = new ArrayList<>();

        // Convert the list to a 2D array for JTable
        return pendingRequests.toArray(new Object[0][0]);
    }

    private void refreshPendingRequestsTable() {
        // Refresh the pending requests table after an action (approve/reject)
        Object[][] pendingData = fetchPendingRequests();
        DefaultTableModel model = (DefaultTableModel) ((JTable) ((JScrollPane) getComponentAt(50, 440)).getViewport().getView()).getModel();
        model.setDataVector(pendingData, new String[]{"Username", "Role"});
    }
}

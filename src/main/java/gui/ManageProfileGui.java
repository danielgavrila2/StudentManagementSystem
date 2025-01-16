package gui;

import db_objs.MyJDBC;
import db_objs.User;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import static gui.ManageUsersGui.adminUser;

public class ManageProfileGui extends BaseFrame {

    public ManageProfileGui(User user){
        super("Manage Profile", user);
    }

    @Override
    protected void addGuiComponents() {

        String[] data = user.getName();

        this.getContentPane().setBackground(bgColor);

        JLabel titleLabel = new JLabel("Personal Information");
        titleLabel.setBounds(0, 20, super.getWidth(), 60);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 40));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel);

        //first name label
        JLabel firstNameLabel = new JLabel("First Name");
        firstNameLabel.setBounds(100, 100, getWidth() / 3, 24);
        firstNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(firstNameLabel);

        JTextField firstNameField = new JTextField(data[0]);
        firstNameField.setBounds(100, 140, getWidth() / 3, 40);
        firstNameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        firstNameField.setBackground(fieldBgColor);
        add(firstNameField);


        //last name
        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setBounds(getWidth() - getWidth() / 3 - 100, 100, getWidth() / 3, 24);
        lastNameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(lastNameLabel);

        JTextField lastNameField = new JTextField(data[1]);
        lastNameField.setBounds(getWidth() - 100 - getWidth() / 3, 140, getWidth() / 3, 40);
        lastNameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        lastNameField.setBackground(fieldBgColor);
        add(lastNameField);


        //email label
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(100, 200, getWidth() / 3, 24);
        emailLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(emailLabel);

        JTextField emailField = new JTextField(data[2]);
        emailField.setBounds(100, 240, getWidth() / 3, 40);
        emailField.setFont(new Font("Dialog", Font.PLAIN, 28));
        emailField.setBackground(fieldBgColor);
        add(emailField);


        //CNP label
        JLabel cnpLabel = new JLabel("Personal Numeric Code (CNP)");
        cnpLabel.setBounds(getWidth() - getWidth() / 3 - 100, 200, getWidth() / 3, 24);
        cnpLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(cnpLabel);

        JTextField cnpField = new JTextField(data[3]);
        cnpField.setBounds(getWidth() - 100 - getWidth() / 3, 240, getWidth() / 3, 40);
        cnpField.setFont(new Font("Dialog", Font.PLAIN, 28));
        cnpField.setBackground(fieldBgColor);
        add(cnpField);

        ((AbstractDocument) cnpField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;

                // Allow only numeric input and limit length to 13
                if (newText.matches("\\d*") && newText.length() <= 13) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;

                // Allow only numeric input and limit length to 13
                if (newText.matches("\\d*") && newText.length() <= 13) {
                    super.insertString(fb, offset, text, attrs);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length); // Allow removal of characters
            }
        });

        JLabel calendarLabel = new JLabel("Date of birth");
        calendarLabel.setBounds(100, 300, getWidth() / 3, 28);
        calendarLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(calendarLabel);


        UtilDateModel model = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        datePicker.setBounds(100, 340, 200, 50);
        add(datePicker);


        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date predefinedDate = dateFormat.parse(data[4]);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(predefinedDate);
            model.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            model.setSelected(true);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date selectedDate = (Date) datePicker.getModel().getValue();
        if (selectedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(selectedDate);
            System.out.println("Selected Date: " + formattedDate);
        } else {
            System.out.println("No date selected.");
        }

        String[] address = user.getAddress(); // address[0]: country, address[1]: county, address[2]: city, address[3]: street, address[4]: number

// Country ComboBox
        ArrayList<String> countryList = MyJDBC.getCountry();
        JComboBox<String> countryComboBox = new JComboBox<>();
        for (String country : countryList) {
            countryComboBox.addItem(country);
        }
        countryComboBox.setBounds(360, 340, 200, 50);
        countryComboBox.setMaximumRowCount(10);
        add(countryComboBox);

// County ComboBox
        JComboBox<String> countyComboBox = new JComboBox<>();
        countyComboBox.setBounds(620, 340, 200, 50);
        countyComboBox.setMaximumRowCount(10);
        countyComboBox.setEnabled(false); // Initially disabled
        add(countyComboBox);

// City ComboBox
        JComboBox<String> cityComboBox = new JComboBox<>();
        cityComboBox.setBounds(880, 340, 200, 50);
        cityComboBox.setMaximumRowCount(10);
        cityComboBox.setEnabled(false); // Initially disabled
        add(cityComboBox);

// Predefined values logic
        if (address[0] != null) {
            countryComboBox.setSelectedItem(address[0]); // Set predefined country
            ArrayList<String> countyList = MyJDBC.getCounty(address[0]); // Fetch counties for predefined country
            for (String county : countyList) {
                countyComboBox.addItem(county);
            }
            countyComboBox.setEnabled(true);

            if (address[1] != null) {
                countyComboBox.setSelectedItem(address[1]); // Set predefined county
                ArrayList<String> cityList = MyJDBC.getCity(address[1]); // Fetch cities for predefined county
                for (String city : cityList) {
                    cityComboBox.addItem(city);
                }
                cityComboBox.setEnabled(true);

                if (address[2] != null) {
                    cityComboBox.setSelectedItem(address[2]); // Set predefined city
                }
            }
        }

// Add Listeners for Dependency
        countryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCountry = (String) countryComboBox.getSelectedItem();
                ArrayList<String> updatedCountyList = MyJDBC.getCounty(selectedCountry);
                countyComboBox.removeAllItems(); // Clear existing items
                for (String county : updatedCountyList) {
                    countyComboBox.addItem(county);
                }
                countyComboBox.setEnabled(true);
                cityComboBox.setEnabled(false); // Disable City dropdown until a County is selected
                cityComboBox.removeAllItems(); // Clear any previous cities
            }
        });

        countyComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCounty = (String) countyComboBox.getSelectedItem();
                if (selectedCounty != null) {
                    ArrayList<String> updatedCityList = MyJDBC.getCity(selectedCounty);
                    cityComboBox.removeAllItems(); // Clear existing items
                    for (String city : updatedCityList) {
                        cityComboBox.addItem(city);
                    }
                    cityComboBox.setEnabled(true); // Enable City dropdown
                }
            }
        });

// Street and Number Fields
        JLabel streetLabel = new JLabel("Street");
        streetLabel.setBounds(100, 430, getWidth() / 3, 24);
        streetLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(streetLabel);

        JTextField streetField = new JTextField();
        streetField.setBounds(100, 470, getWidth() / 3, 40);
        streetField.setFont(new Font("Dialog", Font.PLAIN, 28));
        streetField.setBackground(fieldBgColor);
        streetField.setText(address[3]); // Set predefined street
        add(streetField);

        JLabel numberLabel = new JLabel("Number");
        numberLabel.setBounds(getWidth() - getWidth() / 3 - 100, 430, getWidth() / 3, 24);
        numberLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(numberLabel);

        JTextField numberField = new JTextField();
        numberField.setBounds(getWidth() - 100 - getWidth() / 3, 470, getWidth() / 3, 40);
        numberField.setFont(new Font("Dialog", Font.PLAIN, 28));
        numberField.setBackground(fieldBgColor);
        numberField.setText(address[4]); // Set predefined number
        add(numberField);


        //register button
        JButton updateButton = new JButton("Update values");
        updateButton.setBounds(getWidth() / 3, 540, getWidth() / 3, 40);
        updateButton.setFont(new Font("Dialog", Font.PLAIN, 20));
        updateButton.setBackground(new Color(0, 255, 0));

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String email = emailField.getText();
                String cnp = cnpField.getText();
                Date date = model.getValue(); // Retrieve the selected date
                java.sql.Date sqlDate = (date != null) ? new java.sql.Date(date.getTime()) : null; // Convert to SQL Date
                String country = Objects.requireNonNull(countryComboBox.getSelectedItem()).toString();
                String county = Objects.requireNonNull(countyComboBox.getSelectedItem()).toString();
                String city = Objects.requireNonNull(cityComboBox.getSelectedItem()).toString();
                String street = streetField.getText();
                String number = numberField.getText();

                // Validate user input
                if (validateUserInput(user.getRole(), firstName, lastName, email, cnp, date, street, number)) {
                    if (MyJDBC.updateForm(user.getId(), user.getUsername(), firstName, lastName, email, cnp, sqlDate, country, county, city, street, number)) {
                        ManageProfileGui.this.dispose();

                        MainMenuGui mainMenuGui = null;

                        if (adminUser != null) {
                            mainMenuGui = new MainMenuGui(adminUser);
                        }
                        else {
                            mainMenuGui = new MainMenuGui(user);
                        }

                        mainMenuGui.setVisible(true);

                        JOptionPane.showMessageDialog(mainMenuGui, "Data was successfully updated!");
                    } else {
                        JOptionPane.showMessageDialog(ManageProfileGui.this, "Error! The username was not found!");
                    }
                } else {
                    JOptionPane.showMessageDialog(ManageProfileGui.this, "Error! Please check the input fields.");
                }
            }
        });


        add(updateButton);

    }

    private boolean validateUserInput(String role, String firstName, String lastName, String email, String cnp, Date date,
                                      String street, String number) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || cnp.isEmpty() || number.isEmpty() || street.isEmpty()) {
            return false;
        }

        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (!email.matches(regexPattern)) {
            return false;
        }

        if (!Character.isDigit(number.charAt(0))) {
            return false;
        }

        if (date == null) { // Ensure a date is selected
            return false;
        }

        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (Period.between(localDate, LocalDate.now()).getYears() < 18) {
            return false;
        }

        return true;
    }



    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormat.parse(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                if (value instanceof GregorianCalendar) {
                    // Convert GregorianCalendar to Date
                    GregorianCalendar calendar = (GregorianCalendar) value;
                    return dateFormat.format(calendar.getTime());
                }
            }
            return "";
        }
    }

}


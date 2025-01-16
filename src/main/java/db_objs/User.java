package db_objs;

import Exceptions.InvalidConnection;
import Exceptions.NotFoundException;
import lombok.*;

import java.sql.*;


@Getter
@Setter
@AllArgsConstructor
public class User {
    private final int id;
    private final String username;
    private final String password;
    private final String role;

    public String[] getName() {
        try (Connection connection = DriverManager.getConnection(MyJDBC.DB_URL, MyJDBC.DB_USERNAME, MyJDBC.DB_PASSWORD)) {

            if (connection == null) {
                throw new InvalidConnection("Cannot connect to database");
            }

            // Determine the table name based on the user role
            String role = this.role.equals("STUDENT") ? "students" : "teachers";

            // Dynamically construct the SQL query
            String query = "SELECT first_name, last_name, email, cnp, dob FROM " + role + " WHERE user_id = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, this.id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String email = resultSet.getString("email");
                        String cnp = resultSet.getString("cnp");
                        String dob = resultSet.getString("dob");
                        return new String[]{firstName, lastName, email, cnp, dob};
                    } else {
                        throw new NotFoundException("User not found.");
                    }
                }
            }

        } catch (SQLException e) {
            throw new NotFoundException("Invalid user.");
        } catch (InvalidConnection e) {
            System.out.println(e.getMessage());
            return null; // Optionally, handle the exception as needed
        }
    }

    public String[] getAddress() {
        try (Connection connection = DriverManager.getConnection(MyJDBC.DB_URL, MyJDBC.DB_USERNAME, MyJDBC.DB_PASSWORD)) {

            if (connection == null) {
                throw new InvalidConnection("Cannot connect to database");
            }

            // Determine the table name based on the user role
            String role = this.role.equals("STUDENT") ? "students" : "teachers";

            // Dynamically construct the SQL query
            String query = "select c3.name as Country, c2.name as County, c.name as City,\n" +
                    "       r.street_name as Street, r.house_number as Number\n" +
                    "from " + role + " " +
                    "join public.residential r on r.id = " + role +".residential_id\n" +
                    "join public.city c on c.id = r.city_id\n" +
                    "join public.county c2 on c2.id = c.county_id\n" +
                    "join public.country c3 on c3.id = c2.country_id\n" +
                    "join public.users u on " + role + ".user_id = u.id\n" +
                    "where u.id = ?;";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, this.id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String country = resultSet.getString("country");
                        String county = resultSet.getString("county");
                        String city = resultSet.getString("city");
                        String street = resultSet.getString("street");
                        String number = resultSet.getString("number");
                        return new String[]{country, county, city, street, number};
                    } else {
                        throw new NotFoundException("User not found.");
                    }
                }
            }

        } catch (SQLException e) {
            throw new NotFoundException("Invalid user.");
        } catch (InvalidConnection e) {
            System.out.println(e.getMessage());
            return null; // Optionally, handle the exception as needed
        }
    }


}

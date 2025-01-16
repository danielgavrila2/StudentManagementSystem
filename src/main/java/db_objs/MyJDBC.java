package db_objs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MyJDBC {
    //configuratia bazei de date
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/Students";
    public static final String DB_USERNAME = "postgres";
    public static final String DB_PASSWORD = "inadydoc";

    public static User validateLogic(String username, String password){
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            if (connection == null) {
                System.out.println("Connection failed!");
            }
            else {
                System.out.println("Connection successful!");
            }

            //create sql query
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(
              "SELECT * FROM users where username = ? and password = ?"
            );

            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                int userId = resultSet.getInt("id");
                String role = resultSet.getString("role");

                return new User(userId, username, password, role);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }


    //true = register succesfully / false = registeration failed
    public static boolean register(String username, String password, String role){
        try{
            //prima oara trebuie sa verificam daca userul exista deja sau nu in BD
            if (!checkUser(username)){
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement preparedStatement = conn.prepareStatement(
                        "INSERT INTO users(username, password, role) " + "VALUES(?,?,?)"
                );

                preparedStatement.setString(1,username);
                preparedStatement.setString(2,password);
                preparedStatement.setString(3,role);

                preparedStatement.executeUpdate();

                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    //true = user exists, false  = user doesnt exist
    private static boolean checkUser(String username){
        try{
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return true;
    }

    public static ArrayList<String> getCountry() {

        ArrayList<String> countries = new ArrayList<String>();

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(
              "SELECT * FROM country"
            );
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                countries.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countries;
    }

    public static ArrayList<String> getCounty(String country) {

            ArrayList<String> counties = new ArrayList<String>();

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM county join country c on c.id = county.country_id WHERE c.name = ? "
            );
            preparedStatement.setString(1,country);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                counties.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return counties;
    }

    public static ArrayList<String> getCity(String county) {

        ArrayList<String> cities = new ArrayList<String>();

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM city join public.county c on c.id = city.county_id WHERE c.name = ? "
            );
            preparedStatement.setString(1,county);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                cities.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities;
    }

    public static boolean registerResidential(String country, String county, String city, String street, String number) {
        try{

            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select city.id from city join public.county c on city.county_id = c.id\n" +
                            "join public.country c2 on c.country_id = c2.id\n" +
                            "where c2.name = ? and c.name = ? and city.name = ? "
            );

            preparedStatement.setString(1,country);
            preparedStatement.setString(2,county);
            preparedStatement.setString(3,city);

            ResultSet resultSet = preparedStatement.executeQuery();

            int cityId = 0;

            if(resultSet.next()){
               cityId = resultSet.getInt("id");
            }

            PreparedStatement preparedStatement2 = conn.prepareStatement(
              "INSERT INTO residential(city_id, street_name, house_number) VALUES (?,?,?)"
            );

            preparedStatement2.setInt(1, cityId);
            preparedStatement2.setString(2, street);
            preparedStatement2.setString(3, number);

            preparedStatement2.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }


    public static boolean updateResidential(int residential_id, String country, String county, String city, String street, String number) {
        try{

            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select city.id from city join public.county c on city.county_id = c.id\n" +
                            "join public.country c2 on c.country_id = c2.id\n" +
                            "where c2.name = ? and c.name = ? and city.name = ? "
            );

            preparedStatement.setString(1,country);
            preparedStatement.setString(2,county);
            preparedStatement.setString(3,city);

            ResultSet resultSet = preparedStatement.executeQuery();

            int cityId = 0;

            if(resultSet.next()){
                cityId = resultSet.getInt("id");
            }

            PreparedStatement preparedStatement2 = conn.prepareStatement(
                    "UPDATE residential\n" +
                            "SET city_id = ?,\n" +
                            "    street_name = ?,\n" +
                            "    house_number = ?\n" +
                            "WHERE id = ?;"
            );

            preparedStatement2.setInt(1, cityId);
            preparedStatement2.setString(2, street);
            preparedStatement2.setString(3, number);
            preparedStatement2.setInt(4, residential_id);

            preparedStatement2.executeUpdate();

            return true;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    public static int getCityId(String city) {
        try {

            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT id FROM city WHERE name = ?"
            );
            preparedStatement.setString(1,city);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                int id = resultSet.getInt("id");
                return id;
            }

        } catch (SQLException e){
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean fillForm(int userID, String username, String firstname, String lastname, String email, String cnp,
                                   Date date, String country, String county, String city, String street, String number) {
        try {
            if (checkUser(username)) {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement preparedStatement = conn.prepareStatement(
                        "SELECT id, role FROM users WHERE username = ?"
                );
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                int userId;
                String role = "";
                String query = "";

                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                    role = resultSet.getString("role");
                } else {
                    return false; // User not found
                }

                int residentialID = -1;

                // Register residential address
                if (registerResidential(country, county, city, street, number)) {
                    int cityId = getCityId(city);

                    if (cityId != -1) {
                        PreparedStatement preparedStatement2 = conn.prepareStatement(
                                "SELECT id FROM residential WHERE city_id = ? AND street_name = ? AND house_number = ?"
                        );

                        preparedStatement2.setInt(1, cityId); // Use setInt for integers
                        preparedStatement2.setString(2, street);
                        preparedStatement2.setString(3, number);

                        ResultSet resultSet2 = preparedStatement2.executeQuery();

                        if (resultSet2.next()) {
                            residentialID = resultSet2.getInt("id");
                        }
                    }
                } else {
                    return false; // Failed to register residential address
                }

                // Determine the query based on user role
                if (role.equals("STUDENT")) {
                    query = "INSERT INTO students(user_id, first_name, last_name, email, dob, residential_id, cnp) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                } else if (role.equals("TEACHER")) {
                    query = "INSERT INTO teachers(user_id, first_name, last_name, email, hire_date, residential_id, cnp) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
                } else {
                    return false; // Unknown role
                }

                // Insert user data
                PreparedStatement preparedStatement3 = conn.prepareStatement(query);
                preparedStatement3.setInt(1, userID); // Use setInt for integers
                preparedStatement3.setString(2, firstname);
                preparedStatement3.setString(3, lastname);
                preparedStatement3.setString(4, email);
                preparedStatement3.setDate(5, date); // Pass the Date object directly
                preparedStatement3.setInt(6, residentialID); // Use setInt for integers
                preparedStatement3.setString(7, cnp);

                preparedStatement3.executeUpdate(); // Use executeUpdate for INSERT, UPDATE, DELETE

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean updateForm(int userID, String username, String firstname, String lastname, String email, String cnp,
                                     Date date, String country, String county, String city, String street, String number) {
        try {
            if (checkUser(username)) {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement preparedStatement = conn.prepareStatement(
                        "SELECT id, role FROM users WHERE username = ?"
                );
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                int userId;
                String role = "";
                String query = "";

                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                    role = resultSet.getString("role");
                } else {
                    return false; // User not found
                }

                int residentialID = -1;

                // Get city ID based on provided city, country, and county
                int cityId = getCityId(city);

                if (cityId != -1) {
                    // Check if the residential address already exists
                    PreparedStatement preparedStatement2 = conn.prepareStatement(
                            "SELECT id FROM residential WHERE city_id = ? AND street_name = ? AND house_number = ?"
                    );
                    preparedStatement2.setInt(1, cityId); // Use setInt for integers
                    preparedStatement2.setString(2, street);
                    preparedStatement2.setString(3, number);

                    ResultSet resultSet2 = preparedStatement2.executeQuery();

                    if (resultSet2.next()) {
                        residentialID = resultSet2.getInt("id"); // Found existing residential ID
                    } else {
                        // If residential address does not exist, register a new address
                        if (updateResidential(residentialID, country, county, city, street, number)) {
                            // Get the residential ID after updating
                            preparedStatement2.setInt(1, cityId); // Use setInt for integers
                            preparedStatement2.setString(2, street);
                            preparedStatement2.setString(3, number);
                            resultSet2 = preparedStatement2.executeQuery();
                            if (resultSet2.next()) {
                                residentialID = resultSet2.getInt("id");
                            }
                        } else {
                            return false; // Failed to update residential address
                        }
                    }
                } else {
                    return false; // Invalid city
                }

                // Update user data based on role
                if (role.equals("STUDENT")) {
                    query = "UPDATE students SET first_name = ?, last_name = ?, email = ?, dob = ?, residential_id = ?, cnp = ? WHERE user_id = ?";
                } else if (role.equals("TEACHER")) {
                    query = "UPDATE teachers SET first_name = ?, last_name = ?, email = ?, dob = ?, residential_id = ?, cnp = ? WHERE user_id = ?";
                } else {
                    return false; // Unknown role
                }

                // Update user information in the corresponding table
                PreparedStatement preparedStatement3 = conn.prepareStatement(query);
                preparedStatement3.setString(1, firstname);
                preparedStatement3.setString(2, lastname);
                preparedStatement3.setString(3, email);
                preparedStatement3.setDate(4, date); // Pass the Date object directly
                preparedStatement3.setInt(5, residentialID); // Use setInt for integers
                preparedStatement3.setString(6, cnp);
                preparedStatement3.setInt(7, userID); // Use setInt for integers

                preparedStatement3.executeUpdate(); // Execute update for user data

                return true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }




    public static List<String[]> getGradesForStudent(int userId) {
        List<String[]> grades = new ArrayList<>();

        String query = """
        select grade, c.name, assigning_date from grades join public.courses c on c.id = grades.course_id
        join public.students s on grades.student_id = s.id
        join public.users u on s.user_id = u.id where u.id = ?;
    """;

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String grade = resultSet.getString("grade");
                    String courseName = resultSet.getString("name");
                    String assigningDate = resultSet.getString("assigning_date");

                    // Add the grade and course name as a String array to the list
                    grades.add(new String[]{courseName, grade, assigningDate});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return grades; // Return the populated list
    }


    public static List<String[]> getCoursesForStudent(int userId) {
        List<String[]> courses = new ArrayList<>();

        String query = """
        select courses.name, t.first_name, t.last_name, courses.date, courses.start_time, courses.end_time
                                                               from courses
                                                               join public.attendance a on courses.id = a.course_id
                                                               join public.students s on a.student_id = s.id
                                                               join public.teachers t on t.id = courses.teacher_id
                                                               join public.users u on s.user_id = u.id
                                                               where u.id = ?;
                                                               
    """;

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String course = resultSet.getString("courses.name");
                    String first_name = resultSet.getString("t.first_name");
                    String last_name = resultSet.getString("t.last_name");
                    String date = resultSet.getString("courses.date");
                    String start_time = resultSet.getString("courses.start_time");
                    String end_time = resultSet.getString("courses.end_time");

                    // Add the grade and course name as a String array to the list
                    courses.add(new String[]{course, first_name, last_name, date, start_time, end_time});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return courses; // Return the populated list
    }

    public static List<String[]> getAllUsers() {
        List<String[]> users = new ArrayList<>();

        String query = """
        select * from users;
    """;

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
          //  preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String role = resultSet.getString("role");

                    // Add the grade and course name as a String array to the list
                    users.add(new String[]{username, role});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return users; // Return the populated list

    }


    public static User getUserByUsername(String username) {

        String query = """
        select * from users where username=?;
    """;

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
              preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role");

                    return new User(id, username, password, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return null;

    }

    public static List<String[]> getCourses() {

        List<String[]> courses = new ArrayList<>();

        String query = """
        SELECT name, description, t.first_name AS teacher_first, t.last_name AS teacher_last, 
               date AS course_date, start_time, end_time
        FROM courses
        JOIN public.teachers t ON courses.teacher_id = t.id;
    """;

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String course = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    String firstName = resultSet.getString("teacher_first");
                    String lastName = resultSet.getString("teacher_last");
                    String courseDate = resultSet.getString("course_date");
                    String startTime = resultSet.getString("start_time");
                    String endTime = resultSet.getString("end_time");

                    // Add the course information as a String array to the list
                    courses.add(new String[]{course, description, firstName + " " + lastName, courseDate});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception for debugging
        }

        return courses; // Return the populated list
    }
}

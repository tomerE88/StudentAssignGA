package com.example.Model.Database;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLException;

import com.example.Model.ClassRoom;
import com.example.Model.Major;
import com.example.Model.SpecialRequest;
import com.example.Model.Student;

public class DB {
    private Connection connection; // the connection to the mysqlS
    
    // constructor
    public DB() {
        this.connection = null; // if wont connect connection will be null
        this.connectSql(); // connect the database
    }

    // connect to the mysql database
    public void connectSql() {
        String url = "jdbc:mysql://localhost:3306/assignstudents?serverTimezone=Asia/Jerusalem&useSSL=false";
        String username = "root";
        String password = "Tomer1234";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found: " + e);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // disconnect the sql
    public void disconnectSql() {
        try {
            // Check if the connection is not null
            if (this.connection != null) {
                this.connection.close(); // Close the connection
                this.connection = null; // Set connection to null to avoid reuse
            }
        }
        catch (SQLException e) {
            System.out.println("Error closing the database connection: " + e.getMessage());
        }
    }


    // summon function FitnessFunction from the database, and save the result in a double variable
    public double fitnessFunction(int majorPrefRank, int friendsRank, int sameCityRank, int genderRank, int specialAssignRank, int studentTypeRank, int gradesRank) {
        double fitness = 0;
        try {
            // the query that gets the FitnessFunction function from the database
            String fitnessStr = "SELECT FitnessFunction(?, ?, ?, ?, ?, ?, ?)";
            // create a statement
            PreparedStatement statement = this.connection.prepareStatement(fitnessStr);
            // set all the parameters of the function
            statement.setInt(1, majorPrefRank);
            statement.setInt(2, friendsRank);
            statement.setInt(3, sameCityRank);
            statement.setInt(4, genderRank);
            statement.setInt(5, specialAssignRank);
            statement.setInt(6, studentTypeRank);
            statement.setInt(7, gradesRank);
            // execute the query
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // save the result in the variable
                fitness = resultSet.getDouble(1);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return fitness;
    }
    

    // get array of major preferences and a studentID and
    // add to the major array the three major preferences of the student sorted by the preference
    // so that the first preference will be at index 0 and so on
    public void preferencesForStudent(Major[] mp, String studID) {

        try {
        PreparedStatement statement = this.connection.prepareStatement("SELECT s.studentID, m.majorID, m.name, sp.preference\n" +
        "FROM students AS s\n" +
        "JOIN studentpreferences AS sp ON s.studentID = sp.studentID\n" +
        "JOIN major AS m ON sp.majorID = m.majorID\n" +
        "WHERE sp.preference <= 3\n" +
        "AND sp.studentID = ?\n" +
        "ORDER BY sp.preference;");
        statement.setString(1, studID);
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next() && i<3) {
                mp[i].majorID = resultSet.getInt(2);
                System.out.println(mp[i].getMajorID());
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // get array of friends and a studentID and add the friends of the student order by the preference
    public void bestFriends(int[] friends, String studID) {
        try {
        PreparedStatement statement = this.connection.prepareStatement("SELECT friendID, preference\n" +
        "    FROM friends\n" + 
        "    WHERE studentID = ?\n" +
        "    ORDER BY preference;");
        statement.setString(1, studID);
        System.out.println("hello2");
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next() && i<3) {
                friends[i] = resultSet.getInt(1);
                System.out.println(friends[i]);
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    // the procedure return array of students and insert every student from the database to this array
    public HashMap<String, Student> getAllStudents() {
        HashMap<String, Student> students = new HashMap<String, Student>();
        try {
            // a query that gets all the students sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM students\n" +
            "ORDER BY studentID;");
            ResultSet resultSet = statement.executeQuery();
            // for each of the students that we got, insert into the array
            while (resultSet.next()) {
                students.put(resultSet.getString("studentID"), 
                new Student(
                    resultSet.getString("studentID"),
                    resultSet.getString("name"),
                    resultSet.getString("gender"),
                    resultSet.getDouble("averageGrades"),
                    resultSet.getInt("cityID"), 
                    resultSet.getInt("codeType")));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return students;
    }

    // get number of special requests in the database
    public int getNumSpecialRequests() {
        int numSpecialRequests = 0;
        try {
            // create a statement
            // the query that gets the number of special requests
            PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*) FROM specialrequests;");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                numSpecialRequests = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return numSpecialRequests;
    }

    // get all special requests from the database and insert them into a SpecialRequest array
    public SpecialRequest[] getAllSpecialRequests() {

        int countSpecialRequests = getNumSpecialRequests();
        SpecialRequest[] specialRequests = new SpecialRequest[countSpecialRequests];
        try {
            // create a statement
            // the query that gets all the special requests sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM specialrequests\n" +
            "ORDER BY studentID1, studentID2;");
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                specialRequests[i] = new SpecialRequest(
                    resultSet.getString("studentID1"),
                    resultSet.getString("studentID2"),
                    resultSet.getString("reason"));
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return specialRequests;
    }

    // the procedure return map of classroomIDs and classrooms and insert every classroom from the database to this array
    public ClassRoom[] getAllClassrooms() {
        int i = 0;
        int numofclasses = getNumClassrooms();
        ClassRoom[] classrooms = new ClassRoom[numofclasses];
        try {
            // create a statement
            // the query that gets all the classrooms sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM classrooms\n" +
            "ORDER BY classroomID;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                classrooms[i] = (
                new ClassRoom(
                    resultSet.getInt("classroomID"),
                    resultSet.getString("classroomName"),
                    resultSet.getInt("maxStudents"),
                    resultSet.getInt("minStudents"),
                    resultSet.getInt("classroomMajor")));
                    i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return classrooms;
    }

    // get number of classerooms in the database
    public int getNumClassrooms() {
        int numClassrooms = 0;
        try {
            // create a statement
            // the query that gets the number of classrooms
            PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*) FROM classrooms;");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                numClassrooms = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return numClassrooms;
    }

    // get all the majors from the database and insert them into a map
    public HashMap<Integer, Major> getAllMajors() {
        HashMap<Integer, Major> majors = new HashMap<Integer, Major>();
        try {
            // create a statement
            // the query that gets all the majors sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM majors\n" +
            "ORDER BY majorID;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                majors.put(resultSet.getInt("majorID"), 
                new Major(
                    resultSet.getInt("majorID"),
                    resultSet.getString("majorName"), 
                    resultSet.getDouble("requiredGrade")));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return majors;
    }

    // get all the cities from the database and insert them into a map
    public Map<Integer, String> getAllCities() {
        Map<Integer, String> cities = new HashMap<Integer, String>();
        try {
            // create a statement
            // the query that gets all the cities sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM city\n" +
            "ORDER BY cityID;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cities.put(resultSet.getInt("cityID"), resultSet.getString("name"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return cities;
    }

    // get all friends of a student and insert them into a String array of max length 3
    public Student[] getFriendsFromID(String studentID) {
        Student[] friends = new Student[3];
        Student friend;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT friendID\n" +
            "FROM friends\n" + 
            "WHERE studentID = ?\n" +
            "ORDER BY preference;");
            statement.setString(1, studentID);
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next() && i<3) {
                friend = getStudentFromID(resultSet.getString(1));
                friends[i] = new Student (friend);
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return friends;
    }
    // get all major preferences of a student and insert them into a Major array of max length 3
    public Major[] getMajorPreferencesFromID(String studentID) {
        Major[] majorPreferences = new Major[3];
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT m.majorID, m.majorName, m.requiredGrade, mp.preference\n" +
            "FROM students AS s\n" +
            "JOIN majorspreferences AS mp ON s.studentID = mp.studentID\n" +
            "JOIN majors AS m ON mp.majorID = m.majorID\n" +
            "WHERE mp.preference <= 3\n" +
            "AND mp.studentID = ?\n" +
            "ORDER BY mp.preference;");
            statement.setString(1, studentID);
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next() && i<3) {
                majorPreferences[i] = new Major(resultSet.getInt(1), resultSet.getString(2), resultSet.getDouble(3));
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return majorPreferences;
    }
    

    // get studentID and return the student
    public Student getStudentFromID(String studentID) {
        Student stud;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT *\n" + // select ID
            "FROM students s\n" + // from students
            "WHERE s.studentID = ?;"); // except for the student itself
            statement.setString(1, studentID);
            ResultSet resultSet = statement.executeQuery();
            // checks if there is a student with this ID
            if (resultSet.next()) {
                // creates a new student
                stud = new Student(
                        resultSet.getString("studentID"),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getDouble("averageGrades"),
                        resultSet.getInt("cityID"), 
                        resultSet.getInt("codeType")
                    );
                    return stud; // Return the created Student object
                }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null; // no student with this ID
    }

    // get classroom from the database with the given ID
    public ClassRoom getClassRoomFromID(int classID) {
        ClassRoom classRoom;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT *\n" +
            "FROM classroom c\n" + 
            "WHERE c.classroomID = ?;");
            statement.setInt(1, classID);
            ResultSet resultSet = statement.executeQuery();
            // checks if there is a student with this ID
            if (resultSet.next()) {
                // creates a new student
                classRoom = new ClassRoom(
                        resultSet.getInt("classroomID"),
                        resultSet.getString("classroomName"),
                        resultSet.getInt("maxStudents"),
                        resultSet.getInt("minStudents"),
                        resultSet.getInt("classroomMajor")
                    );
                    return classRoom;
                }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null; // no class with this ID
    }




    public static void main(String[] args) {

        try {

            DB db = new DB();
            // make a student out of studentID 100000002 and print all ID's of students in same city as him
            Student stud;
            double fitness = db.fitnessFunction(1, 2, 3, 4, 5, 6, 7);
            stud = db.getStudentFromID("100000002");
            System.out.println("*****************************");
            System.out.println("gerso");
            System.out.println(stud.getName());
            System.out.println("fitness: " + fitness);
            System.out.println("*****************************");
    
                db.disconnectSql();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        }
}
package com.example.Controller.Database;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.example.Controller.ClassRoom;
import com.example.Controller.Major;
import com.example.Controller.Student;

public class DB {
    private Connection connection; // the connection to the mysqlS
    
    // constructor
    public DB() {
        this.connection = null; // if wont connect connection will be null
        this.connectSql(); // connect the database
    }

    // connect to the mysql database
    public void connectSql() {
        String url = "jdbc:mysql://localhost:3306/assignstudents?serverTimezone=Asia/Jerusalem";
        String username = "root";
        String password = "Tomer1234";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
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
        } catch (SQLException e) {
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
    public Map<String, Student> getAllStudents() {
        Map<String, Student> students = new HashMap<String, Student>();
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
                    resultSet.getInt("cityID")));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return students;
    }

    // the procedure return array of classrooms and insert every classroom from the database to this array
    public Map<String, ClassRoom> getAllClassrooms() {
        Map<String, ClassRoom> classrooms = new HashMap<String, ClassRoom>();
        try {
            // create a statement
            // the query that gets all the students sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM classroom\n" +
            "ORDER BY classID;");
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                classrooms.put(resultSet.getString("classID"), 
                new ClassRoom(
                    resultSet.getInt("classID"),
                    resultSet.getString("className"),
                    resultSet.getInt("maxStudents"),
                    resultSet.getInt("minStudents"),
                    resultSet.getInt("majorID")));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return classrooms;
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
                        resultSet.getInt("cityID")
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
            "WHERE c.classID = ?;");
            statement.setInt(1, classID);
            ResultSet resultSet = statement.executeQuery();
            // checks if there is a student with this ID
            if (resultSet.next()) {
                // creates a new student
                classRoom = new ClassRoom(
                        resultSet.getInt("classID"),
                        resultSet.getString("className"),
                        resultSet.getInt("maxStudents"),
                        resultSet.getInt("minStudents"),
                        resultSet.getInt("majorID")
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
package com.example.Controller;
import java.sql.*;
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
        String url = "jdbc:mysql://localhost:3306/mydb?serverTimezone=Asia/Jerusalem";
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
                System.out.println(mp[i].majorID);
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

    public int getNumOfStudents() {
        int count = 0;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*)\n" +
            "FROM students;");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    // the procedure return array of students and insert every student from the database to this array
    public Student[] getAllStudents() {
        int count = getNumOfStudents();
        Student[] students = new Student[count];
        try {
            // create a statement
            // the query that gets all the students sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM students\n" +
            "ORDER BY studentID;");
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            // for each of the students that we got, insert into the array
            while (resultSet.next()) {
                students[i].studentID = resultSet.getInt(1);
                students[i].name = resultSet.getString(2);
                students[i].gender = resultSet.getString(3);
                students[i].averageGrades = resultSet.getDouble(4);
                students[i].cityID = resultSet.getInt(5);
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return students;
    }


    public int getNumOfClasses() {
        int count = 0;
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT COUNT(*)\n" +
            "FROM classRoom;");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    // the procedure return array of classrooms and insert every classroom from the database to this array
    public Student[] getAllClassroom() {
        int count = getNumOfStudents();
        Student[] students = new Student[count];
        try {
            // create a statement
            // the query that gets all the students sorted by their ID
            PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM classroom\n" +
            "ORDER BY classID;");
            ResultSet resultSet = statement.executeQuery();
            int i = 0;
            // for each of the students that we got, insert into the array
            // while (resultSet.next()) {
            //     students[i].studentID = resultSet.getInt(1);
            //     students[i].name = resultSet.getString(2);
            //     students[i].gender = resultSet.getString(3);
            //     students[i].averageGrades = resultSet.getDouble(4);
            //     students[i].cityID = resultSet.getInt(5);
            //     i++;
            // }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return students;
    }


    // return all studentID's of every student that is in the same city as the studentID
    // will not include the selected student
    public int sameCity(int[] citizens, Student stud) {
        int i = 0;
        try {
        PreparedStatement statement = this.connection.prepareStatement("SELECT s.studentID\n" + 
        "FROM students s\n" + // from students
        "WHERE s.cityID = ?\n" + // all students that live in same city as the given student 
        "AND s.studentID != ?;"); // except for the student itself
        statement.setInt(1, stud.cityID);
        statement.setInt(2, stud.studentID);
        ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                citizens[i] = resultSet.getInt(1);
                System.out.println(citizens[i]);
                i++;
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return i;
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
                        resultSet.getInt("studentID"),
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

        DB db = new DB();
        // String url = "jdbc:mysql://localhost:3306/mydb?serverTimezone=Asia/Jerusalem";
        // String username = "root";
        // String password = "Tomer1234";
        try {
            // Class.forName("com.mysql.cj.jdbc.Driver");
            // Connection connection = DriverManager.getConnection(url, username, password);
            // Statement statement = connection.createStatement();


            // make a student out of studentID 100000002 and print all ID's of students in same city as him
            Student stud;
            int citizens[] = new int[10];
            stud = db.getStudentFromID("100000002");
            System.out.println("*****************************");
            System.out.println("gerso");
            System.out.println(stud.name);
            int l = db.sameCity(citizens, stud);
            System.out.println("*****************************");

            
            // int studID = 100000002;
            // String majorStr = "SELECT s.studentID, m.majorID, m.name, sp.preference\n" + //
            //                     "FROM students AS s\n" + //
            //                     "JOIN studentpreferences AS sp ON s.studentID = sp.studentID\n" + //
            //                     "JOIN major AS m ON sp.majorID = m.majorID\n" + //
            //                     "WHERE sp.preference <= 3\n" + //
            //                     "AND sp.studentID = " + studID +"\n"+ //
            //                     "ORDER BY sp.preference;";
            // String dfdff = "select studentID, name, gender from students order by studentID;";

            //     ResultSet resultSet = statement.executeQuery(dfdff);
                // int i = 0;
                // while (resultSet.next() && i<3) {
                //     mp[i].majorID = resultSet.getInt(2);
                //     System.out.println(mp[i].majorID);
                //     i++;
                // }
                // int i = 0;
                // while (resultSet.next()) {
                //     System.out.println(resultSet.getString(1));
                //     System.out.println(resultSet.getString(2));
                //     System.out.println(resultSet.getString(3));
                //     i++;
                // }
    
                db.disconnectSql();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        }
}
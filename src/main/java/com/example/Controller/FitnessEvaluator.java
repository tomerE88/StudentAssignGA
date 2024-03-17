package com.example.Controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import com.example.Controller.Database.DB;

public class FitnessEvaluator {

    private Student[] students;
    private ClassRoom[] classrooms;


    public FitnessEvaluator(Student[] students, ClassRoom[] classrooms) {
        this.students = students;
        this.classrooms = classrooms;  
    }

    // getters and setters
    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }

    public ClassRoom[] getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(ClassRoom[] classrooms) {
        this.classrooms = classrooms;
    }

    private HashMap<String, ClassRoom> createStudentClassroomMap() {
        // Create a mapping from student ID (String) to classroom
        HashMap<String, ClassRoom> studentClassroomMap = new HashMap<>();
        // add the students and their classrooms to the map
        for (ClassRoom classroom : classrooms) {
            for (Student student : classroom.getStudents().values()) {
                studentClassroomMap.put(student.getStudentID(), classroom);
            }
        }
        return studentClassroomMap;
    }

    // calculate the score that this assignment get for major preference
    // the number will be between 0 and 100 and will be calculated by the following formula:
    // the (current score / max score) * 100
    private double percentageMajorPreferenceScore() {
        double maxScore = students.length * 3; // Max score calculation remains the same
        double currentScore = 0;
    
        // Create a map from student ID to classroom
        HashMap<String, ClassRoom> studentClassroomMap = createStudentClassroomMap();
    
        // Calculate the score based on major preferences
        for (Student student : students) {
            ClassRoom classroom = studentClassroomMap.get(student.getStudentID());
            if (classroom != null) { // Add null check here
                Major[] majorPreferences = student.getMajorPreferences();
                boolean found = false; // Initialize found to false
    
                for (int k = 0; k < majorPreferences.length && !found; k++) {
                    if (classroom.getMajorID() == majorPreferences[k].getMajorID()) {
                        currentScore += (3 - k);
                        found = true; // Found the preferred major, no need to check further
                    }
                }
            }
        }
    
        double percentage = (currentScore / maxScore) * 100;
        return percentage;
    }

    // return the score that this assignment get for friends
    private double percentageFriendsScore() {
        double maxScore = students.length * 6; // Max score calculation remains the same
        double currentScore = 0;
    
        // Create a mapping from student ID to classroom
        HashMap<String, ClassRoom> studentClassroomMap = createStudentClassroomMap();
    
        // Calculate the score based on friends in the same classroom
        for (Student student : students) {
            // classroom of the current student
            ClassRoom studentClassroom = studentClassroomMap.get(student.getStudentID());
            // friends of curre2nt student
            Student[] friends = student.getFriends();
    
            // loop for each friend (max of 3 friends)
            for (int k = 0; k < friends.length; k++) {
                // classroom of the friend
                ClassRoom friendClassroom = studentClassroomMap.get(friends[k].getStudentID());
                if (studentClassroom != null && studentClassroom.equals(friendClassroom)) {
                    // Add the score to the current score, decreasing as the friend's priority decreases
                    currentScore += (3 - k);
                }
            }
        }
    
        double percentage = (currentScore / maxScore) * 100;
        return percentage;
    }
    
    // return the score that this assignment get for same city
    // checks if each student has at least one other student in same city as him in class
    private double percentageSameCity() {
        double totalStudents = students.length; // Total students
        double sameCityCount = 0; // Counter for students with at least one classmate from the same city

        // Iterate through each classroom
        for (ClassRoom classroom : classrooms) {
            HashMap<Integer, Integer> cityCounts = new HashMap<>(); // Hash map to count occurrences of each city ID
            
            // Populate the map with city occurrences for the classroom
            for (Student student : classroom.getStudents().values()) {
                int cityID = student.getCityID();
                // add 1 to the value of the cityID key in the hash map
                cityCounts.put(cityID, cityCounts.getOrDefault(cityID, 0) + 1);
            }

            // Count if there's more than one student from the same city
            for (int count : cityCounts.values()) {
                if (count > 1) {
                    sameCityCount += count; // Add all students from cities with more than one student with them
                }
            }
        }

        // Calculate and return the percentage
        double percentage = (sameCityCount / totalStudents) * 100;
        return percentage;
    }

    // return the score from 0 to -100 that this assignment for difference between males and females in each class
    private double percentageGenderDifference() {
        double totalStudents = 0;
        double genderDifference = 0;
        int maleCount;
        int femaleCount;
        double percentage = 0;
        String gender;
        // array of student in class
        Student[] studentsInClass;

        totalStudents = students.length;

        // loop through all the classes and check the difference
        for (int i = 0; i < classrooms.length; i++) {
            maleCount = 0;
            femaleCount = 0;
            studentsInClass = classrooms[i].getStudents().values().toArray(new Student[0]);
            for (int j = 0; j < studentsInClass.length; j++) {
                gender = studentsInClass[j].getGender();
                // check the gender of the student
                if (gender.equals("M"))
                    maleCount++;
                else if (gender.equals("F"))
                    femaleCount++;
            }
            // Absolute value of the difference in the class
            genderDifference += Math.abs(maleCount - femaleCount);
        }

        // calculate the percentage
        percentage = (genderDifference / totalStudents) * (-100);
        return percentage;
    }

    // return score of number of special request in same class from 0 to -100
    private double percentageSpecialRequestAssignments() {
        DB db = new DB();
        double totalSpecialRequests;
        double totalassignments = 0;
        double percentage = 0;
         // array of student in class
         SpecialRequest[] specialRequests = db.getAllSpecialRequests();

        totalSpecialRequests = specialRequests.length;

        // loop through all the special requests and check if the students are in the same class
        for (int i = 0; i < specialRequests.length; i++) {
            for (int j = 0; j < classrooms.length; j++) {
                // checks if both students in the same class
                if (classrooms[j].isStudentInClass(specialRequests[i].getStudentID1()) && classrooms[j].isStudentInClass(specialRequests[i].getStudentID2())) {
                    totalassignments++;
                }
            }
        }
        // calculate the percentage
        percentage = (totalassignments / totalSpecialRequests) * (-100);
        return percentage;
    }

    // calculate the standard deviation of the number of students with code type 2 in each class
    private double calculateStandardDeviationMerge() {
        int totalcode2 = 0;
        int[] code2InClass = new int[classrooms.length];
        double avgType2 = 0;
        double varienceSum = 0;
        double stddev = 0;

        // loop through all the classes and check the difference
        for (int i = 0; i < classrooms.length; i++) {
            code2InClass[i] = 0;
            for (Student student : classrooms[i].getStudents().values()) {
                if (student.getCodeType() == 2) {
                    code2InClass[i]++;
                    totalcode2++;
                }
            }
        }

        // calculate the average of code type 2 students in each class
        avgType2 = totalcode2 / classrooms.length;

        for (int i = 0; i < classrooms.length; i++) {
            // sum square of differences from average for each class
            varienceSum += Math.pow(code2InClass[i] - avgType2, 2);
        }

        // calculate the standard deviation
        stddev = Math.sqrt(varienceSum / classrooms.length);

        return stddev;
    }

    // calculate the max standard deviation of the number of students with code type 2 in each class
    // the max standard deviation is if the students with code 2 are not divided equally between the classes
    private double maxStandardDeviationMerge() {
        double totalcode2 = 0;
        double avgType2 = 0;
        double varienceSum = 0;
        double maxstddev = 0;
        ClassRoom[] classrooms = this.classrooms;
        int j = 0;


        // sort the classes by the max capacity
        Arrays.sort(classrooms, new Comparator<ClassRoom>() {
            public int compare(ClassRoom c1, ClassRoom c2) {
                return Integer.compare(c2.getMaxStudents(), c1.getMaxStudents());
            }
        });

        // total students with code type 2
        for (int i = 0; i < students.length; i++) {
            if (students[i].getCodeType() == 2) {
                totalcode2++;
            }
        }

        // calculate the average of code type 2 students in each class
        avgType2 = totalcode2 / classrooms.length;


        while (totalcode2 > classrooms[j].getMaxStudents()) {
            // sum square of differences from average for each class
            varienceSum += Math.pow(classrooms[j].getMaxStudents() - avgType2, 2);
            totalcode2 -= classrooms[j].getMaxStudents();
            j++;
        }

        // print length of classes
        System.out.println("length: " + classrooms.length);
        // print total code 2
        System.out.println("totalcode2: " + totalcode2);
        // print average
        System.out.println("avgType2: " + avgType2);
        // less than max capacity of class
        varienceSum += Math.pow(totalcode2 - avgType2, 2);

        // calculate the standard deviation
        maxstddev = Math.sqrt(varienceSum / classrooms.length);

        return maxstddev;
    }

    // checks if the students with codetype 2 divided between classes equaly
    // return number between 0 and -100
    private double percentageStandardDeviationMerge() {
        // calculate the standard deviation
        double stddev = calculateStandardDeviationMerge();
        // calculate the max standard deviation
        double maxstddev = maxStandardDeviationMerge();
        double percentage = 0;


        if (maxstddev == 0) {
            return 0;
        }

        // calculate the percentage
        percentage = (stddev / maxstddev) * (-100);
        return percentage;
    }

    // return the score that this assignment get for students that have the required grades in their classes
    private double percentageRequiredGrade() {
        DB db = new DB();
        double totalAccepted = 0;
        double totalStudents = 0;
        double percentage = 0;
        HashMap<Integer, Major> majors = db.getAllMajors();

        // loop through all the students and check if the student is above the minimum grade for the major's class
        for (int i = 0; i < classrooms.length; i++) {
            for (Student student : classrooms[i].getStudents().values()) {
                // get the major of the class
                Major major = majors.get(classrooms[i].getMajorID());

                if (student.getAverageGrades() >= major.getRequiredGrade()) {
                    totalAccepted++;
                }
                totalStudents++;
            }
        }

        // calculate the percentage
        percentage = (totalAccepted / totalStudents) * 100;
        return percentage;
    }

    // Fitness function that calculates the fitness score of a given individual based on the weights of the different factors
    public double fitnessFunction(int majorPrefRank, int friendsRank, int sameCityRank, int genderRank, int specialAssignRank, int studentTypeRank, int gradesRank) {
        double majorPrefScore = percentageMajorPreferenceScore() * majorPrefRank;
        double friendsScore = percentageFriendsScore() * friendsRank;
        double sameCityScore = percentageSameCity() * sameCityRank;
        double genderScore = percentageGenderDifference() * genderRank;
        double specialAssignScore = percentageSpecialRequestAssignments() * specialAssignRank;
        double studentTypeScore = percentageStandardDeviationMerge() * studentTypeRank;
        double gradesScore = percentageRequiredGrade() * gradesRank;

        System.out.println("majorPrefScore: " + majorPrefScore);
        System.out.println("friendsScore: " + friendsScore);
        System.out.println("sameCityScore: " + sameCityScore);
        System.out.println("genderScore: " + genderScore);
        System.out.println("specialAssignScore: " + specialAssignScore);
        System.out.println("studentTypeScore: " + studentTypeScore);
        System.out.println("gradesScore: " + gradesScore);

        // Calculate the fitness as the sum of all weighted scores
        double fitness = majorPrefScore + friendsScore + sameCityScore + genderScore + specialAssignScore + studentTypeScore + gradesScore;

        return fitness;
    }
}


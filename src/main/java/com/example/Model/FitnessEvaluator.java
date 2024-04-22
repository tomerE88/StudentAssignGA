package com.example.Model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class FitnessEvaluator {

    /*
     * get array of classrooms and
     * create a mapping from student ID to classroom
     */
    private static HashMap<String, ClassRoom> createStudentClassroomMap(ClassRoom[] classrooms) {
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

    /*
     calculate the score that this assignment get for major preference
     the number will be between 0 and 100 and will be calculated by the following formula:
     the (current score / max score) * 100
    */
    private static double percentageMajorPreferenceScore(Student[] students, ClassRoom[] classrooms) {
        double maxScore = students.length * 3; // Max score calculation remains the same
        double currentScore = 0;
    

        // this is O(n) because if there are n students and m classes it loops for each
        // class n/m students on average and for each student 3 preferences
        // so its O(3m * (n / m)) = O(3n) ~ O(n)
        for (ClassRoom classroom : classrooms) {
            for (Student student : classroom.getStudents().values()) {
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

    /*
     * the number will be between 0 and 100 and will be calculated by the following formula:
     * the (current score / max score) * 100
     * the current score is the sum of the priority of the friends in the same class
     * the max score is the number of students * 6 because each student has 3 friends (1 + 2 + 3 = 6)
     */
    private static double percentageFriendsScore(Student[] students, ClassRoom[] classrooms) {
        double maxScore = students.length * 6; // Max score calculation remains the same
        double currentScore = 0;
    
        // Create a mapping from student ID to classroom
        HashMap<String, ClassRoom> studentClassroomMap = createStudentClassroomMap(classrooms);
    
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
    
    /*
     * the number will be between 0 and 100 and will be calculated by the following formula:
     * the (current score / total students) * 100
     * the current score is the number of students with at least one classmate from the same city
     * the total students is the number of students
     */
    private static double percentageSameCity(Student[] students, ClassRoom[] classrooms) {
        double totalStudents = students.length; // Total students
        double sameCityCount = 0; // Counter for students with at least one classmate from the same city
        // Hash map to count occurrences of each city ID
        HashMap<Integer, Integer> cityCounts = new HashMap<>();

        // Iterate through each classroom
        for (ClassRoom classroom : classrooms) {
            
            // Populate the map with city occurrences for the classroom
            for (Student student : classroom.getStudents().values()) {
                int cityID = student.getCityID();
                if (cityCounts.containsKey(cityID))
                    // If the cityID already exists in the map, increment its count by 1
                    cityCounts.put(cityID, cityCounts.get(cityID) + 1);
                else
                    // If the cityID does not exist in the map, initialize its count as 1
                    cityCounts.put(cityID, 1);
            }

        }

        // Count if there's more than one student from the same city
        for (int count : cityCounts.values()) {
            if (count > 1) {
                sameCityCount += count; // Add all students from cities with more than one student with them
            }
        }

        // Calculate and return the percentage
        double percentage = (sameCityCount / totalStudents) * 100;
        return percentage;
    }

    /*
     * the number will be between 0 and 100 and will be calculated by the following formula:
     * the 100 - ((current score / total students) * 100)
     * the current score is the difference between the number of boys and girls
     * the total students is the number of students
     */
    private static double percentageGenderDifference(Student[] students, ClassRoom[] classrooms) {
        double totalStudents = 0;
        double genderDifference = 0;
        int maleCount;
        int femaleCount;
        double percentage = 0;
        String gender;
        // array of student in class
        HashMap<Integer, Student> studentsInClass;

        totalStudents = students.length;

        // loop through all the classes and check the difference
        for (int i = 0; i < classrooms.length; i++) {
            maleCount = 0;
            femaleCount = 0;
            studentsInClass = classrooms[i].getStudents();
            for (int j = 0; j < studentsInClass.size(); j++) {
                gender = studentsInClass.get(j).getGender();
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
        percentage = (100 - (genderDifference / totalStudents) * 100);
        return percentage;
    }

    /*
     * the number will be between 0 and 100 and will be calculated by the following formula:
     * the 100 - ((current score / total special requests) * 100)
     * the current score is the number of special requests that are in the same class
     * the total special requests is the number of special requests
     */
    private static double percentageSpecialRequestAssignments(ClassRoom[] classrooms, SpecialRequest[]specialRequests) {
        double totalSpecialRequests;
        double totalassignments = 0;
        double percentage = 0;

        totalSpecialRequests = specialRequests.length;

        // Create a mapping from student ID to classroom
        HashMap<String, ClassRoom> studentClassroomMap = createStudentClassroomMap(classrooms);

        // loop through all the special requests and check if the students are in the same class
        for (int i = 0; i < specialRequests.length; i++) {
            // check if both students in the same class
            if (studentClassroomMap.get(specialRequests[i].getStudentID1()).equals(studentClassroomMap.get(specialRequests[i].getStudentID2()))) {
                totalassignments++;
            }
        }
        // calculate the percentage
        percentage = 100 - ((totalassignments / totalSpecialRequests) * 100);
        return percentage;
    }

    /* 
     * calculate the standard deviation of the number of students with code type 2 in each class
     */
    private static double calculateStandardDeviationMerge(ClassRoom[] classrooms) {
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

    /*
     * sort the classrooms from most students they can have to least students they can have
     */
    private static ClassRoom[] sortClassrooms(ClassRoom[] classrooms) {
        // sort the classrooms by the max number of students they can have
        Arrays.sort(classrooms, new Comparator<ClassRoom>() {
            @Override
            public int compare(ClassRoom c1, ClassRoom c2) {
                return c2.getMaxStudents() - c1.getMaxStudents();
            }
        });
        return classrooms;
    }

    /*
     * calculate the max standard deviation of the number of students with code type 2 in each class 
     * the max standard deviation is if the students with code 2 are not divided equally between the classes
     */
    private static double maxStandardDeviationMerge(Student[] students, ClassRoom[] classrooms) {
        double totalcode2 = 0;
        double avgType2 = 0;
        double varienceSum = 0;
        double maxstddev = 0;
        int j = 0;

        // sort the classrooms by the max number of students they can have
        classrooms = sortClassrooms(classrooms);

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

        // less than max capacity of class
        varienceSum += Math.pow(totalcode2 - avgType2, 2);

        // calculate the standard deviation
        maxstddev = Math.sqrt(varienceSum / classrooms.length);

        return maxstddev;
    }

    /*
     * the number will be between 0 and 100 and will be calculated by the following formula:
     * the 100 - ((current score / max score) * 100)
     * the current score is the standard deviation of the number of students with code type 2 in each class
     * the max score is the max standard deviation of the number of students with code type 2 in each class
     * the max standard deviation is if the students with code 2 are not divided equally between the classes
     */
    private static double percentageStandardDeviationMerge(Student[] students, ClassRoom[] classrooms) {
        // calculate the standard deviation
        double stddev = calculateStandardDeviationMerge(classrooms);
        // calculate the max standard deviation
        double maxstddev = maxStandardDeviationMerge(students, classrooms);
        double percentage = 0;


        if (maxstddev == 0) {
            return 0;
        }

        // calculate the percentage
        percentage = 100 - ((stddev / maxstddev) * 100);
        return percentage;
    }

    // return the score that this assignment get for students that have the required grades in their classes
    private static double percentageRequiredGrade(ClassRoom[] classrooms, HashMap<Integer, Major> majors) {
        double totalAccepted = 0;
        double totalStudents = 0;
        double percentage = 0;

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

    /*
     * the max fitness is the sum of the max score of each factor
     */
    public static double getMaxFitness(int[] ranks) {
        // return the max fitness
        return 100;
    }

    // Fitness function that calculates the fitness score of a given individual based on the weights of the different factors
    public static double fitnessFunction(Student[] students, ClassRoom[] classrooms, HashMap<Integer, Major> majors, SpecialRequest[] specialRequests, int[] ranks) {
        int counter = 0;
        int sumRanks = 0;
        for (int i = 0; i < ranks.length; i++) {
            sumRanks += ranks[i];
        }
        for (int i = 0; i < classrooms.length; i++) {
            counter += classrooms[i].getNumStudents();
        }
        // positive value
        int majorPrefRank = ranks[0];
        double majorPrefScore = percentageMajorPreferenceScore(students, classrooms) * majorPrefRank;
        // positive value
        int friendsRank = ranks[1];
        double friendsScore = percentageFriendsScore(students, classrooms) * friendsRank;
        // positive value
        int sameCityRank = ranks[2];
        double sameCityScore = percentageSameCity(students, classrooms) * sameCityRank;
        // negitive value
        int genderRank = ranks[3];
        double genderScore = percentageGenderDifference(students, classrooms) * genderRank;
        // negetive value
        int specialAssignRank = ranks[4];
        double specialAssignScore = percentageSpecialRequestAssignments(classrooms, specialRequests) * specialAssignRank;
        // negetive value
        int studentTypeRank = ranks[5];
        double studentTypeScore = percentageStandardDeviationMerge(students, classrooms) * studentTypeRank;
        // positive value
        int gradesRank = ranks[6];
        double gradesScore = percentageRequiredGrade(classrooms, majors) * gradesRank;

        // // print all scores
        // System.out.println("major preference score: " + majorPrefScore);
        // System.out.println("friends score: " + friendsScore);
        // System.out.println("same city score: " + sameCityScore);
        // System.out.println("gender score: " + genderScore);
        // System.out.println("special assignment score: " + specialAssignScore);
        // System.out.println("student type score: " + studentTypeScore);
        // System.out.println("grades score: " + gradesScore);

        System.out.println("number of studsents in all classrooms: " + counter);

        // Calculate the fitness as the sum of all weighted scores
        double fitness = majorPrefScore + friendsScore + sameCityScore + genderScore + specialAssignScore + studentTypeScore + gradesScore;

        // return a number between 0 and 100 as the fitness
        return fitness / sumRanks;
    }
}


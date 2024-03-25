package com.example.Model;

public class SpecialRequest {

    private String studentID1;
    private String studentID2;
    private String reason;

    // constructor
    public SpecialRequest(String studentID1, String studentID2, String reason) {
        this.studentID1 = studentID1;
        this.studentID2 = studentID2;
        this.reason = reason;
    }

    // getters and setters
    public String getStudentID1() {
        return studentID1;
    }

    public void setStudentID1(String studentID1) {
        this.studentID1 = studentID1;
    }

    public String getStudentID2() {
        return studentID2;
    }

    public void setStudentID2(String studentID2) {
        this.studentID2 = studentID2;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}

package edu.uncc.inclass11;

import java.io.Serializable;
import java.util.UUID;

public class Grade implements Serializable {
    private String grade_id = UUID.randomUUID().toString();
    private String user_id;
    private String course_number;
    private String course_name;
    private Double course_hours;
    private String course_grade;

    public Grade(String user_id, String course_number, String course_name, Double course_hours, String course_grade) {
        this.user_id = user_id;
        this.course_number = course_number;
        this.course_name = course_name;
        this.course_hours = course_hours;
        this.course_grade = course_grade;
    }

    public String getGrade_id() {
        return grade_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCourse_number() {
        return course_number;
    }

    public String getCourse_name() {
        return course_name;
    }

    public Double getCourse_hours() {
        return course_hours;
    }

    public String getCourse_grade() {
        return course_grade;
    }

    public Grade setGrade_id(String grade_id) {
        this.grade_id = grade_id;
        return this;
    }

    public Grade setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public Grade setCourse_number(String course_number) {
        this.course_number = course_number;
        return this;
    }

    public Grade setCourse_name(String course_name) {
        this.course_name = course_name;
        return this;
    }

    public Grade setCourse_hours(Double course_hours) {
        this.course_hours = course_hours;
        return this;
    }

    public Grade setCourse_grade(String course_grade) {
        this.course_grade = course_grade;
        return this;
    }
}

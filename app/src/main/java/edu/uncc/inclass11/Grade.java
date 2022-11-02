package edu.uncc.inclass11;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.UUID;

public class Grade implements Serializable {
    private String grade_id = UUID.randomUUID().toString();
    private String user_id;
    private String course_number;
    private String course_name;
    private Double course_hours;
    private String course_grade;
    private Timestamp created_at;

    public Grade() {}

    public Grade(String user_id, String course_number, String course_name, Double course_hours, String course_grade) {
        this.user_id = user_id;
        this.course_number = course_number;
        this.course_name = course_name;
        this.course_hours = course_hours;
        this.course_grade = course_grade;
        this.created_at = Timestamp.now();
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    /**
     * @return Double
     */
    public Double calcCourse_points() {
        double points = 0.0;

        switch (getCourse_grade()) {
            case "A":
                points += 4 * getCourse_hours();
                break;
            case "B":
                points += 3 * getCourse_hours();
                break;
            case "C":
                points += 2 * getCourse_hours();
                break;
            case "D":
                points += 1 * getCourse_hours();
                break;
            default:
                points += 0 * getCourse_hours();
        }

        return points;
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

    public Grade setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
        return this;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "grade_id='" + grade_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", course_number='" + course_number + '\'' +
                ", course_name='" + course_name + '\'' +
                ", course_hours=" + course_hours +
                ", course_grade='" + course_grade + '\'' +
                ", created_at=" + created_at +
                '}';
    }
}

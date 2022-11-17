package ir.proprog.enrollassist.domain.studyRecord;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.GraduateLevel;
import ir.proprog.enrollassist.domain.course.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;


import java.util.*;

import static org.junit.Assert.*;

@RunWith (Parameterized.class)
public class StudyRecordTest {

    private StudyRecord studyRecord;

    private String courseLevel;
    private String studentLevel;
    private double grade;
    private boolean expected;

    public StudyRecordTest(String courseLevel, String studentLevel, double grade, boolean expected) {
        this.courseLevel = courseLevel;
        this.studentLevel = studentLevel;
        this.grade = grade;
        this.expected = expected;
    }

    @Parameters
    public static Collection<Object []> parameters() {
        return Arrays.asList(new Object [][]{
                {"Undergraduate", "Undergraduate", 9.0, false},
                {"Undergraduate", "Undergraduate", 10.0, true},
                {"Masters", "Masters", 11.0, false},
                {"Masters", "Masters", 13.0, true},
                {"PHD", "PHD", 13.0, false},
                {"PHD", "PHD", 14.0, true},
                {"Undergraduate", "Masters", 11.0, true},
                {"Undergraduate", "Masters", 9.0, false},
                {"Masters", "Undergraduate", 11.0, true},
                {"Undergraduate", "PHD", 10.0, true},
                {"Masters", "PHD", 11.0, false}
        });
    }

    @Test
    public void isPassedTest() throws ExceptionList {
        studyRecord = new StudyRecord("14011",
                new Course("4444444", "MATH1", 3, this.courseLevel),
                this.grade);
        assertTrue(studyRecord.isPassed(GraduateLevel.valueOf(studentLevel)) == this.expected);
    }
}

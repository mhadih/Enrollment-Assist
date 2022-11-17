package ir.proprog.enrollassist.domain.enrollmentList;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.EnrollmentRules.*;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.enrollmentList.EnrollmentList;
import ir.proprog.enrollassist.domain.major.Major;
import ir.proprog.enrollassist.domain.program.Program;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.PresentationSchedule;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EnrollmentListTest {

    private EnrollmentList enrollmentList;
    private Major ce;
    private Course math1;
    private Course phys1;
    private Course prog;
    private Course math2;
    private Course ap;
    private Course dm;
    private Program ceProgram;
    private Section math1_1;
    private Section phys1_1;
    private Section prog_1;
    private Section math2_1;
    private Section math2_2;
    private Section ap_1;
    private Section dm_1;
    private Student student;

    @Before
    public void setup() throws Exception {
        ce = new Major("8101", "CE", "Engineering");
        math1 = new Course("4444444", "MATH1", 3, "Undergraduate");
        phys1 = new Course("8888888", "PHYS1", 3, "Undergraduate");
        prog = new Course("7777777", "PROG", 4, "Undergraduate");
        math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1);
        ap = new Course("2222222", "AP", 3, "Undergraduate");
        dm = new Course("3333333", "DM", 3, "Undergraduate");
        ceProgram = new Program(ce, "Undergraduate", 140, 140, "Major");
        ceProgram.addCourse(math1, math2, phys1);
        ExamTime exam0 = new ExamTime("2021-07-10T09:00", "2021-07-10T11:00");
        ExamTime exam1 = new ExamTime("2021-07-11T09:00", "2021-07-11T11:00");
        ExamTime exam2 = new ExamTime("2021-07-12T09:00", "2021-07-12T11:00");
        ExamTime exam3 = new ExamTime("2021-07-13T09:00", "2021-07-13T11:00");
        ExamTime exam4 = new ExamTime("2021-07-14T09:00", "2021-07-14T11:00");
        ExamTime exam6 = new ExamTime("2021-07-16T09:00", "2021-07-16T11:00");
        ExamTime exam7 = new ExamTime("2021-07-16T10:00", "2021-07-16T12:00");
        math1_1 = new Section(math1, "01", exam0, Collections.emptySet());
        phys1_1 = new Section(phys1, "01", exam1, Collections.emptySet());
        prog_1 = new Section(prog, "01", exam4, Collections.emptySet());
        math2_1 = new Section(math2, "01", exam2, Collections.emptySet());
        math2_2 = new Section(math2, "02", exam3, Collections.emptySet());
        ap_1 = new Section(ap, "01", exam6, Collections.emptySet());
        dm_1 = new Section(dm, "01", exam7, Collections.emptySet());
        student = new Student("810196657", "Undergraduate");
        student.addProgram(ceProgram);
    }

    @Test
    public void withoutViolationTest() throws Exception {
        enrollmentList = new EnrollmentList("Hadi's list", student);
        enrollmentList.addSections(math1_1, phys1_1, prog_1, ap_1);
        assertTrue(enrollmentList.checkEnrollmentRules().size()==0);
    }

    @Test
    public void checkPrerequisiteTest() throws Exception {
        enrollmentList = new EnrollmentList("Hadi's list", student);
        enrollmentList.addSections(math1_1, phys1_1, math2_1, ap_1);
        assertTrue(enrollmentList.checkEnrollmentRules().get(0) instanceof PrerequisiteNotTaken);
    }

    @Test
    public void checkAlreadyPassedTest() throws Exception {
        student.setGrade("14011", math1, 16.5);
        enrollmentList = new EnrollmentList("Hadi's list", student);
        enrollmentList.addSections(math1_1, phys1_1, prog_1, ap_1);
        assertTrue(enrollmentList.checkEnrollmentRules().get(0) instanceof RequestedCourseAlreadyPassed);
    }

    @Test
    public void checkRequestedTwiceTest() throws Exception {
        enrollmentList = new EnrollmentList("Hadi's list", student);
        enrollmentList.addSections(math1_1, phys1_1, phys1_1, ap_1);
        assertTrue(enrollmentList.checkEnrollmentRules().get(0) instanceof CourseRequestedTwice);
    }

    @Test
    public void minCreditTest() throws Exception {
        enrollmentList = new EnrollmentList("Hadi's list", student);
        enrollmentList.addSections(math1_1, phys1_1, prog_1);
        assertTrue(enrollmentList.checkEnrollmentRules().get(0) instanceof MinCreditsRequiredNotMet);
    }

    // TODO

    @Test
    public void ExamTimeConflictTest() throws Exception {
        enrollmentList = new EnrollmentList("Hadi's list", student);
        enrollmentList.addSections(math1_1, phys1_1, ap_1, dm_1);
        assertTrue(enrollmentList.checkEnrollmentRules().get(0) instanceof ExamTimeCollision);
    }

    @Test
    public void sectionScheduleConflictTest() throws Exception {
        enrollmentList = new EnrollmentList("Hadi's list", student);
        PresentationSchedule schedule1 = new PresentationSchedule("Sunday", "9:00", "11:00");
        PresentationSchedule schedule2 = new PresentationSchedule("Monday", "9:00", "11:00");
        PresentationSchedule schedule3 = new PresentationSchedule("Monday", "10:00", "12:00");
        PresentationSchedule schedule4 = new PresentationSchedule("Wednesday", "10:00", "12:00");
        math1_1.setPresentationSchedule(new HashSet<>(Arrays.asList(schedule1, schedule2)));
        ap_1.setPresentationSchedule(new HashSet<>(Arrays.asList(schedule3, schedule4)));
        enrollmentList.addSections(math1_1, phys1_1, prog_1, ap_1);
        assertTrue(enrollmentList.checkEnrollmentRules().get(0) instanceof ConflictOfClassSchedule);
    }

    @Test
    public void manyViolationTest() throws ExceptionList {
        student.setGrade("14011", phys1, 16.5);
        enrollmentList = new EnrollmentList("Hadi's list", student);
        enrollmentList.addSections(math1_1, phys1_1, math2_1, ap_1); // has two violation: already pass & prerequisite
        assertTrue(enrollmentList.checkEnrollmentRules().size() == 2);
    }
}

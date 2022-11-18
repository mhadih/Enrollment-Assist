package ir.proprog.enrollassist.domain.enrollmentList;

import com.google.common.collect.Lists;
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
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

@RunWith (Parameterized.class)
public class EnrollmentListTest {

    private Set<Integer> errors = new HashSet<>();
    private EnrollmentList enrollmentList;

    private  ArrayList <Section> newSections;
    private boolean expected;
    private static Student student;


    private static Program ceProgram;
    private static Major ce;

    public static Course math1;
    public static Course phys1;
    public static Course phys2;
    public static Course prog;
    public static Course math2;
    public static Course ap;
    public static Course dm;
    public static Course akhlagh;
    public static Course english;
    public static Course Tarbiat;
    public static Course Adabiat;

    public static Section math1_1;

    public static Section phys1_1;
    public static Section phys2_1;
    public static Section prog_1;
    public static Section math2_1;
    public static Section math2_2;
    public static Section ap_1;
    public static Section dm_1;
    public static Section akhlagh_1;
    public static Section english_1 ;
    public static Section tarbiat_1 ;
    public static Section adabiat_1 ;




    public EnrollmentListTest(ArrayList<Section> newSections,ArrayList<String> errors) throws Exception {

        this.newSections = newSections;

        for(String error : errors){
            this.errors.add(error.hashCode());
        }

    }

    @Parameters
    public static Collection<Object []> parameters() throws Exception {

        math1 = new Course("4444444", "MATH1", 3, "Undergraduate");
        phys1 = new Course("8888888", "PHYS1", 3, "Undergraduate");
        phys2 = new Course("8888889", "PHYS2", 3, "Undergraduate");
        prog = new Course("7777777", "PROG", 4, "Undergraduate");
        math2 = new Course("6666666", "MATH2", 3, "Undergraduate").withPre(math1);
        ap = new Course("2222222", "AP", 3, "Undergraduate");
        dm = new Course("3333333", "DM", 3, "Undergraduate");
        english = new Course("1010101", "EN", 2, "Undergraduate");
        akhlagh = new Course("1111110", "AKHLAGH", 2, "Undergraduate");
        Tarbiat = new Course("1111010", "Tarbiat", 1, "Undergraduate");
        Adabiat = new Course("1011010", "Adabiat", 3, "Undergraduate");

        ExamTime exam0 = new ExamTime("2021-07-10T09:00", "2021-07-10T11:00");
        ExamTime exam1 = new ExamTime("2021-07-11T09:00", "2021-07-11T11:00");
        ExamTime exam2 = new ExamTime("2021-07-12T09:00", "2021-07-12T11:00");
        ExamTime exam3 = new ExamTime("2021-07-13T09:00", "2021-07-13T11:00");
        ExamTime exam4 = new ExamTime("2021-07-14T09:00", "2021-07-14T11:00");
        ExamTime exam5 = new ExamTime("2021-07-15T09:00", "2021-07-15T11:00");
        ExamTime exam6 = new ExamTime("2021-07-16T09:00", "2021-07-16T11:00");
        ExamTime exam7 = new ExamTime("2021-07-17T09:00", "2021-07-17T11:00");

        math1_1 = new Section(math1, "01", exam0, Collections.emptySet());
        phys1_1 = new Section(phys1, "01", exam1, Collections.emptySet());
        phys2_1 = new Section(phys1, "01", exam7, Collections.emptySet());
        prog_1 = new Section(prog, "01", exam4, Collections.emptySet());
        math2_1 = new Section(math2, "01", exam2, Collections.emptySet());
        math2_2 = new Section(math2, "02", exam3, Collections.emptySet());
        ap_1 = new Section(ap, "01", exam6, Collections.emptySet());
        dm_1 = new Section(dm, "01", exam7, Collections.emptySet());
        akhlagh_1 = new Section(akhlagh, "01" ,exam0, Collections.emptySet());
        english_1 = new Section(english, "01", exam1, Collections.emptySet());
        adabiat_1 = new Section(Adabiat, "01", exam0, Collections.emptySet());
        tarbiat_1 = new Section(Tarbiat, "01", exam5, Collections.emptySet());

        ce = new Major("8101", "CE", "Engineering");

        ceProgram = new Program(ce, "Undergraduate", 140, 140, "Major");
        ceProgram.addCourse(math1,phys1,prog,math2,ap,dm,akhlagh,english);



        student = new Student("810196657", "Undergraduate");
        student.addProgram(ceProgram);

        student.setGrade("11112", akhlagh, 11);

        PresentationSchedule schedule1 = new PresentationSchedule("Sunday", "9:00", "11:00");
        PresentationSchedule schedule2 = new PresentationSchedule("Monday", "9:00", "11:00");
        PresentationSchedule schedule3 = new PresentationSchedule("Monday", "10:00", "12:00");
        PresentationSchedule schedule4 = new PresentationSchedule("Wednesday", "10:00", "12:00");
        math1_1.setPresentationSchedule(new HashSet<>(Arrays.asList(schedule1, schedule2)));
        dm_1.setPresentationSchedule(new HashSet<>(Arrays.asList(schedule3, schedule4)));


        return Arrays.asList(new Object [][]{
                {Lists.newArrayList(math1_1, phys1_1, prog_1, ap_1),Lists.newArrayList()},
                {Lists.newArrayList(math2_1, phys1_1, prog_1,ap_1),Lists.newArrayList("PrerequisiteNotTaken")},
                {Lists.newArrayList(dm_1,english_1,ap_1,prog_1,akhlagh_1),Lists.newArrayList("RequestedCourseAlreadyPassed")},
                {Lists.newArrayList(math1_1, phys1_1, phys1_1, ap_1),Lists.newArrayList("CourseRequestedTwice")},
                {Lists.newArrayList(math1_1, phys1_1, ap_1,english_1,tarbiat_1),Lists.newArrayList("ExamTimeCollision")},
                {Lists.newArrayList(math1_1, phys1_1, prog_1),Lists.newArrayList("MinCreditsRequiredNotMet")},
                {Lists.newArrayList(dm_1, prog_1,ap_1,tarbiat_1,english_1,adabiat_1),Lists.newArrayList("MaxCreditsLimitExceeded")},
                {Lists.newArrayList(math1_1, phys1_1, prog_1,dm_1),Lists.newArrayList("ConflictOfClassSchedule")},
                {Lists.newArrayList(math2_1,math1_1, phys1_1, phys1_1),Lists.newArrayList("CourseRequestedTwice","PrerequisiteNotTaken")}

        });
    }

    @Test
    public void isCheck() throws ExceptionList {
        enrollmentList = new EnrollmentList("Hadi's list", student);
        for (Section section : this.newSections)
        {
            this.enrollmentList.addSection(section);
        }

        Set<Integer> EnrollmentRulesName = new HashSet<>();

        for(EnrollmentRuleViolation rule : this.enrollmentList.checkEnrollmentRules()){
            EnrollmentRulesName.add(rule.getName().hashCode());
        }

        assertTrue(EnrollmentRulesName.size() == this.errors.size() & this.errors.containsAll(EnrollmentRulesName));
    }
}

package ir.proprog.enrollassist.domain.enrollmentList;

import ir.proprog.enrollassist.Exception.ExceptionList;
import ir.proprog.enrollassist.domain.EnrollmentRules.EnrollmentRuleViolation;
import ir.proprog.enrollassist.domain.EnrollmentRules.ExamTimeCollision;
import ir.proprog.enrollassist.domain.course.Course;
import ir.proprog.enrollassist.domain.section.ExamTime;
import ir.proprog.enrollassist.domain.section.Section;
import ir.proprog.enrollassist.domain.student.Student;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnrollmentListTest {

    private EnrollmentList enrollmentList;
    private Student student;
    private Course math1,phys1;

    public EnrollmentListTest() {

    }

    @Before
    public void init() throws ExceptionList {
        MockitoAnnotations.initMocks(this);
        student = new Student("810196657", "Undergraduate");
        enrollmentList = new EnrollmentList("Hadi's list", student);
        math1 = new Course("4444444", "MATH1", 3, "Undergraduate");
        phys1 = new Course("8888888", "PHYS1", 3, "Undergraduate");
    }

    @Test
    public void checkExamTimeConflictsEmptySectionsTest() {
        List<EnrollmentRuleViolation> violations = enrollmentList.checkExamTimeConflicts();
        assertEquals(0, violations.size());
    }

    @Test
    public void checkExamTimeConflictsTestOneConflict() throws Exception {
        Section math1_1 = mock(Section.class);
        Section phys1_1 = mock(Section.class);
        when(math1_1.getExamTime()).thenReturn(new ExamTime("2021-07-10T09:00", "2021-07-10T11:00"));
        when(phys1_1.getExamTime()).thenReturn(new ExamTime("2021-07-10T10:30", "2021-07-10T12:00"));
        enrollmentList.addSection(math1_1);
        enrollmentList.addSection(phys1_1);
        List<EnrollmentRuleViolation> violations = enrollmentList.checkExamTimeConflicts();
        assertEquals(violations.size(), 1);
        assertTrue(violations.get(0) instanceof ExamTimeCollision);
    }

    @Test
    public void checkExamTimeConflictsTestNullExamTime() throws Exception {
        Section math1_1 = mock(Section.class);
        Section phys1_1 = mock(Section.class);
        Section ap_1 = mock(Section.class);
        when(math1_1.getExamTime()).thenReturn(new ExamTime("2021-07-10T09:00", "2021-07-10T11:00"));
        when(phys1_1.getExamTime()).thenReturn(new ExamTime("2021-07-10T10:30", "2021-07-10T12:00"));
        when(ap_1.getExamTime()).thenReturn(null);
        enrollmentList.addSection(math1_1);
        enrollmentList.addSection(phys1_1);
        enrollmentList.addSection(ap_1);
        List<EnrollmentRuleViolation> violations = enrollmentList.checkExamTimeConflicts();
        assertEquals(violations.size(), 1);
    }

    @Test
    public void checkExamTimeConflictsTestMultiConflicts() throws Exception {
        Section math1_1 = mock(Section.class);
        Section phys1_1 = mock(Section.class);
        Section ap_1 = mock(Section.class);
        when(math1_1.getExamTime()).thenReturn(new ExamTime("2021-07-10T09:00", "2021-07-10T12:00"));
        when(phys1_1.getExamTime()).thenReturn(new ExamTime("2021-07-10T10:00", "2021-07-10T12:00"));
        when(ap_1.getExamTime()).thenReturn(new ExamTime("2021-07-10T10:00", "2021-07-10T11:00"));
        enrollmentList.addSection(math1_1);
        enrollmentList.addSection(phys1_1);
        enrollmentList.addSection(ap_1);
        List<EnrollmentRuleViolation> violations = enrollmentList.checkExamTimeConflicts();
        assertEquals(violations.size(), 3);
    }

}

package ir.proprog.enrollassist.domain.section;

import ir.proprog.enrollassist.Exception.ExceptionList;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class PresentationScheduleTest {

    PresentationSchedule schedule1;
    PresentationSchedule schedule2;

    public PresentationScheduleTest() {

    }

    @Test
    public void withoutConflictTest() throws ExceptionList {
        this.schedule1 = new PresentationSchedule("Sunday", "9:00", "10:30");
        this.schedule2 = new PresentationSchedule("Sunday", "12:00", "14:00");
        assertFalse(schedule1.hasConflict(schedule2));
    }

    @Test
    public void withoutConflictDifferentDayTest() throws ExceptionList {
        this.schedule1 = new PresentationSchedule("Sunday", "9:00", "10:30");
        this.schedule2 = new PresentationSchedule("Monday", "10:00", "12:00");
        assertFalse(schedule1.hasConflict(schedule2));
    }

    @Test
    public void withoutConflictBackToBackTest() throws ExceptionList {
        this.schedule1 = new PresentationSchedule("Sunday", "9:00", "10:30");
        this.schedule2 = new PresentationSchedule("Sunday", "10:30", "12:00");
        assertFalse(schedule1.hasConflict(schedule2));
    }

    @Test
    public void hasConflictWithCorrectOrderTest() throws ExceptionList {
        this.schedule1 = new PresentationSchedule("Sunday", "9:00", "11:00");
        this.schedule2 = new PresentationSchedule("Sunday", "10:00", "12:00");
        assertTrue(schedule1.hasConflict(schedule2));
    }

    @Test
    public void hasConflictWithInverseOrderTest() throws ExceptionList {
        this.schedule1 = new PresentationSchedule("Sunday", "9:00", "11:00");
        this.schedule2 = new PresentationSchedule("Sunday", "8:00", "10:00");
        assertTrue(schedule1.hasConflict(schedule2));
    }

    @Test
    public void hasConflictNestedTest() throws ExceptionList {
        this.schedule1 = new PresentationSchedule("Sunday", "15:00", "17:00");
        this.schedule2 = new PresentationSchedule("Sunday", "15:30", "16:30");
        assertTrue(schedule1.hasConflict(schedule2));
    }

    @Test
    public void hasConflictNestedInverseTest() throws ExceptionList {
        this.schedule1 = new PresentationSchedule("Sunday", "12:00", "13:00");
        this.schedule2 = new PresentationSchedule("Sunday", "11:30", "13:00");
        assertTrue(schedule1.hasConflict(schedule2));
    }

    @After
    public void tearDown() {
        schedule1 = null;
        schedule2 = null;
    }
}

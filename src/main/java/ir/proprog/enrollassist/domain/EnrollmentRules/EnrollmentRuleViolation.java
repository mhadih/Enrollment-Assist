package ir.proprog.enrollassist.domain.EnrollmentRules;

import java.util.Objects;

public abstract class EnrollmentRuleViolation {
    public String getName() {
        return this.getClass().getSimpleName();
    }
}



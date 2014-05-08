package org.umlg.quickpreview.associationclass;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.umlg.associationclass.Course;
import org.umlg.associationclass.Enrollment;
import org.umlg.associationclass.Semester;
import org.umlg.associationclass.Student;
import org.umlg.quickpreview.BaseTest;

/**
 * Date: 2014/05/08
 * Time: 10:25 PM
 */
public class TestAssociationClass extends BaseTest {

    @Test
    public void testAssociationClass() {
        Student john = new Student();
        Course math = new Course();
        Enrollment enrollmentMath = new Enrollment();
        enrollmentMath.setJoined(new LocalDate());
        enrollmentMath.setSemester(Semester.FIRST);
        john.addToCourse(math, enrollmentMath);

        Course english = new Course();
        Enrollment enrollmentEnglish = new Enrollment();
        enrollmentEnglish.setJoined(new LocalDate());
        enrollmentEnglish.setSemester(Semester.SECOND);
        john.addToCourse(english, enrollmentEnglish);
        db.commit();

        Assert.assertEquals(2, john.getEnrollment().size());
        Assert.assertTrue(john.getEnrollment().contains(enrollmentMath));
        Assert.assertTrue(john.getEnrollment().contains(enrollmentEnglish));

        //UMLG generates convenience methods to get the association class given the other end of the association
        Assert.assertEquals(enrollmentMath, john.getEnrollment_course(math));
        Assert.assertEquals(enrollmentEnglish, john.getEnrollment_course(english));

        //The association class is a class like any other. Its properties can be navigated to.
        Assert.assertEquals(math, enrollmentMath.getCourse());
        Assert.assertEquals(english, enrollmentEnglish.getCourse());
    }
}

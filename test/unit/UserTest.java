package unit;

import models.User;
import org.junit.Before;
import org.junit.Test;
import play.data.validation.Validation;
import play.test.Fixtures;
import play.test.UnitTest;

public class UserTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteAllModels();
    }

    @Test
    public void addUser() {
        long count = User.count();
        assertEquals("should be no users", 0, count);
        User u = new User();
        u.username = "name";
        u.email = "test@test.com";
        u.salt = "salt";
        u.password = "9439713489 fhhf njv";
        assertTrue("user should save", u.validateAndCreate());
        assertEquals("should be 1 user now", 1, User.count());
    }

    @Test
    public void noUsernamePw() {
        User u = new User();
        u.username = "name";
        u.email = "test@test.com";
        assertFalse("user should not save", u.validateAndCreate());
        assertTrue("should fail for password", Validation.hasError(".password"));
        assertTrue("should fail for salt", Validation.hasError(".salt"));
    }

    @Test
    public void badUsername() {
        User u = new User();
        u.username = "n";
        assertFalse("user should not save", u.validateAndCreate());
        assertTrue("username too short", Validation.hasError(".username"));
        Validation.clear();
        u.username = "nnnnnnnnnnnnnnnnnnnnnnnjjjjjjjjjjjjjjaaaaaaaaaaaaaa"
                + "aaaaaaeeeeeeeeeeeeeeeeeeeeeehhhhhhhhhhhhhhhhhhhrrrrrrrrrrrrr"
                + "rrrrrrraaaaaaaaaaaaaaaaaas8947762378dfi78gh780ydf78gad";
        assertFalse("user should not save", u.validateAndCreate());
        assertTrue("username too long", Validation.hasError(".username"));
        Validation.clear();
        u.username = "n&*hhh";
        assertFalse("user should not save", u.validateAndCreate());
        assertTrue("username contains invalid chars", Validation.hasError(".username"));
        Validation.clear();
        u.username = "good.name";
        assertFalse("user should not save", u.validateAndCreate());
        assertFalse("username is good", Validation.hasError(".username"));
    }

    @Test
    public void badEmail() {
        User u = new User();
        u.email = "email";
        assertFalse("user should not save", u.validateAndCreate());
        assertTrue("email bad format", Validation.hasError(".email"));
    }
}

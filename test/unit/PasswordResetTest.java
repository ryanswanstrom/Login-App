package unit;

import java.util.Date;
import models.BaseModel.Valid;
import models.PasswordReset;
import models.User;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;

public class PasswordResetTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteAllModels();
        User u = new User("testuser", "test@testuser.com", "somehashvalue", "pepper");
        u.validateAndCreate();
    }

    @Test
    public void addPasswordReset() {
        assertEquals("should be no Password Resets", 0, PasswordReset.count());
        PasswordReset reset = new PasswordReset((User)User.all().first()).save();
        assertEquals("should be one Password Resets", 1, PasswordReset.count());
        assertNotNull(reset.uuid);
        assertNotNull(reset.user);
        assertNotNull(reset.expires);
        assertNotNull(reset.valid);

        assertTrue(DateUtils.addDays(new Date(), 2).after(reset.expires));
    }

    @Test
    public void invalidateTest() {
        PasswordReset reset = new PasswordReset((User)User.all().first()).save();
        assertTrue(Valid.Y.equals(reset.valid));
        reset.invalidate();
        assertTrue(Valid.N.equals(reset.valid));

    }

}

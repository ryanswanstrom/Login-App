
import org.junit.Test;
import play.mvc.Http.Response;
import play.test.FunctionalTest;


public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    @Test
    public void testThatPrivacyPageWorks() {
        Response response = GET("/privacy");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
        assertContentMatch("Privacy Policy", response);
    }

    @Test
    public void testThatTermsOfServicePageWorks() {
        Response response = GET("/termsofservice");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
        assertContentMatch("Terms of Service", response);
    }

    @Test
    public void testThatForgotPasswordPageWorks() {
        Response response = GET("/forgot/password");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
        assertContentMatch("Username", response);
    }

    @Test
    public void testThatForgotUsernamePageWorks() {
        Response response = GET("/forgot/username");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
        assertContentMatch("Email", response);
    }

    @Test
    public void testThatLoginPageWorks() {
        Response response = GET("/login");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
        assertContentMatch("login", response);
        assertContentMatch("Username", response);
        assertContentMatch("Password", response);
    }

}
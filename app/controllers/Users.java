package controllers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import java.util.List;
import models.BaseModel.Valid;
import models.PasswordReset;
import models.User;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.Logger;
import play.Play;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Codec;
import play.libs.Mail;
import play.mvc.Controller;
import play.mvc.Http.Request;

public class Users extends Controller {

    private static final String FROM;
    /**
     * The number of times to hash a password. Why 1153? it is a prime number
     */
    private static final int HASH_ITERS = 1153;

    static {
        FROM = Play.configuration.getProperty("mail.from");
    }

    public static void register() {
        User user = new User();
        render(user);
    }

    public static void add(User user, @Required @MinSize(4) String password,
            @Required @Equals("password") String pwConfirm) throws Throwable {
        if (User.count("byUsername", user.username) > 0) {
            Validation.addError("user.username", "Opps, that username is already taken");
        }
        if (User.count("byEmail", user.email) > 0) {
            Validation.addError("user.email", "Opps, that email already exists");
        }
        if (Validation.hasErrors()) {
            Logger.error("validation failed during user registration");
            Validation.keep();
            params.flash();
            register();
        }
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // Salt generation 64 bits long
            byte[] bSalt = new byte[8];
            random.nextBytes(bSalt);

            user.lastLogin = new Date();
            user.hashIters = HASH_ITERS;
            user.password = Codec.encodeBASE64(Security.getHash(user.hashIters, password, bSalt));
            user.salt = Codec.encodeBASE64(bSalt);
        } catch (NoSuchAlgorithmException e) {
            Logger.error(e, "bad SecureRandom instance SHA1PRNG");
            Validation.addError("password", "The password cannot be hashed.");
            Application.index();
        } catch (UnsupportedEncodingException e) {
            Logger.error(e, "bad encoding UTF-8");
            Validation.addError("password", "The password cannot be encoded properly.");
            Application.index();
        }
        user.username = StringUtils.lowerCase(user.username);
        if (!user.validateAndCreate()) {
            Logger.error("validation failed: %s", user);
            Validation.keep();
            params.flash();
            register();
        }
        // send verificationEmail
        SimpleEmail msg = new SimpleEmail();
        msg.setFrom(FROM);
        msg.addTo(user.email);
        msg.setSubject("email verification");
        msg.setMsg("Please click (or copy and paste into a browser) the link to verify your email. "
                + Request.current().host + "/verify/email/" + user.uuid);
        Mail.send(msg);
        // add to flash
        flash.success("Please click the verification link in your email.");
        Secure.login();
    }

    public static void activate(String uuid) throws Throwable {
        // look up uuid
        User user = User.find("byUuidAndValid", uuid, Valid.Y).first();
        notFoundIfNull(user);
        // activate user
        user.activate();
        flash.success("email verified correctly, please login");
        flash.put("username", user.username);
        Secure.login();
    }

    /**
     * this will display the public page for any username
     *
     * @param username
     */
    public static void show(String username) {
        User user = User.findByUsername(username);
        notFoundIfNull(user);
        render(user);
    }

    public static void forgotUsername() {
        render();
    }

    public static void sendUsername(@Required @Email String email) throws Throwable {
        if (Validation.hasErrors()) {
            params.flash();
            Validation.keep();
            forgotUsername();
        }
        User user = User.find("byEmailAndValidAndActive", email, Valid.Y, Valid.Y).first();
        if (null == user) {
            flash.error("Email not found");
            forgotUsername();
        }
        SimpleEmail msg = new SimpleEmail();
        msg.setFrom(FROM);
        msg.addTo(user.email);
        msg.setSubject("forgot username");
        msg.setMsg("Your username is: " + user.username);
        Mail.send(msg);
        // add to flash
        flash.success("Please check your email for your username");
        Secure.login();

    }

    public static void forgotPassword() {
        render();
    }

    public static void sendPasswordReset(@Required String username) throws EmailException {
        User user = User.findByUsername(username);
        if (null == user) {
            flash.error("Username not found");
            forgotPassword();
        }
        PasswordReset pwReset = new PasswordReset(user);
        if (!pwReset.validateAndSave()) {
            Logger.error("User cannot save: %s", username);
            Validation.addError("username", "Unknown Problem");
            Validation.keep();
            params.flash();
            forgotPassword();
        }
        // send an email

        SimpleEmail msg = new SimpleEmail();
        msg.setFrom(FROM);
        msg.addTo(user.email);
        msg.setSubject("reset Password");
        msg.setMsg("Please click (or copy and paste into a browser) the link to reset your password. "
                + " Please hurry because the link is only active for 24 hours. " + Request.current().host
                + "/reset/password/" + pwReset.uuid);
        Mail.send(msg);
        // forward to login page with message "Check your email for instructions to reset password."
        flash.success("Check your email for instructions to reset your password.");
        forgotPassword();
    }

    public static void resetPasswordShow(String uuid) {
        PasswordReset pwReset = PasswordReset.find("byUuidAndValid", uuid, Valid.Y).first();
        notFoundIfNull(pwReset);
        User user = pwReset.user;
        Logger.info("User that needs password reset is %s", user);
        Date now = new Date();
        if (pwReset.expires.before(now)) {
            notFound("Password Reset Page has expired");
        }
        render(user);
    }

    public static void resetPassword(@Required String username, @Required @MinSize(4) String password,
            @Required @Equals("password") String pwConfirm) throws Throwable {
        //TODO: check validation
        User user = User.findByUsername(username);
        if (null == user) {
            flash.error("username not found");
            forgotPassword();
        }

        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // Salt generation 64 bits long
            byte[] bSalt = new byte[8];
            random.nextBytes(bSalt);
            user.password = Codec.encodeBASE64(Security.getHash(user.hashIters, password, bSalt));
            user.salt = Codec.encodeBASE64(bSalt);
        } catch (NoSuchAlgorithmException e) {
            Logger.error(e, "bad SecureRandom instance SHA1PRNG");
            Validation.addError("password", "The password cannot be hashed.");
            Application.index();
        } catch (UnsupportedEncodingException e) {
            Logger.error(e, "bad encoding UTF-8");
            Validation.addError("password", "The password cannot be encoded properly.");
            Application.index();
        }
        if (!user.validateAndSave()) {
            Logger.error("validation failed during password reset: %s", user);
            Validation.keep();
            params.flash();
            resetPasswordShow(user.getId().toString());
        }
        // disable all reset password links for this user
        List<PasswordReset> pwresets = PasswordReset.find("byUserAndValid", user, Valid.Y).fetch();
        for (PasswordReset pr : pwresets) {
            pr.valid = Valid.N;
            pr.save();
        }
        flash.success("password reset, please login");
        flash.put("username", user.username);
        Secure.login();

    }

}

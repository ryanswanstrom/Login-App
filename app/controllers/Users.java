package controllers;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

import models.User;
import play.Logger;
import play.data.validation.Email;
import play.data.validation.Equals;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.data.validation.Validation;
import play.libs.Codec;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class Users extends Controller {
		
	public static void show() {
		User user = User.find("byUsername", session.get("username")).first();
		notFoundIfNull(user);
		render(user);
	}
	
	public static void edit() {
		render();
	}
	
	public static void update(){
		show();
	}

}

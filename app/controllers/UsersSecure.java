package controllers;


import models.User;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class UsersSecure extends Controller {
		
	public static void dashboard() {
		User user = User.find("byUsername", session.get("username")).first();
		notFoundIfNull(user);
		render(user);
	}
	
	public static void edit() {
		render();
	}
	
	public static void update(){
		dashboard();
	}

}

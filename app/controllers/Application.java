package controllers;

import java.io.File;
import java.util.List;

import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.Request;


public class Application extends Controller {

    public static void index() {
    	List<User> users = User.findAll();
        render(users);
    }
    
    /**
     * action to open the robots.txt file.
     */
    public static void robots() {
        File file = play.Play.getFile("public/robots.txt");
        response.cacheFor("24h");
        renderBinary(file);
    }
  
    /**
     * action to open the sitemap.xml file
     */
    public static void sitemap() {
        File file = play.Play.getFile("public/sitemap.xml");
        response.cacheFor("24h");
        renderBinary(file);
    }
    

}
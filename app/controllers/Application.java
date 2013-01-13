package controllers;

import java.io.File;
import java.util.List;

import models.BaseModel.Valid;
import models.User;
import play.mvc.Controller;


public class Application extends Controller {

    public static void index() {
    	List<User> users = User.find("byValidAndActive", Valid.Y, Valid.Y).fetch(10);
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
     * action to open the robots.txt file.
     */
    public static void humans() {
        File file = play.Play.getFile("public/humans.txt");
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

    public static void privacy() {
        render();
    }

    public static void terms() {
        render();
    }


}
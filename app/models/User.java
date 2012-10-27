package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import play.data.validation.Email;
import play.data.validation.Match;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;

@Entity
public class User extends BaseModel {

    @Required
    @Match(value = "[a-zA-Z0-9\\.]+", message = "validation.username")
    @Column(unique = true)
    @MinSize(2)
    @MaxSize(100)
    public String username;
    @Email
    @Required
    @Column(unique = true)
    public String email;
    @Required
    public String password;    // this is actually the hashed password
    @Required
    public String salt;
    public Integer hashIters;
    public Date lastLogin;
    public Valid active;
    public Date created;
    public String uuid;

    public User() {
        this.active = Valid.N;
        this.uuid = UUID.randomUUID().toString();
        this.created = new Date();
    }

    public void activate() {
        this.active = Valid.Y;
        this.uuid = null;
        this.save();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static <T extends User> T findByUsername(String username) {
        return User.find("byUsernameAndValidAndActive", username, Valid.Y, Valid.Y).first();

    }
}

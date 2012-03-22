package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Mail;
import play.mvc.Http.Request;

@Entity
public class User extends BaseModel {
	@Required
	@Column(unique=true)
	public String username;
	@Email
	@Required
	@Column(unique=true)
	public String email;
	@Required
	public String password;
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
		this.save();
	}

	public String toString() {
		return this.username;
	}
}

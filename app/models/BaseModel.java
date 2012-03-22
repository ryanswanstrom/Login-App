package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class BaseModel extends Model {
	public enum Valid{Y, N}
	public Valid valid;
	
	public BaseModel() {
		this.valid = Valid.Y;
	}
}

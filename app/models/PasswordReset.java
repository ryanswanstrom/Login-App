package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Required;

@Entity
public class PasswordReset extends BaseModel {

    @Required
    public String uuid;
    @Required
    public Date expires;
    @Required
    @ManyToOne
    public User user;
    
    public PasswordReset(User user) {
        this.user = user;
        // invalidate all previous password resets
        List<PasswordReset> older = PasswordReset.find("byUserAndValid", user, Valid.Y).fetch();
        for (PasswordReset old : older) {
            old.invalidate();
        }
        String tmpUuid = UUID.randomUUID().toString();
        if (PasswordReset.count("byUuid", tmpUuid) > 0) {
            // concatenating another UUID should avoid collisions
            tmpUuid += UUID.randomUUID().toString();
        }
        this.uuid = tmpUuid;
        Date now = new Date();  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(now);  
        cal.add(Calendar.DAY_OF_YEAR, 1);   
        Date tomorrow = cal.getTime();
        this.expires = tomorrow;
    }

}

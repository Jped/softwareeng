package edu.cooper.ee.ece366.events;

import edu.cooper.ee.ece366.events.model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Service {
    private EvantStore es;
    public Service(EvantStore es) {
        this.es = es;
    };

    public User createUser(Boolean userType, String userName, String userPassword, String userPhone, String userEmail, LocalDateTime userBirthday, Boolean userGender) {
        User user;
        Boolean isOrg = userType;
        if (isOrg) {
            Organization new_org = new Organization(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail);
            user = es.createOrg(new_org);
        } else {
            Member new_member = new Member(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail, userBirthday, userGender);
            user = es.createMember(new_member);
        }
        return user;
    }
    public User getUser(String userEmail, Boolean userType) {
        if(!userType){
            return es.getMember(userEmail);
        }
        return es.getOrg(userEmail);
    }

    public Event createEvent(String eventName, String orgName, LocalDateTime eventDate, String eventMessage) {
        Event e = new Event(eventName, orgName, eventDate, eventMessage);
        return es.createEvent(e);
    }

    public void joinEvent(String eventName, String orgName, String userEmail) {
        Member m = es.getMember(userEmail);
        Event e = es.getEvent(eventName, orgName);
        es.joinEvent(m, e);
        return;
    }

    public Boolean verifyPassword(User u, String password){
        return BCrypt.checkpw(password, u.getPassword());
    }

}

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
            user = es.createOrg(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail);
        } else {
            user = es.createMember(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail, userBirthday, userGender);
        }
        return user;
    }
    public User getUser(String userEmail, Boolean userType) {
        if(userType){
            return es.getMember(userEmail);
        }
        return es.getOrg(userEmail);
    }

    public Boolean verifyPassword(User u, String password){
        return BCrypt.checkpw(password, u.getPassword());
    }

}

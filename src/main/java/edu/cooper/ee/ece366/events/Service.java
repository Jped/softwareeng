package edu.cooper.ee.ece366.events;

import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.Member;
import edu.cooper.ee.ece366.events.model.Organization;
import edu.cooper.ee.ece366.events.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Service {
    public Service() {
    }

    ;

    public User createUser(Boolean userType, String userName, String userPassword, String userPhone, String userEmail, LocalDateTime userBirthday, Boolean userGender) {
        User user;
        Boolean isOrg = userType;
        if (isOrg) {
            user = new Organization(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail);
        } else {
            user = new Member(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail, userBirthday, userGender);
        }
        return user;
    }

    public Event createEvent(String eventName, String orgName, LocalDateTime eventDate, String eventMessage){
        Event e = new Event(eventName, orgName, eventDate, eventMessage);
        return e;
    }

    public Boolean verifyPassword(User u, String password){
        return BCrypt.checkpw(password, u.getPassword());
    }

    public ArrayList<Event> getMyEvents(HashMap<Event, User> eventUserHashMap, String email){
        ArrayList<Event> myEvents = new ArrayList<Event>();
        eventUserHashMap.forEach((event, user) -> {
            if (((user.getEmail())).equals(email)) {
                myEvents.add(event);
            }
        });
        return myEvents;
    }

    public  ArrayList<Event> getUpcomingEvents(HashMap<String, Event> eventSet) {
        ArrayList<Event> upcomingEvents = new ArrayList<Event>();
        eventSet.forEach((name, event) -> {
            if(event.getDate().isAfter(LocalDateTime.now())) {
                upcomingEvents.add(event);
            }
        });
        return upcomingEvents;
    }

}

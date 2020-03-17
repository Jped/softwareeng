package edu.cooper.ee.ece366.events.model;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class EvantStoreBasicImpl implements  EvantStore {
    private HashMap<String, User> userSet;
    private HashMap<String, Event> eventSet;
    private HashMap<Event, User> eventUserHashMap;

    public EvantStoreBasicImpl() {
        this.userSet = new HashMap<String, User>();
        this.eventSet =  new HashMap<String, Event>();
        this.eventUserHashMap = new HashMap<Event, User>();
    }
    Member createMember(String userName, String userPassword, String userPhone, String userEmail, LocalDateTime userBirthday, Boolean userGender){
        Member m = new Member(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail, userBirthday, userGender);
        userSet.put(userEmail, m);
        return m;
    }
    Member getMember(String memberEmail);

    Organization createOrg(String userName, String userPassword, String userPhone, String userEmail){
        Organization o = new Organization(userName, BCrypt.hashpw(userPassword, BCrypt.gensalt()), userPhone, userEmail);
        userSet.put(userEmail, o);
        return o;
    }
    Organization getOrg(String orgEmail);

    Boolean checkUser(String userEmail);
    Boolean checkOrg(String orgEmail);
    Boolean checkEvent(String eventName);


    Event getEvent(String eventName);

    Event createEvent(String eventName, String orgName, LocalDateTime eventDate, String eventMessage){
        Event e = new Event(eventName, orgName, eventDate, eventMessage);
        eventSet.put(eventName, e);
        return e;
    }

    Event joinEvent(String eventName, String userEmail);

    ArrayList<Event> getMyEvents(String email){
        ArrayList<Event> myEvents = new ArrayList<Event>();
        eventUserHashMap.forEach((event, user) -> {
            if (((user.getEmail())).equals(email)) {
                myEvents.add(event);
            }
        });
        return myEvents;
    }
    ArrayList<Event> getUpcomingEvents(){
        ArrayList<Event> upcomingEvents = new ArrayList<Event>();
        eventSet.forEach((name, event) -> {
            if(event.getDate().isAfter(LocalDateTime.now())) {
                upcomingEvents.add(event);
            }
        });
        return upcomingEvents;
    }
}

package edu.cooper.ee.ece366.events.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface EvantStore {


    Member createMember(String userName, String userPassword, String userPhone, String userEmail, LocalDateTime userBirthday, Boolean userGender);
    Member getMember(String memberEmail);

    Organization createOrg(String userName, String userPassword, String userPhone, String userEmail);
    Organization getOrg(String orgEmail);

    Boolean checkUser(String userEmail);
    Boolean checkOrg(String orgEmail);
    Boolean checkEvent(String eventName);


    Event getEvent(String eventName, String orgName);
    Event createEvent(String eventName, String orgName, LocalDateTime eventDate, String eventMessage);
    Event joinEvent(String eventName, String orgName, String memberEmail);

    ArrayList<Event> getMyEvents(String email);
    ArrayList<Event> getUpcomingEvents();
}

package edu.cooper.ee.ece366.events.model;

import org.jdbi.v3.core.result.ResultIterator;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface EvantStore {


    Member createMember(String userName, String userPassword, String userPhone, String userEmail, LocalDateTime userBirthday, Boolean userGender);
    Member getMember(String memberEmail);

    Organization createOrg(String userName, String userPassword, String userPhone, String userEmail);
    Organization getOrg(String orgEmail);

    Boolean checkMember(String userEmail);
    Boolean checkOrg(String orgEmail);
    Boolean checkEvent(String eventName, String orgName);


    Event getEvent(String eventName, String orgName);
    Event createEvent(String eventName, String orgName, LocalDateTime eventDate, String eventMessage);
    Event joinEvent(String eventName, String orgName, String memberEmail);

    ResultIterator<Event> getMyEvents(String email);
    ResultIterator<Event> getUpcomingEvents();
}

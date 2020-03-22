package edu.cooper.ee.ece366.events.model;

import org.jdbi.v3.core.result.ResultIterator;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface EvantStore {


    Member createMember(Member member);
    Member getMember(String memberEmail);

    Organization createOrg(Organization org);
    Organization getOrg(String orgEmail);

    Boolean checkMember(String memberEmail);
    Boolean checkOrg(String orgEmail);
    Boolean checkEvent(String eventName, String orgName);


    Event getEvent(String eventName, String orgName);
    Event createEvent(Event e);
    void joinEvent(Member m, Event e);

    ResultIterator<Event> getMyEvents(String email);
    ResultIterator<Event> getUpcomingEvents();
}

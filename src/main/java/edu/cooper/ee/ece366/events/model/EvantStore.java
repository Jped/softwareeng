package edu.cooper.ee.ece366.events.model;

import org.jdbi.v3.core.result.ResultIterator;

import java.util.List;

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
    void leaveEvent(Member m, Event e);

    List<Event> getMyEvents(String email);
    List<Event> getOrgEvents(String orgName);
    List<Event> getUpcomingEvents();

    List<User> getSignups(Event e);

}

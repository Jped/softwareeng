package edu.cooper.ee.ece366.events;

import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.Member;
import edu.cooper.ee.ece366.events.model.User;
import edu.cooper.ee.ece366.events.model.Organization;
import edu.cooper.ee.ece366.events.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import spark.Request;


// TODO ERROR: there are two types of things we need to check
//      (1) if all the relevant parameters have been sent
//      (2) if the parameters are wrong
//      with this information we need to update the res code

public class Handler {
    HashMap<String, User> userSet = new HashMap<String, User>();
    HashMap<String, Event> eventSet = new HashMap<String, Event>();
    HashMap<Event, User> eventUserHashMap = new HashMap<Event, User>();

    Service service = new Service();

    public Handler() {}

    public Optional<User> signUp(Request request) {
        if (userSet.containsKey(request.queryParams("userEmail"))) {
            //TODO: Do some error stuff
            return Optional.empty();
        } else {
            User  u = service.createUser(
                    getUserType(request),
                    request.queryParams("userName"),
                    request.queryParams("userPassword"),
                    request.queryParams("userPhone"),
                    request.queryParams("userEmail"),
                    getDate(request.queryParams("userBirthday")),
                    getUserGender(request));
            userSet.put(request.queryParams("userEmail"), u);
            return Optional.of(u);
        }
    }

    public Optional<User> logIn(Request request) {
        // TODO: discussion needs to be had with how we want to differentiate errors here
        if (!userSet.containsKey(request.queryParams("userEmail"))){
            return Optional.empty();
        }
        else{
            User u = userSet.get(request.queryParams("userEmail"));
            Boolean correctPass = service.verifyPassword(u, request.queryParams("userPassword"));
            if (correctPass){
                return Optional.of(u);
            }
            else{
                return Optional.empty();
            }
        }
    }

    public Optional<Event> createEvent(Request request) {
        Event event;
        // TODO: Eventually we want this to be more secure, ie we need an org token to create an event
        if (!userSet.containsKey(request.queryParams("orgEmail"))) {
            return Optional.empty();
        } else {
            event = service.createEvent(
                    request.queryParams("eventName"),
                    request.queryParams("orgName"),
                    getDate(request.queryParams("eventDate")),
                    request.queryParams("eventMessage"));
            eventSet.put(request.queryParams("eventName"), event);
            return Optional.of(event);
        }
    }

    public Boolean joinEvent(Request request) {
        // TODO: Check if the user is already in the event
        if (eventSet.containsKey(request.queryParams("eventName"))) {
            eventUserHashMap.put(eventSet.get(request.queryParams("eventName")), userSet.get(request.queryParams("userEmail")));
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Event> myEvents(Request request) {
        ArrayList<Event> myEvents = service.getMyEvents(eventUserHashMap, request.queryParams("userEmail"));
        return myEvents;
    }

    public ArrayList<Event> upcomingEvents(Request request) {
        ArrayList<Event> upcomingEvents = service.getUpcomingEvents(eventSet);
        return upcomingEvents;
    }

    private Boolean getUserType(Request request) {
        return Boolean.valueOf(request.queryParams("userType"));
    }

    private Boolean getUserGender(Request request) {
        return Boolean.valueOf(request.queryParams("userGender"));
    }

    private LocalDateTime getDate(String date) {
        // TODO: verify that the date is in kosher format provided by the user
        if(date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(date, formatter);
        }
        return LocalDateTime.now();
    }
}



package edu.cooper.ee.ece366.events;

import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.Member;
import edu.cooper.ee.ece366.events.model.User;
import edu.cooper.ee.ece366.events.model.Organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import spark.Request;

public class Handler {

    public Handler() {
    }

    public User createUser(Request request) {
        User user;
        Boolean isOrg = getUserType(request);
        if (isOrg) {
            user = new Organization(request.queryParams("userName"), request.queryParams("userPassword"),request.queryParams("userPhone"), request.queryParams("userEmail"));
        }
        else {
            user = new Member(request.queryParams("userName"), request.queryParams("userPassword"), request.queryParams("userPhone"), request.queryParams("userEmail"), request.queryParams("userBirthday"), request.queryParams("userGender"));
        }
        return user;
    }

    public Optional<User> logIn(Request request, HashMap<String, User> userSet){
        User u = userSet.get(request.queryParams("userEmail"));

        if (userSet.containsKey(request.queryParams("userEmail"))){
            if (request.queryParams(("userPassword")).equals(u.getPassword())) {
                System.out.println("log in successful ");
                Optional<User> u_optional = Optional.of(u);
                return u_optional;
            }
            else{
                System.out.println("password incorrect");
                return Optional.empty();
            }

        }
        else{
            System.out.println("User not found");
            return Optional.empty();
        }


        /*


        Optional<User> u_optional;
        if (u != null) {
            u_optional = Optional.of(u);
        } else {
            return Optional.empty();
        }
        if (u.getPassword().equals(request.queryParams("userPassword"))) {
            return u_optional;
        }
        return Optional.empty();
*/
//        u.ifPresent(user -> {System.out.println("User's name = " + user.getName());
//        });
//        request.queryParams(("userPassword")).equals(u.getPassword())
//        u.ifPresent(user -> {System.out.println("User's name = " + user.getName());
//        });
//
//        if (userSet.containsKey(request.queryParams("userEmail"))){
//            if (request.queryParams(("userPassword")).equals(u.getPassword())) {
//                System.out.println("log in successful");
//                return u;
//            }
//            else{
//                System.out.println("password incorrect");
//                return u;
//            }
//
//        }
//        else{
//            System.out.println("User not found");
//            return u;
//        }

    }

    public Event createEvent(Request request, HashMap<String, User> userSet, HashMap<String, Event> eventSet){
        Event event;
        if (userSet.containsKey(request.queryParams("orgEmail"))) {
            event = new Event(request.queryParams("eventName"), request.queryParams("orgName"), request.queryParams("eventDate"), request.queryParams("eventMessage"));
            eventSet.put(request.queryParams("eventName"), event);
        }
        else {
            System.out.println("Organization not found");
            event = null;
        }
        return event;
    }

    public Boolean joinEvent(Request request, HashMap<String, Event> eventSet, HashMap<String, User> userSet, HashMap<Event, User> eventUserHashMap){
        if(eventSet.containsKey(request.queryParams("eventName"))) {
            eventUserHashMap.put(eventSet.get(request.queryParams("eventName")), userSet.get(request.queryParams("userEmail")));
            return true;
        }
        else {
            System.out.println("Event not found");
            return false;
        }
    }

    public ArrayList<Event> myEvents(Request request, HashMap<Event, User> eventUserHashMap){
        ArrayList<Event> myEvents = new ArrayList<Event>();
        eventUserHashMap.forEach((event, user)-> {
            if (((user.getEmail())).equals(request.queryParams("userEmail"))){
                myEvents.add(event);
            }
        });

        return myEvents;
    }

    public ArrayList<Event> upcomingEvents(Request request, HashMap<String, Event> eventSet){
        ArrayList<Event> upcomingEvents = new ArrayList<Event>();
        eventSet.forEach((name, event)->{
            //Check if date has passed
            upcomingEvents.add(event);
        });
        return upcomingEvents;
    }
    private Boolean getUserType(Request request){
        return Boolean.valueOf(request.queryParams("userType"));
    }
}



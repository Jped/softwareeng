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

public class Handler {
    HashMap<String, User> userSet = new HashMap<String, User>();
    HashMap<String, Event> eventSet = new HashMap<String, Event>();
    HashMap<Event, User> eventUserMap = new HashMap<Event, User>();

    Service service = new Service();

    public Handler() {
    }

    public User signUp(Request request) {
        if (userSet.containsKey(request.queryParams("userEmail"))) {
            //TODO: Do some error stuff ( maybe return an optional and if optional is empty res is an error)
        } else {
            service.createUser(
                    getUserType(request),
                    request.queryParams("userName"),
                    request.queryParams("userPassword"),
                    request.queryParams("userPhone"),
                    request.queryParams("userEmail"),
                    getUserDate(request),
                    getUserGender(request));
        }
    }

    public Optional<User> logIn(Request request, HashMap<String, User> userSet) {
        User u = userSet.get(request.queryParams("userEmail"));

        if (userSet.containsKey(request.queryParams("userEmail"))) {
            if (request.queryParams(("userPassword")).equals(u.getPassword())) {
                System.out.println("log in successful ");
                Optional<User> u_optional = Optional.of(u);
                return u_optional;
            } else {
                System.out.println("password incorrect");
                return Optional.empty();
            }

        } else {
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

    public Event createEvent(Request request, HashMap<String, User> userSet, HashMap<String, Event> eventSet) {
        Event event;
        if (userSet.containsKey(request.queryParams("orgEmail"))) {
            event = new Event(request.queryParams("eventName"), request.queryParams("orgName"), request.queryParams("eventDate"), request.queryParams("eventMessage"));
            eventSet.put(request.queryParams("eventName"), event);
        } else {
            System.out.println("Organization not found");
            event = null;
        }
        return event;
    }

    public Boolean joinEvent(Request request, HashMap<String, Event> eventSet, HashMap<String, User> userSet, HashMap<Event, User> eventUserHashMap) {
        if (eventSet.containsKey(request.queryParams("eventName"))) {
            eventUserHashMap.put(eventSet.get(request.queryParams("eventName")), userSet.get(request.queryParams("userEmail")));
            return true;
        } else {
            System.out.println("Event not found");
            return false;
        }
    }

    public ArrayList<Event> myEvents(Request request, HashMap<Event, User> eventUserHashMap) {
        ArrayList<Event> myEvents = new ArrayList<Event>();
        eventUserHashMap.forEach((event, user) -> {
            if (((user.getEmail())).equals(request.queryParams("userEmail"))) {
                myEvents.add(event);
            }
        });

        return myEvents;
    }

    public ArrayList<Event> upcomingEvents(Request request, HashMap<String, Event> eventSet) {
        ArrayList<Event> upcomingEvents = new ArrayList<Event>();
        eventSet.forEach((name, event) -> {
            //Check if date has passed
            upcomingEvents.add(event);
        });
        return upcomingEvents;
    }

    private Boolean getUserType(Request request) {
        return Boolean.valueOf(request.queryParams("userType"));
    }

    private Boolean getUserGender(Request request) {
        return Boolean.valueOf(request.queryParams("userGender"));
    }

    private LocalDateTime getUserDate(Request request) {
        if(request.queryParams("userBirthday") == null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(request.queryParams("userBirthday"), formatter);
        }
        return LocalDateTime.now();
    }
}



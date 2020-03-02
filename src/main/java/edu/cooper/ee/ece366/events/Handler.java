package edu.cooper.ee.ece366.events;

import com.google.gson.JsonObject;
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
import java.util.regex.Pattern;

import edu.cooper.ee.ece366.events.util.Validate;
import org.eclipse.jetty.http.HttpParser;
import spark.Request;
import spark.Response;


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

    public static void UpdateResponse(Response response, Integer code, String message) {
        response.status(code);
        response.body(message);
        System.out.println(message);
    }

    public Optional<User> signUp(Request request, Response response) {
        if (Validate.signUp(request,response,userSet)) {  //Passed in userSet only because of in memory configuration. This should be changed when db integrated.
            User  u = service.createUser(
                    getUserType(request),
                    request.queryParams("userName"),
                    request.queryParams("userPassword"),
                    request.queryParams("userPhone"),
                    request.queryParams("userEmail"),
                    getDate(request.queryParams("userBirthday")),
                    getUserGender(request));
            userSet.put(request.queryParams("userEmail"), u);

            UpdateResponse(response,200,String.valueOf(u));

            return Optional.of(u);
        }
        else {  //User could not be signed up. Response code and message were returned with more details.
            return Optional.empty();
        }
    }


    public Optional<User> logIn(Request request, Response response) {
        // TODO: discussion needs to be had with how we want to differentiate errors here
        String userEmail = request.queryParams("userEmail");
        String userPassword = request.queryParams("userPassword");

        // Check if parameters to logIn are provided: email and password. This avoids an unnecessary db query.
        if (userEmail == null || userPassword == null) {
            Handler.UpdateResponse(response, 400, "Missing field");
            return Optional.empty();
        }
        // Ensure userEmail is already signedUp
        if (!userSet.containsKey(userEmail)){
            Handler.UpdateResponse(response, 404, "This userEmail is not registered.");
            return Optional.empty();
        }
        else{
            User u = userSet.get(userEmail);
            Boolean correctPass = service.verifyPassword(u, userPassword);
            if (correctPass){
                UpdateResponse(response,200,"Log-in successful");
                return Optional.of(u);
            }
            else{
                UpdateResponse(response,404,"Log-in failed");
                return Optional.empty();
            }
        }
    }

    public Optional<Event> createEvent(Request request, Response response) {
        Event event;
        // TODO: Eventually we want this to be more secure, ie we need an org token to create an event
        if (Validate.createEvent(request,response,userSet,eventSet)) {
            event = service.createEvent(
                    request.queryParams("eventName"),
                    request.queryParams("orgName"),
                    getDate(request.queryParams("eventDate")),
                    request.queryParams("eventMessage"));
            eventSet.put(request.queryParams("eventName"), event);
            return Optional.of(event);
        }
        else { // Event could not be created. Response was modified to reflect the error.
            return Optional.empty();
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



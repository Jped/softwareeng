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

    public Optional<User> signUp(Request request, Response response) {
        /*String userEmail = request.queryParams("userEmail");
        String userPassword = request.queryParams("userPassword");

        // Check if minimum parameters to signUp are provided: email and password
        if (userEmail == null || userPassword == null) {
            UpdateResponse(response, 400, "Missing field");
            //System.out.println("Missing field");
            return Optional.empty();
        }
        // Validate email address
        else if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", userEmail)) {
            UpdateResponse(response, 400, "Email format incorrect");
            System.out.println("Email format incorrect");
            return Optional.empty();
        }
        // Check if password is between 6 and 100 alphanumeric characters and is not the same as any email field
        else if (!Pattern.matches("[A-Z,a-z,0-9]{6,}", userPassword) | userEmail.equals(userPassword)) {
            UpdateResponse(response, 400, "Password not acceptable");
            System.out.println("Password not acceptable");
            return Optional.empty();
        }
        // If user is already signed up, error code 409
        else if (userSet.containsKey(request.queryParams("userEmail"))) {
            response.status(409);
            response.body("No new user was signed up because a user already exists with this email address.");
            System.out.println("No new user was signed up because a user already exists with this email address.");
            return Optional.empty();
        } else {*/
            if (Validate_signUp(request,response)) {
                User  u = service.createUser(
                        getUserType(request),
                        request.queryParams("userName"),
                        request.queryParams("userPassword"),
                        request.queryParams("userPhone"),
                        request.queryParams("userEmail"),
                        getDate(request.queryParams("userBirthday")),
                        getUserGender(request));
                userSet.put(request.queryParams("userEmail"), u);

                response.status(200);
                response.body(String.valueOf(u));

                return Optional.of(u);
            }
            else {  //User could not be signed up. Response code and message were returned with more details.
                return Optional.empty();
            }
        }

    private Boolean Validate_signUp(Request request, Response response) {
        String userEmail = request.queryParams("userEmail");
        String userPassword = request.queryParams("userPassword");

        // Check if minimum parameters to signUp are provided:
        // email, password, userType, userName, userPhone, gender (userBirthday is optional)
        if (    userEmail == null ||
                userPassword == null ||
                request.queryParams("userName") == null ||
                request.queryParams("userType") == null ||
                request.queryParams("userPhone") == null ||
                request.queryParams("gender") == null
           ) {
            UpdateResponse(response, 400, "Missing field");
            return false;
        }

        // Validate provided parameters:
        // Validate email address
        else if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", userEmail)) {
            UpdateResponse(response, 400, "Email format incorrect");
            return false;
        }
        // Check if password is between 6 and 100 alphanumeric characters and ensure not the same userName, userEmail, userPhone
        else if (!Pattern.matches("[A-Z,a-z,0-9]{6,}", userPassword) ||
                userPassword.equals(userEmail) ||
                userPassword.equals(request.queryParams("userName")) ||
                userPassword.equals(request.queryParams("userPhone"))
        ) {
            UpdateResponse(response, 400, "Password not acceptable");
            return false;
        }
        // Validate userName
        else if (!Pattern.matches("[A-Za-z0-9_\\.]+", request.queryParams("userName"))) {
            UpdateResponse(response, 400, "Username not acceptable");
            return false;
        }
        // Validate userType - must be boolean true or false
        //else if (!Pattern.matches("false" || "FALSE || ", request.queryParams("userType")) {
        //    UpdateResponse(response, 400, "Username not acceptable");
         //   return false;
        //}
        // If user is already signed up, error code 409
        else if (userSet.containsKey(request.queryParams("userEmail"))) {
            response.status(409);
            response.body("No new user was signed up because a user already exists with this email address.");
            return false;
        }
        else {
            return true;
            }
    }

        private void UpdateResponse(Response response, Integer code, String message) {
        response.status(code);
        response.body(message);
        System.out.println(message);
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



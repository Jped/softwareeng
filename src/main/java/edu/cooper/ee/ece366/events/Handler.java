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

            response.status(200);
            response.body(String.valueOf(u));

            return Optional.of(u);
        }
        else {  //User could not be signed up. Response code and message were returned with more details.
            return Optional.empty();
        }
    }

    /*private Boolean Validate_signUp(Request request, Response response) {
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
        // Validate userType - must be String true or false, case insensitive
        else if (!Pattern.matches("(?i)false|(?i)true|0", request.queryParams("userType").strip())) {
            UpdateResponse(response, 400, "userType not acceptable. Enter 'true' for an organization and 'false' for a member.");
            return false;
        }
        // Validate gender - must be String true or false, case insensitive
        else if (!Pattern.matches("(?i)false|(?i)true|0", request.queryParams("gender").strip())) {
            UpdateResponse(response, 400, "gender not acceptable. Enter 'true' for a -- and 'false' for --.");
            return false;
        }
        // Validate userPhone
        else if (!validatePhoneNumber(request.queryParams("userPhone"))) {
            UpdateResponse(response, 400, "userPhone not acceptable");
            return false;
        }
        // TODO: Validate birthday
        // Check if user is already signed up
        else if (userSet.containsKey(request.queryParams("userEmail"))) {
            UpdateResponse(response, 409, "No new user was signed up because a user already exists with this email address.");
            return false;
        }
        else {
            return true;
            }
    }*/

    public static void UpdateResponse(Response response, Integer code, String message) {
        response.status(code);
        response.body(message);
        System.out.println(message);
    }

    /*// Used to validate phone number. Source: https://www.journaldev.com/641/regular-expression-phone-number-validation-in-java
    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;
    }*/

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



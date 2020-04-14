package edu.cooper.ee.ece366.events;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.cooper.ee.ece366.events.model.*;
import edu.cooper.ee.ece366.events.Service;

import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import edu.cooper.ee.ece366.events.util.Validate;
import netscape.javascript.JSObject;
import org.eclipse.jetty.http.HttpParser;
import org.jdbi.v3.core.result.ResultIterator;
import spark.Request;
import spark.Response;


// TODO ERROR: there are two types of things we need to check
//      (1) if all the relevant parameters have been sent
//      (2) if the parameters are wrong
//      with this information we need to update the res code

public class Handler {


    private Service service;
    private EvantStore es;
    public Handler(EvantStore es) {
        this.es = es;
        this.service = new Service(es);
    }

    public static void UpdateResponse(Response response, Integer code, String message) {
        response.status(code);
        response.body(message);
        System.out.println(message);
    }

    public Optional<User> signUp(Request request, Response response) {
        if (Validate.signUp(request,response, es)) {  //Passed in userSet only because of in memory configuration. This should be changed when db integrated.
            JsonObject reqObj = new Gson().fromJson(request.body(), JsonObject.class);
            System.out.print(reqObj);
            User  u = service.createUser(
                    getUserType(reqObj),
                    makeString(reqObj.get("userName")),
                    makeString(reqObj.get("userPassword")),
                    makeString(reqObj.get("userPhone")),
                    makeString(reqObj.get("userEmail")),
                    getDate(makeString(reqObj.get("userBirthday"))),
                    getUserGender(reqObj));


            UpdateResponse(response,200,String.valueOf(u));

            return Optional.of(u);
        }
        else {  //User could not be signed up. Response code and message were returned with more details.
            return Optional.empty();
        }
    }


    public Optional<User> logIn(Request request, Response response) {
        // TODO: need to differentiate between loginTypes
        JsonObject reqObj = new Gson().fromJson(request.body(), JsonObject.class);
        String userEmail = reqObj.get("userEmail").getAsString();
        String userPassword = reqObj.get("userPassword").getAsString();
        Boolean userType = getUserType(reqObj);

        // Check if parameters to logIn are provided: email and password. This avoids an unnecessary db query.
        if (userEmail == null || userPassword == null) {
            Handler.UpdateResponse(response, 400, "Missing field");
            return Optional.empty();
        }
        // Ensure userEmail is already signedUp
        if ((userType == false && !es.checkMember(userEmail)) || (userType == true && !es.checkOrg(userEmail))) { //Member type but email not in the table or org type but email not in table
            Handler.UpdateResponse(response, 404, "This userEmail is not registered.");
            return Optional.empty();
        }
        else {
            User u = service.getUser(userEmail, userType);
            Boolean correctPass = service.verifyPassword(u, userPassword);

            if (correctPass){
                request.session().attribute("logged in", u);
                response.cookie("sessid", request.session().id());
                UpdateResponse(response,200,"Log-in successful");
                return Optional.of(u);
            }
            else{
                UpdateResponse(response,404,"Log-in failed");
                return Optional.empty();
            }
        }
    }
    public Optional<User> isValidUser(Request request, Response response){
        JsonObject reqObj = new Gson().fromJson(request.body(), JsonObject.class);
        if (request.session().attribute("logged in") == null || !request.session().id().equals(reqObj.get("userSesh").getAsString())){
            UpdateResponse(response, 404, "Invalid user");
            request.session().removeAttribute("logged in");
            request.cookies().clear();
            return Optional.empty();
        }
        return Optional.of(request.session().attribute("logged in"));
    }
    public Optional<User> logOut(Request request, Response response) {
        User u = request.session().attribute("logged in");
        // Ensure a user is logged in
        if (u == null) {
            UpdateResponse(response,404,"No user logged in");
            return Optional.empty();
        }

        // Destroy session
        request.session().removeAttribute("logged in");
        UpdateResponse(response, 200, "Log-out successful");
        return Optional.of(u);
    }

    public Optional<Event> createEvent(Request request, Response response) {
        Event event;
        if (Validate.createEvent(request,response,es)) {
            JsonObject reqObj = new Gson().fromJson(request.body(), JsonObject.class);
            User u = request.session().attribute("logged in");
            event = service.createEvent(
                    reqObj.get("eventName").getAsString(),
                    u.getName(),
                    getDate(reqObj.get("eventDate").getAsString()),
                    reqObj.get("eventMessage").getAsString());

            UpdateResponse(response,200,String.valueOf(event));
            return Optional.of(event);
        }
        else { // Event could not be created. Response was modified to reflect the error.
            return Optional.empty();
        }
    }

    public Boolean joinEvent(Request request, Response response) {
        // For now, we assume each event name is unique but in future should allow multiple orgs to have same event
        if (Validate.joinEvent(request,response,es)) {
            JsonObject reqObj = new Gson().fromJson(request.body(), JsonObject.class);
            Member m = request.session().attribute("logged in");
            service.joinEvent(reqObj.get("eventName").getAsString(), reqObj.get("orgName").getAsString(), m);
            UpdateResponse(response, 200, "Joined event successfully");
            return true;
        }
        else {
            return false;
        }
    }

    public ResultIterator<Event> myEvents(Request request, Response response) {
        User u = request.session().attribute("logged in");
        ResultIterator<Event> myEvents = es.getMyEvents(u.getEmail());
        UpdateResponse(response, 200, String.valueOf(myEvents));
        return myEvents;
    }

    public ResultIterator<Event> upcomingEvents(Request request, Response response) {
        ResultIterator<Event> upcomingEvents = es.getUpcomingEvents();
        UpdateResponse(response, 200, String.valueOf(upcomingEvents));
        return upcomingEvents;
    }

    private Boolean getUserType(JsonObject reqObj) {
        if (reqObj.get("userType") != null) {
            return Boolean.valueOf(reqObj.get("userType").getAsString());
        }
        return false;
    }

    private Boolean getUserGender(JsonObject reqObj) {
        return Boolean.valueOf(makeString(reqObj.get("userGender")));
    }

    private LocalDateTime getDate(String date) {
        // TODO: verify that the date is in kosher format provided by the user
        if(date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(date, formatter);
        }
        return LocalDateTime.now();
    }
    private String makeString(JsonElement elem){
        if (elem != null){
            return elem.getAsString();
        }
        return null;
    }

}



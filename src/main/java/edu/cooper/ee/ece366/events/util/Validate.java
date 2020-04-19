package edu.cooper.ee.ece366.events.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.cooper.ee.ece366.events.Handler;
import edu.cooper.ee.ece366.events.model.EvantStore;
import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.User;
import spark.Request;
import spark.Response;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class Validate {

    //Passed in userSet only because of in memory configuration. This should be changed when db integrated.
    public static Boolean signUp(Request request, Response response, EvantStore es) {
        JsonObject reqObj = new Gson().fromJson(request.body(), JsonObject.class);
        String userName = reqObj.get("userName").getAsString();
        String userEmail = reqObj.get("userEmail").getAsString();
        String userPhone = reqObj.get("userPhone").getAsString();
        String userGender = reqObj.get("userGender").getAsString();
        String userPassword = reqObj.get("userPassword").getAsString();
        Boolean userType = false;
        if (reqObj.get("userType") != null) {
            userType = Boolean.valueOf(reqObj.get("userType").getAsString());
        }

        // Check if minimum parameters to signUp are provided:
        // email, password, userType, userName, userPhone, (gender and userBirthday is optional - both of these only apply to members)
        if (    userEmail == null ||
                userPassword == null ||
                userType == null ||
                userName == null ||
                userPhone == null) {
            Handler.UpdateResponse(response, 400, "Missing field");
            return false;
        }

        // Validate provided parameters:
        // Validate email address
        else if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", userEmail)) {
            Handler.UpdateResponse(response, 400, "Email format incorrect");
            return false;
        }
        // Check if password is between 6 and 100 alphanumeric characters and ensure not the same userName, userEmail, userPhone
        else if (!Pattern.matches("[A-Z,a-z,0-9]{6,}", userPassword) ||
                userPassword.equals(userEmail) ||
                userPassword.equals(userName) ||
                userPassword.equals(userPhone)
        ) {
            Handler.UpdateResponse(response, 400, "Password not acceptable");
            return false;
        }
        // Validate userName
        else if (!Pattern.matches("[A-Za-z0-9_\\.]+", userName)) {
            Handler.UpdateResponse(response, 400, "Username not acceptable");
            return false;
        }
        // Validate userType - must be String true or false, case insensitive
        else if (!Pattern.matches("(?i)false|(?i)true|0", userType.toString())) {
            Handler.UpdateResponse(response, 400, "userType not acceptable. Enter 'true' for an organization and 'false' for a member.");
            return false;
        }
        // Validate userPhone
        else if (!validatePhoneNumber(userPhone)) {
            Handler.UpdateResponse(response, 400, "userPhone not acceptable");
            return false;
        }
        // TODO: Validate birthday
        // Check if user is already signed up
        else if ((userType == false && es.checkMember(userEmail)) || (userType == true && es.checkOrg(userEmail))) {
            Handler.UpdateResponse(response, 409, "No new user was signed up because a user already exists with this email address.");
            return false;
        }
        else {
            // If user is of member type, additional checks:
            if (Pattern.matches("(?i)false|0", userType.toString())) {
                // Validate gender - must be String true or false, case insensitive
                if (!Pattern.matches("(?i)false|(?i)true|0", userGender)) {
                    Handler.UpdateResponse(response, 400, "gender not acceptable. Enter 'true' for a -- and 'false' for --.");
                    return false;
                }
                else {
                    return true;
                }
            }
            // If user is of org type:
            return true;
        }
    }


    public static Boolean createEvent(Request request, Response response, EvantStore es) {
        User u = request.session().attribute("logged in");
        if (u == null) {
            Handler.UpdateResponse(response, 404, "Organization is not logged in.");
            return false;
        }
        // Check whether orgEmail is registered
        else if (!u.isOrganization()) {
            Handler.UpdateResponse(response, 404, "This organization email is not registered.");
            return false;
        }
        // Check if minimum parameters to createEvent are provided: eventName, eventDate
        else if (request.queryParams("eventName") == null || request.queryParams("eventDate") == null) {
            Handler.UpdateResponse(response, 400, "Missing field");
            return false;
        }
        // Check whether event already exists
        else if (es.checkEvent(request.queryParams("eventName"), u.getName())) {
            Handler.UpdateResponse(response, 409, "An event with this name already exists for the given organization.");
            return false;
        }
        // Check whether eventDate is supplied in valid format
        else if (!validateDate(request.queryParams("eventDate"))) {
            Handler.UpdateResponse(response, 400, "Datetime format is incorrect");
            return false;
        }
        else {
            return true;
        }
    }


    public static boolean joinEvent(Request request, Response response, EvantStore es) {
        // Check that user email provided and eventName provided
        User u = request.session().attribute("logged in");
        if (request.queryParams("eventName") == null  || request.queryParams("orgName") == null) {
            Handler.UpdateResponse(response, 400, "Missing field");
            return false;
        }
        // Check if user exists
        else if (u == null || u.isOrganization()) {
            Handler.UpdateResponse(response, 400, "User Not Signed In");
            return false;
        }
        // Check if event exists
        // For now, we assume each event name is unique but in future should allow multiple orgs to have same event
        else if (!es.checkEvent(request.queryParams("eventName"), request.queryParams("orgName"))) {
            Handler.UpdateResponse(response, 400, "No such event exists");
            return false;
        }
        // TODO: Check if user already in event. This should be implemented after db integration.
        else {
            return true;
        }
    }

    // Used to validate phone number. Source: https://www.journaldev.com/641/regular-expression-phone-number-validation-in-java
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
    }

    private static boolean validateDate(String datetime) {
        // Input to be parsed should strictly follow the defined date format below
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        format.setLenient(false);

        try {
            format.parse(datetime);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


}

package edu.cooper.ee.ece366.events.util;

import edu.cooper.ee.ece366.events.Handler;
import edu.cooper.ee.ece366.events.model.User;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Validate {

    //Passed in userSet only because of in memory configuration. This should be changed when db integrated.
    public static Boolean signUp(Request request, Response response, HashMap<String, User> userSet) {
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
                userPassword.equals(request.queryParams("userName")) ||
                userPassword.equals(request.queryParams("userPhone"))
        ) {
            Handler.UpdateResponse(response, 400, "Password not acceptable");
            return false;
        }
        // Validate userName
        else if (!Pattern.matches("[A-Za-z0-9_\\.]+", request.queryParams("userName"))) {
            Handler.UpdateResponse(response, 400, "Username not acceptable");
            return false;
        }
        // Validate userType - must be String true or false, case insensitive
        else if (!Pattern.matches("(?i)false|(?i)true|0", request.queryParams("userType").strip())) {
            Handler.UpdateResponse(response, 400, "userType not acceptable. Enter 'true' for an organization and 'false' for a member.");
            return false;
        }
        // Validate gender - must be String true or false, case insensitive
        else if (!Pattern.matches("(?i)false|(?i)true|0", request.queryParams("gender").strip())) {
            Handler.UpdateResponse(response, 400, "gender not acceptable. Enter 'true' for a -- and 'false' for --.");
            return false;
        }
        // Validate userPhone
        else if (!validatePhoneNumber(request.queryParams("userPhone"))) {
            Handler.UpdateResponse(response, 400, "userPhone not acceptable");
            return false;
        }
        // TODO: Validate birthday
        // Check if user is already signed up
        else if (userSet.containsKey(request.queryParams("userEmail"))) {
            Handler.UpdateResponse(response, 409, "No new user was signed up because a user already exists with this email address.");
            return false;
        }
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
}

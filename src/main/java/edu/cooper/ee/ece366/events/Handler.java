package edu.cooper.ee.ece366.events;

import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.Member;
import edu.cooper.ee.ece366.events.model.User;
import edu.cooper.ee.ece366.events.model.Organization;

import java.util.HashMap;
import java.util.List;
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

    public Boolean logIn(Request request, HashMap<String, User> userSet){
        if (userSet.containsKey(request.queryParams("userEmail"))){
            if (request.queryParams(("userPassword")).equals((userSet.get(request.queryParams("userEmail"))).getPassword())) {
                System.out.println("log in successful");
                return true;
            }
            else{
                System.out.println("password incorrect");
                return false;
            }

        }
        else{
            System.out.println("User not found");
            return false;
        }

    }

    private Boolean getUserType(Request request){
        return Boolean.valueOf(request.queryParams("userType"));
    }
}



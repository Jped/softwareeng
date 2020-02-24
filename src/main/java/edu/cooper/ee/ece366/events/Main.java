package edu.cooper.ee.ece366.events;

import spark.Spark;
import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.Member;
import edu.cooper.ee.ece366.events.model.User;
import edu.cooper.ee.ece366.events.model.Organization;

import java.util.*;
import java.util.HashSet;

public class Main {

  public static void main(String[] args) {
    HashMap<String, User> userSet = new HashMap<String, User>();
    Handler handler = new Handler();

    Spark.get("/ping", (req, res) -> "OK");
    Spark.post("/signUp", (req, res) -> {
      User u = handler.createUser(req);
      userSet.put(u.getEmail(), u);
      System.out.println(userSet);
      // Check for errors, check for duplicate users, hash password
      // Move this into handler
      return res;
    });
    Spark.post("/logIn", (req, res) -> handler.logIn(req, userSet));
    Spark.post("/createEvent", (req, res) -> "Input event info");
    Spark.post("/joinEvent", (req, res) -> "User added to event");
    Spark.get("/myEvents", (req, res) -> "List of my events");
    Spark.get("/upcomingEvents", (req, res) -> "List of upcoming events");

  }
}

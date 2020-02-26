package edu.cooper.ee.ece366.events;

import spark.Spark;
import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.Member;
import edu.cooper.ee.ece366.events.model.User;
import edu.cooper.ee.ece366.events.model.Organization;
import edu.cooper.ee.ece366.events.util.JsonTransformer;

import java.util.*;
import java.util.HashSet;

public class Main {

  public static void main(String[] args) {
    HashMap<String, User> userSet = new HashMap<String, User>();
    HashMap<String, Event> eventSet = new HashMap<String, Event>();
    HashMap<Event, User> eventUserMap = new HashMap<Event, User>();

    Handler handler = new Handler();
    JsonTransformer jsonTransformer = new JsonTransformer();

    Spark.get("/ping", (req, res) -> "OK");
    Spark.post("/signUp", (req, res) -> {
      User u = handler.createUser(req);
      userSet.put(u.getEmail(), u);
      System.out.println(userSet);
      // Check for errors, check for duplicate users, hash password
      // Move this into handler
      return res;//res;
    }, jsonTransformer);

    Spark.post("/logIn", (req, res) -> handler.logIn(req, userSet));
    Spark.post("/createEvent", (req, res) -> handler.createEvent(req, userSet, eventSet));
    Spark.post("/joinEvent", (req, res) -> handler.joinEvent(req, eventSet, userSet, eventUserMap));
    Spark.get("/myEvents", (req, res) -> handler.myEvents(req, userSet, eventUserMap));
    Spark.get("/upcomingEvents", (req, res) -> handler.upcomingEvents(req, eventSet));

  }
}

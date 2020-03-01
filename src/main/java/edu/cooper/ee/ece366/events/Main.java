package edu.cooper.ee.ece366.events;

import spark.Spark;
import edu.cooper.ee.ece366.events.model.Event;
import edu.cooper.ee.ece366.events.model.Member;
import edu.cooper.ee.ece366.events.model.User;
import edu.cooper.ee.ece366.events.model.Organization;
import edu.cooper.ee.ece366.events.util.JsonTransformer;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Handler handler = new Handler();
        JsonTransformer jsonTransformer = new JsonTransformer();

        Spark.post("/signUp", (req, res) -> handler.signUp(req, res).orElse(null),jsonTransformer);
        Spark.post("/logIn", (req, res) -> handler.logIn(req).orElse(null), jsonTransformer);
        Spark.post("/createEvent", (req, res) -> handler.createEvent(req).orElse(null), jsonTransformer);
        Spark.post("/joinEvent", (req, res) -> handler.joinEvent(req), jsonTransformer);
        Spark.get("/myEvents", (req, res) -> handler.myEvents(req), jsonTransformer);
        Spark.get("/upcomingEvents", (req, res) -> handler.upcomingEvents(req), jsonTransformer);
    }

}

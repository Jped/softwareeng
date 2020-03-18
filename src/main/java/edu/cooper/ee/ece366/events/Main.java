package edu.cooper.ee.ece366.events;
import edu.cooper.ee.ece366.events.model.EvantStore;
import edu.cooper.ee.ece366.events.model.EvantMysqlImpl;
import org.jdbi.v3.core.Jdbi;
import spark.Spark;
import edu.cooper.ee.ece366.events.util.JsonTransformer;

import java.util.*;


public class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/evant2?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST";
        String username="main";
        String password="software";
        Jdbi jdbi = Jdbi.create(url, username, password);
        EvantMysqlImpl es = new EvantMysqlImpl(jdbi);

        es.populateDb();
        Handler handler = new Handler(es);
        JsonTransformer jsonTransformer = new JsonTransformer();
        Spark.post("/signUp", (req, res) -> handler.signUp(req, res).orElse(null),jsonTransformer);
        Spark.post("/logIn", (req, res) -> handler.logIn(req, res).orElse(null), jsonTransformer);
        Spark.post("/createEvent", (req, res) -> handler.createEvent(req, res).orElse(null), jsonTransformer);
        Spark.post("/joinEvent", (req, res) -> handler.joinEvent(req, res), jsonTransformer);
        Spark.get("/myEvents", (req, res) -> handler.myEvents(req, res), jsonTransformer);
        Spark.get("/upcomingEvents", (req, res) -> handler.upcomingEvents(req, res), jsonTransformer);
    }

}

package edu.cooper.ee.ece366.events;
import edu.cooper.ee.ece366.events.model.Organization;
import spark.Filter;
import edu.cooper.ee.ece366.events.model.EvantStore;
import edu.cooper.ee.ece366.events.model.EvantMysqlImpl;
import org.jdbi.v3.core.Jdbi;
import spark.Spark;
import edu.cooper.ee.ece366.events.util.JsonTransformer;
import spark.ModelAndView;
import java.time.LocalDateTime;
import java.util.*;
import edu.cooper.ee.ece366.events.model.User;

public class Main {

    public static void main(String[] args) {
        Spark.staticFiles.location("/public");
//        Spark.after((Filter) (request, response) -> {
//            response.header("Access-Control-Allow-Origin", "*");
//            response.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//        });
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
        Spark.post("/isValidUser", (req, res) -> handler.isValidUser(req,res).orElse(null), jsonTransformer);
        Spark.get("/logOut", (req, res) -> handler.logOut(req,res).orElse(null), jsonTransformer);
        Spark.post("/createEvent", (req, res) -> handler.createEvent(req, res).orElse(null), jsonTransformer);
        Spark.post("/joinEvent", (req, res) -> handler.joinEvent(req, res), jsonTransformer);
        Spark.get("/myEvents", (req, res) -> handler.myEvents(req, res), jsonTransformer);
        Spark.get("/upcomingEvents", (req, res) -> handler.upcomingEvents(req, res), jsonTransformer);


        Spark.options("/*", (req, res) -> {
            String accessControlRequestHeaders = req.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                res.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = req.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                res.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Headers", "*");
            res.type("application/json");
        });

    }

}

package edu.cooper.ee.ece366.events;

import spark.Spark;

public class Main {

  public static void main(String[] args) {

    Spark.get("/ping", (req, res) -> "OK");
    Spark.get("/myEvents", (req, res) -> "List of my events");
    Spark.get("/upcomingEvents", (req, res) -> "List of upcoming events");
    Spark.post("/signUp", (req, res) -> "Input user info");
    Spark.post("/logIn", (req, res) -> "Input login info");	
    Spark.post("/createEvent", (req, res) -> "Input event info");
    Spark.post("/joinEvent", (req, res) -> "User added to event");
  }
}

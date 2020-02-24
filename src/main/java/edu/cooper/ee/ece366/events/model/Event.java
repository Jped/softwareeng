package edu.cooper.ee.ece366.events.model;

public class Event {
    private final String name;
    private final String orgName;
    private final String date;
    private final String eventMessage;
 
   public Event(String name, String orgName, String date, String eventMessage) {
	this.name = name;
	this.orgName = orgName;
	this.date = date;
	this.eventMessage = eventMessage;
   }

   public String getName(){
       return name;
   }

   public String getOrgName(){
       return orgName;
   }
   public String getDate(){
       return date;
   }

   public String getEventMessage(){
       return eventMessage;
   }
}


package edu.cooper.ee.ece366.events.model;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class Event {
    @Expose private final String name;
    @Expose private final String orgName;
    @Expose private final LocalDateTime date;
    @Expose private final String eventMessage;
 
   public Event(String name, String orgName, LocalDateTime date, String eventMessage) {
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
   public LocalDateTime getDate(){
       return date;
   }

   public String getEventMessage(){
       return eventMessage;
   }
}


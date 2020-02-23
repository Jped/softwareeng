package edu.cooper.ee.ece366.events.model;

public class Event {
   private final Long id;
   private final Long orgID;
   private final String name;
   private final String date;
   private final String eventMessage; 
 
   public Event(Long id, Long orgID, String name, String date, String eventMessage) {
	this.id = id;
	this.orgID = orgID;
	this.name = name;
	this.date = date;
	this.eventMessage = eventMessage;
   }
}


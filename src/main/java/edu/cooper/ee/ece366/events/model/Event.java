package edu.cooper.ee.ece366.events.model;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class Event {
    @Expose private String name;
    @Expose private String orgName;
    @Expose private LocalDateTime date;
    @Expose private String eventMessage;
    private long id;

    public Event(long id, String name, String orgName, LocalDateTime date, String eventMessage) {
        this.name = name;
        this.orgName = orgName;
        this.date = date;
        this.eventMessage = eventMessage;
        this.id = id;
    }

    public Event(String name, String orgName, LocalDateTime date, String eventMessage) {
        this.name = name;
        this.orgName = orgName;
        this.date = date;
        this.eventMessage = eventMessage;
        this.id = -1;
    }

    public Event(){
        this.name = null;
        this.orgName = null;
        this.date = LocalDateTime.now();
        this.eventMessage = null;
        this.id = -1;
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

    public void setID(long id) {this.id = id;}
    public long getID() {return id;}

    public void setName(String name){this.name = name;}
    public void setOrgName(String orgName){this.orgName = orgName;}
    public void setDate(LocalDateTime date){this.date = date;}
    public void setEventMessage(String eventMessage){this.eventMessage = eventMessage;}
}

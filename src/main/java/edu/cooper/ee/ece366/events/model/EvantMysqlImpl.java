package edu.cooper.ee.ece366.events.model;

import org.jdbi.v3.core.Jdbi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.result.ResultIterator;
import org.jdbi.v3.core.statement.Query;

public class EvantMysqlImpl implements EvantStore{

    private final Jdbi jdbi;

    public EvantMysqlImpl(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    public void populateDb(){
        jdbi.useHandle(
                handle -> {
                    handle.execute("create table if not exists members (id bigint auto_increment, name varchar(255), password varchar(255), phone varchar(255), email varchar(255), birthday datetime, gender bool, primary key(id));");
                    handle.execute("create table if not exists orgs (id bigint auto_increment, name varchar(255), password varchar(255), phone varchar(255), email varchar(255), primary key(id));");
                    handle.execute("create table if not exists events (id bigint auto_increment, name varchar(255), orgName varchar(255), eventMessage varchar(1024), date datetime, primary key(id));");
                    handle.execute("create table if not exists signUps (id bigint auto_increment, userID bigint, eventID bigint, primary key(id));");
                });
    }

    public Member createMember(Member member){
        Integer id = jdbi.withHandle(
                handle ->
                        handle.createUpdate("insert into members (name, password, phone, email, birthday, gender) values (:name, :password, :phone, :email, :birthday, :gender)")
                                .bind("name", member.getName())
                                .bind("password", member.getPassword())
                                .bind("phone", member.getPhone())
                                .bind("email", member.getEmail())
                                .bind("birthday", member.getBirthday())
                                .bind("gender", member.getGender())
                                .executeAndReturnGeneratedKeys("id")
                                .mapTo(Integer.class)
                                .one());
        member.setID(id);
        return member;
    }

    public Member getMember(String memberEmail){
        Member member = jdbi.withHandle(
                handle ->
                        handle.select("select id, name, password, phone, email, birthday, gender from members where email = ?", memberEmail)
                                .mapToBean(Member.class)
                                .one());
        return member;
    }


    public Organization createOrg(Organization organization){
        Integer id = jdbi.withHandle(
                handle ->
                        handle.createUpdate("insert into orgs (name, password, phone, email) values (:name, :password, :phone, :email)")
                                .bind("name", organization.getName())
                                .bind("password", organization.getPassword())
                                .bind("phone", organization.getPhone())
                                .bind("email", organization.getEmail())
                                .executeAndReturnGeneratedKeys("id")
                                .mapTo(Integer.class)
                                .one());
        organization.setID(id);
        return organization;
    }

    public Organization getOrg(String orgEmail){
        System.out.println(orgEmail);
        Optional<Organization> org = jdbi.withHandle(
                handle ->
                        handle.select("select id, name, password, phone, email from orgs where email = ?", orgEmail)
                                .mapToBean(Organization.class)
                                .findOne());
        return org.get();
    }

    public Boolean checkMember(String memberEmail){
         Optional<Boolean> resp = jdbi.withHandle(
                handle ->
                        handle.select("select id from members where email = ?", memberEmail)
                        .mapTo(Boolean.class)
                        .findOne());
         if (resp.isEmpty()){
             return false;
         }
         return true;
    }

    public Boolean checkOrg(String orgEmail){
        Optional<Integer> orgID = jdbi.withHandle(
                handle ->
                        handle.select("select id from orgs where email = ?", orgEmail)
                                .mapTo(Integer.class)
                                .findOne());
        if (orgID.isEmpty()) {
            return false;
        }
        return true;
    }

    public Boolean checkEvent(String eventName, String orgName){
        Optional<Integer> eventID = jdbi.withHandle(
                handle ->
                        handle.select("select id from events where name =? and orgName =?", eventName, orgName)
                                .mapTo(Integer.class)
                                .findOne());
        //System.out.println(eventName + " " + orgName);
        if (eventID.isEmpty()) {
            return false;
        }
        return true;
    }

    public Event getEvent(String eventName, String orgName){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT * FROM events WHERE name = :eventName AND orgName = :orgName")
                .bind("eventName", eventName)
                .bind("orgName", orgName)
                .mapToBean(Event.class)
                .one());
    }

    public Event createEvent(Event e){
        Integer id = jdbi.withHandle(
                handle ->
                        handle.createUpdate("INSERT INTO events (name, orgName, eventMessage, date) values (:name, :orgName, :eventMessage, :date)")
                        .bind("name", e.getName())
                        .bind("orgName", e.getOrgName())
                        .bind("eventMessage", e.getEventMessage())
                        .bind("date", e.getDate())
                        .executeAndReturnGeneratedKeys("id")
                        .mapTo(Integer.class)
                        .one());
        e.setID(id);
        return e;
    }

    public void joinEvent(Member m, Event e){
        jdbi.withHandle(
                handle ->
                        handle.execute("INSERT INTO signUps (userID, eventID) values (?, ?)", m.getID(), e.getID()));
        return;
    }

    public ResultIterator<Event> getMyEvents(String email) {
        return jdbi.withHandle(
                handle ->
                        handle.select("SELECT events.name, events.orgName, events.id, events.date, events.eventMessage FROM members join signUps ON members.id = signUps.userID join events ON signUps.eventID = events.id WHERE members.email = ?", email)
                        .mapToBean(Event.class)
                        .iterator());
    }

    public ResultIterator<Event> getUpcomingEvents() {
        return jdbi.withHandle(
                handle ->
                        handle.select("SELECT * FROM events WHERE date >= ?", LocalDateTime.now())
                                .mapToBean(Event.class)
                                .iterator());
    }
}

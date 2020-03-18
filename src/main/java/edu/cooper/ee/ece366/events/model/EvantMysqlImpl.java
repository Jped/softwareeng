package edu.cooper.ee.ece366.events.model;

import org.jdbi.v3.core.Jdbi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Member createMember(String userName, String userPassword, String userPhone, String userEmail, LocalDateTime userBirthday, Boolean userGender){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("insert into members (name, password, phone, email, birthday, gender) values (:name, :password, :phone, :email, :birthday, :gender)")
                        .bind("name", userName)
                        .bind("password", userPassword)
                        .bind("phone", userPhone)
                        .bind("email", userEmail)
                        .bind("birthday", userBirthday)
                        .bind("gender", userGender)
                        .mapToBean(Member.class)
                        .one());
    }

    public Member getMember(String memberEmail){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("select from members where email = :email")
                .bind("email", memberEmail)
                .mapToBean(Member.class)
                .one());
    }


    public Organization createOrg(String userName, String userPassword, String userPhone, String userEmail){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("insert into members (name, password, phone, email) values (:name, :password, :phone, :email)")
                                .bind("name", userName)
                                .bind("password", userPassword)
                                .bind("phone", userPhone)
                                .bind("email", userEmail)
                                .mapToBean(Organization.class)
                                .one());
    }
    public Organization getOrg(String orgEmail){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("select from orgs where email = :email")
                                .bind("email", orgEmail)
                                .mapToBean(Organization.class)
                                .one());
    }

    public Boolean checkMember(String memberEmail){
         return jdbi.withHandle(
                handle ->
                        handle.createQuery("select id from members where email =:email")
                        .bind("email", memberEmail)
                        .mapToBean(Boolean.class)
                        .one());
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
        if (eventID.isEmpty()) {
            return false;
        }
        return true;
    }

    public Event getEvent(String eventName, String orgName){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT FROM events WHERE name = :eventName AND orgName = :orgName")
                .bind("eventName", eventName)
                .bind("orgName", orgName)
                .mapToBean(Event.class)
                .one());
    }

    public Event createEvent(String eventName, String orgName, LocalDateTime eventDate, String eventMessage){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("INSERT INTO events (name, orgName, eventMessage, date) values (:name, :orgName, :eventMessage, :date)")
                        .bind("name", eventName)
                        .bind("orgName", orgName)
                        .bind("eventMessage", eventMessage)
                        .bind("date", eventDate)
                        .mapToBean(Event.class)
                        .one());
    }

    public Event joinEvent(String eventName, String orgName, String memberEmail){
        return jdbi.withHandle(
                handle ->
                        handle.createQuery("INSERT INTO signUps (userID, eventID)" +
                                        "SELECT m.id, e.id" +
                                        "FROM members m, events e" +
                                        "WHERE m.email = :memberEmail" +
                                        "AND e.name = :eventName" +
                                        "AND e.orgName = :orgName")
                        .bind("memberEmail", memberEmail)
                        .bind("eventName", eventName)
                        .bind("orgName", orgName)
                        .mapToBean(Event.class)
                        .one());

    }

    public ArrayList<Event> getMyEvents(String email) {
        return (ArrayList<Event>) jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT FROM members WHERE email = :email join signUps ON members.id = signUps.userID join events ON signUps.eventID = events.id")
                        .bind("email", email)
                        .mapToBean(Event.class)
                        .list());
    }

    public ArrayList<Event> getUpcomingEvents() {
        ArrayList<Event> UpComing = (ArrayList<Event>) jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT FROM events WHERE date >= FROM_UNIXTIME(:Now)")
                                .bind("Now", LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0)))
                                .mapToBean(Event.class)
                                .list());
        if (UpComing.isEmpty()) {
            return new ArrayList<Event>();
        }
        return UpComing;
    }
}

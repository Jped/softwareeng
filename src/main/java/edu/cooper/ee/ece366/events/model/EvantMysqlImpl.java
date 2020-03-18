package edu.cooper.ee.ece366.events.model;

import org.jdbi.v3.core.Jdbi;

import java.time.LocalDateTime;
import java.util.ArrayList;

import edu.cooper.ee.ece366.events.model.Member;

public class EvantMysqlImpl implements EvantStore{

    private final Jdbi jdbi;

    public EvantMysqlImpl(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    public void populateDb(){
        jdbi.useHandle(
                handle -> {
                    handle.execute("create table members (id bigint auto_increment, name varchar(255), password varchar(255), phone varchar(255), email varchar(255), birthday datetime, gender bool, primary key(id));");
                    handle.execute("create table orgs (id bigint auto_increment, name varchar(255), password varchar(255), phone varchar(255), email varchar(255), primary key(id));");
                    handle.execute("create table events (id bigint auto_increment, name varchar(255), orgName varchar(255), eventMessage varchar(1024), date datetime, primary key(id));");
                    handle.execute("create table signUps (id bigint auto_increment, userID bigint, eventID bigint, primary key(id)");
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

    public Organization createOrg(String userName, String userPassword, String userPhone, String userEmail){return null;}
    public Organization getOrg(String orgEmail){return null;}

    public Boolean checkUser(String userEmail){return null;}
    public Boolean checkOrg(String orgEmail){return null;}
    public Boolean checkEvent(String eventName){return null;}

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
        return (ArrayList<Event>) jdbi.withHandle(
                handle ->
                        handle.createQuery("SELECT FROM events WHERE date >= CURRENT_DATE()")
                        .mapToBean(Event.class)
                        .list());
    }
}

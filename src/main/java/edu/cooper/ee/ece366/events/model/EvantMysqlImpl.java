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
                    handle.execute("create table members (id bigint auto_increment, name varchar(255), password varchar(255), phone varchar(255), email varchar(255), birthday datetime, gender bool);");
                    handle.execute("create table orgs (id bigint auto_increment, name varchar(255), password varchar(255), phone varchar(255), email varchar(255));");
                    handle.execute("create table events (id bigint auto_increment, name varchar(255), orgName varchar(255), eventMessage varchar(1024), date datetime);");
                    handle.execute("create table signUps (id bigint auto_increment, userID bigint, eventID bigint");
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

    public Organization createOrg(String userName, String userPassword, String userPhone, String userEmail){}
    public Organization getOrg(String orgEmail){}

    public Boolean checkUser(String userEmail){}
    public Boolean checkOrg(String orgEmail){}
    public Boolean checkEvent(String eventName){}


    public Event getEvent(String eventName){}
    public Event createEvent(String eventName, String orgName, LocalDateTime eventDate, String eventMessage){}
    public Event joinEvent(Event e, Organization o){}

    public ArrayList<Event> getMyEvents(String email){}
    public ArrayList<Event> getUpcomingEvents(){}



    }

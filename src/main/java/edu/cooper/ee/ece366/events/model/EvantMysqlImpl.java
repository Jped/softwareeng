package edu.cooper.ee.ece366.events.model;

import org.jdbi.v3.core.Jdbi;

public class EvantMysqlImpl implements EvantStore{

    private final Jdbi jdbi;

    public EvantMysqlImpl(Jdbi jdbi){
        this.jdbi = jdbi;
    }

    public void populateDb(){
        jdbi.withHandle(
                handle -> {
                    handle.execute("create table members (id bigint auto_increment, name varchar(255), password varchar(255), phoneNumber varchar(255), email varchar(255), birthday datetime, gender bool);");
                    handle.execute("create table orgs (id bigint auto_increment, name varchar(255), password varchar(255), phoneNumber varchar(255), email varchar(255));");
                    handle.execute("create table events (id bigint auto_increment, name varchar(255), orgName varchar(255), eventMessage varchar(1024), date datetime);");
                    handle.execute("create table signUps (id bigint auto_increment, userID bigint, eventID bigint");
                });
    }
    


    }
}

package edu.cooper.ee.ece366.events.model;

import java.time.LocalDateTime;

public class Member extends User {

    private final LocalDateTime birthday;
    private final Boolean gender;

    public Member(String name, String password, String phone, String email, LocalDateTime birthday, Boolean gender){
        super(name, password, phone, email);
        this.birthday = birthday;
        this.gender = gender;
    }

}

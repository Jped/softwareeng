package edu.cooper.ee.ece366.events.model;

public class Member extends User {

    private final String birthday;
    private final String gender;

    public Member(String name, String phone, String email, String birthday, String gender){
        super(name, phone, email);
        this.birthday = birthday;
        this.gender = gender;
    }

}

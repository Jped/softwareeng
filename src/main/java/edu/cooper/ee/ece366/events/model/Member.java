package edu.cooper.ee.ece366.events.model;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;

public class Member extends User {

    @Expose private LocalDate birthday;
    @Expose private Boolean gender;

    public Member(long id, String name, String password, String phone, String email, LocalDate birthday, Boolean gender){
        super(id, name, password, phone, email);
        this.birthday = birthday;
        this.gender = gender;
    }
    public Member(String name, String password, String phone, String email, LocalDate birthday, Boolean gender){
        super(-1, name, password, phone, email);
        this.birthday = birthday;
        this.gender = gender;
    }

    public Member(){
        super(-1, null, null, null, null);
        this.birthday = LocalDate.now();
        this.gender = null;
    }

    public LocalDate getBirthday() { return birthday; }
    public Boolean getGender() { return gender; }

    public void setGender(Boolean gender){this.gender = gender;}
    public void setBirthday(LocalDate birthday){this.birthday = birthday; }

    public Boolean isOrganization(){
        return false;
    }
}

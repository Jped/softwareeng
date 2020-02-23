package edu.cooper.ee.ece366.events.model;

public abstract class User {
  private final String name;
  private final String phone;
  private final String email;
  //private final List organizations;
  public User(String name, String phone, String email){
    this.name = name;
    this.phone = phone;
    this.email = email;
  };

  public String getName(){
    return name;
  }


}

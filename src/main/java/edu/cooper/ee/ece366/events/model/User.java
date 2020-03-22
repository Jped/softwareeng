package edu.cooper.ee.ece366.events.model;

import com.google.gson.annotations.Expose;

public abstract class User {
  
  	
  @Expose private final String name;
  private final String password;
  @Expose private final String phone;
  @Expose private final String email;
  private long id;
  //private final List organizations;
  public User(long id, String name, String password, String phone, String email){
    this.id = id;
    this.name = name;
    this.password = password;
    this.phone = phone;
    this.email = email;
  };

  public String getName(){
    return name;
  }
  public String getEmail(){
    return email;
  }
  public String getPassword(){
    return password;
  }
  public String getPhone() { return phone; }

  public void setID(long id) { this.id = id; }
  public long getID() {return id;}


}

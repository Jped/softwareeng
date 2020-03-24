package edu.cooper.ee.ece366.events.model;

import com.google.gson.annotations.Expose;

public abstract class User {
  
  	
  @Expose private String name;
  private String password;
  @Expose private String phone;
  @Expose private String email;
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

  public void setName(String name){this.name = name;}
  public void setPassword(String password){this.password = password; }
  public void setPhone(String phone){this.phone = phone;}
  public void setEmail(String email){this.email = email;}

  public abstract Boolean isOrganization();

}

package edu.cooper.ee.ece366.events.model;

public class User {

  private final Long id;
  private final String name;
  private final String phone;
  private final String email;
  private final String birthday;
  private final String gender;
  //private final List organizations;
  public User(Long id, String name, String phone, String email, String birthday, String gender) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.birthday = birthday;
    this.gender = gender;

  }
}

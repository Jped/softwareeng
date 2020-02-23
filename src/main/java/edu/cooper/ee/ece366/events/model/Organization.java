package edu.cooper.ee.ece366.events.model;

public class Organization {

  private final Long id;
  private final String name;
  private final String phone;
  private final String email;

  public Organization(Long id, String name, String phone, String email) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.email = email;

  }
}

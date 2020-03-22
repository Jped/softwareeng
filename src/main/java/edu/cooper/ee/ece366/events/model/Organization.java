package edu.cooper.ee.ece366.events.model;

public class Organization extends User{
   public Organization(long id, String name, String password, String phone, String email){
      super(id, name, password, phone, email);
   }
   public Organization (String name, String password, String phone, String email) {
      super (-1, name, password, phone, email);
   }

   public Organization() {
      super(-1, null, null, null, null);
   }
}

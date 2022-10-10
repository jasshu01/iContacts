package com.example.icontacts;

import java.util.Comparator;

public class Contact {
    private String firstName, lastName, phone1, phone2, email;
    private int sno;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Contact() {
    }

    public Contact(String firstName, String phone1) {
        this.firstName = firstName;
        this.phone1 = phone1;
    }

    public Contact(int sno, String firstName, String phone1) {
        this.sno = sno;
        this.firstName = firstName;
        this.phone1 = phone1;
    }

    public Contact(String firstName, String lastName, String phone1) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone1 = phone1;
    }

    public Contact(String firstName, String lastName, String phone1, String phone2, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.email = email;
    }

    public static Comparator<Contact> contactsComparator = new Comparator<Contact>() {


        public int compare(Contact c1, Contact c2) {
            String name1 = c1.getFirstName().toUpperCase() + " " + c1.getLastName();
            String name2 = c2.getFirstName().toUpperCase() + " " + c2.getLastName();


            return name1.compareTo(name2);

        }
    };


}

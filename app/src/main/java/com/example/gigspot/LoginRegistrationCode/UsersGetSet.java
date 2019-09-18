package com.example.gigspot.LoginRegistrationCode;
//Getters and setters
public class UsersGetSet {
    //This will be the field name saved in the DB
    private String firstName;
    private String lastName;
    private String birthday;
    private int age;
    private String phone;
    private String email;
    private String TorTS;

    public UsersGetSet(){

    }

    public UsersGetSet(String firstName, String lastName, String birthday, int age, String phone, String email, String TorTS) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.TorTS = TorTS;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
    public String getTorTS() {
        return TorTS;
    }

}

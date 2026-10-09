package se.edugrade.model;

import se.edugrade.utility.Helper;

public class Member {
    private int id;
    private String name;

    //Konstruktor lämnas tom
    //public Member() {}

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(String name) {
        this.name = name;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
        public String toString() {
        return String.format(Helper.colorS("blue", "\uD83D\uDC65 MEMBER: [ID] %d [Name] %s "), id, name);
    }
}

package se.edugrade.model;

import se.edugrade.utility.Helper;

import java.sql.Date;

public class Author {
    private int id;
    private String name;
    private Date birthDate;
    private String nationality;

    //Konstruktor för befintlig författare (med ID autoINC från databasen)
    public Author(int id, String name, Date birthDate, String nationality) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    // Konstruktor för ny författare (utan ID)
    public Author(String name, Date birthDate, String nationality) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    @Override
    public String toString() {
        return Helper.colorS("red", "📚 ID: ") + id + " | " +
                Helper.colorS("green", "👤 Name: ") + name + " | " +
                Helper.colorS("yellow", "🎂 Birthdate: ") + birthDate + " | " +
                Helper.colorS("blue", "🌍 Nationality: ") + nationality;
    }

}

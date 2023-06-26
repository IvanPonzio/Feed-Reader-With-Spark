package com.example.spark.namedEntity;

import com.example.spark.namedEntity.themes.Themes;


public class Person extends NamedEntity {

    private String last_name;
    private String first_name;
    private String title;
    private String[] alternate_names;
    private Themes theme;
    private int personFrequency;
    public static int personCount = 0; // Public static attribute specific to Person class

    public Person(String name) {
        super(name, "person");
        this.personFrequency = 1;
        personCount++;                 // Increment personCount for each new Person instance
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAlternateNames(String[] alternate_names) {
        this.alternate_names = alternate_names;
    }

    public String[] getAlternateNames() {
        return alternate_names;
    }

    public int inqPersonFrequency() {
        return this.personFrequency++;
    }

    public int getPersonFrequency() {
        return this.personFrequency;
    }
    
    public void setThemePerson(Themes theme) {
        this.theme = theme;
    }

    public Themes getThemePerson() {
        return theme;
    }

    public static int getPersonCount() {
        return personCount;
    }
}
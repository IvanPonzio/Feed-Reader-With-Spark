package com.example.spark.namedEntity;

import com.example.spark.namedEntity.themes.Themes;


public class Organization extends NamedEntity{
    //private String name;
    private int employeeNumber;
    private String field;
    private Themes theme;
    int frequencyOrganization;
    public static int organizationCount = 0; // Public static attribute specific to Organization class


    public Organization(String name) {
        super(name, "organization");
        organizationCount++;                 // Increment organizationCount for each new Organization instance
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employees) {
        this.employeeNumber = employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField(){
        return field;
    }
    public void setField(String field){
        this.field = field;
    }
    

    public int inqOrganizationFrequency() {
        return this.frequencyOrganization++;
    }

    public int getOrganizationFrequency() {
        return this.frequencyOrganization;
    }

    public Themes getThemeOrganization() {
        return theme;
    }

    public void setThemeOrganization(Themes theme) {
        this.theme = theme;
    }
    
    public static int getOrganizationCount() {
        return organizationCount;
    }
}
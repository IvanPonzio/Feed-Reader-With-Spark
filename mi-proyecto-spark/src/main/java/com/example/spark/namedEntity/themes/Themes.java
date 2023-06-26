package com.example.spark.namedEntity.themes;
import java.io.Serializable;

public class Themes implements Serializable {
    String name;
    String category;

    public Themes(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Theme [name=" + name + "]";
    }

    public void prettyPrint() {
        System.out.println("El tema es " + this.getName() + " y su categoría es " + this.getCategory());
    }
    
}


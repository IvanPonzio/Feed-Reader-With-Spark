package com.example.spark.namedEntity.heuristic;
import com.example.spark.namedEntity.themes.Themes;



import java.util.function.Supplier;
import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public abstract class Heuristic implements Serializable {

    private static Map<String, String> categoryMap = new HashMap<>();

static {
    categoryMap.put("Microsoft", "Organization");
    categoryMap.put("Apple", "Organization");
    categoryMap.put("Google", "Organization");
    categoryMap.put("Musk", "Person");
    categoryMap.put("Biden", "Person");
    categoryMap.put("Trump", "Person");
    categoryMap.put("Messi", "Person");
    categoryMap.put("Federer", "Person");
    categoryMap.put("USA", "Place");
    categoryMap.put("Russia", "Place");
    categoryMap.put("Berlin", "Place");
    categoryMap.put("Tokyo", "Place");
    categoryMap.put("Olympics", "Event");
    categoryMap.put("WorldCup", "Event");
    categoryMap.put("iPhone", "Product");
    categoryMap.put("Tesla", "Product");
    categoryMap.put("Netflix", "Organization");
    categoryMap.put("Disney", "Organization");
    categoryMap.put("SpaceX", "Organization");
    categoryMap.put("NASA", "Organization");
    categoryMap.put("Elvis", "Person");
    categoryMap.put("Madonna", "Person");
    categoryMap.put("Einstein", "Person");
    categoryMap.put("Picasso", "Person");
    categoryMap.put("London", "Place");
    categoryMap.put("Paris", "Place");
    categoryMap.put("Rome", "Place");
    categoryMap.put("Sydney", "Place");
    categoryMap.put("Pyramids", "Place");
    categoryMap.put("GreatWall", "Place");
    categoryMap.put("SuperBowl", "Event");
    categoryMap.put("Oscars", "Event");
    categoryMap.put("Grammys", "Event");
    categoryMap.put("Samsung", "Product");
    categoryMap.put("Nike", "Product");
    categoryMap.put("Adidas", "Product");
    categoryMap.put("NewYear", "ImportantDate");
    categoryMap.put("Thanksgiving", "ImportantDate");
    categoryMap.put("Christmas", "ImportantDate");
    categoryMap.put("Halloween", "ImportantDate");
    categoryMap.put("Easter", "ImportantDate");
}


    


private static Map<String, Supplier<Themes>> themeMap = new HashMap<>();

static {
    themeMap.put("Microsoft", () -> new Themes("Technology", "Technology"));
    themeMap.put("Apple", () -> new Themes("Technology", "Technology"));
    themeMap.put("Google", () -> new Themes("Technology", "Technology"));
    themeMap.put("Musk", () -> new Themes("Technology", "Innovation"));
    themeMap.put("Biden", () -> new Themes("Politics", "international"));
    themeMap.put("Trump", () -> new Themes("Politics", "international"));
    themeMap.put("Messi", () -> new Themes("Sports", "Futbol"));
    themeMap.put("Federer", () -> new Themes("Sports", "tenis"));
    themeMap.put("USA", () -> new Themes("Place", "Geography"));
    themeMap.put("Russia", () -> new Themes("Place", "Geography"));
    themeMap.put("Berlin", () -> new Themes("Place", "Geography"));
    themeMap.put("Tokyo", () -> new Themes("Place", "Geography"));
    themeMap.put("Olympics", () -> new Themes("Sports", "event"));
    themeMap.put("WorldCup", () -> new Themes("Sports", "event"));
    themeMap.put("iPhone", () -> new Themes("Technology", "Technology"));
    themeMap.put("Tesla", () -> new Themes("Technology", "Technology"));
    themeMap.put("Netflix", () -> new Themes("Culture", "entertainment"));
    themeMap.put("Disney", () -> new Themes("Culture", "entertainment"));
    themeMap.put("SpaceX", () -> new Themes("Technology", "Innovation"));
    themeMap.put("NASA", () -> new Themes("Technology", "Science"));
    themeMap.put("Elvis", () -> new Themes("Culture", "music"));
    themeMap.put("Madonna", () -> new Themes("Culture", "music"));
    themeMap.put("Einstein", () -> new Themes("Culture", "science"));
    themeMap.put("Picasso", () -> new Themes("Culture", "art"));
    themeMap.put("London", () -> new Themes("Place", "Geography"));
    themeMap.put("Paris", () -> new Themes("Place", "Geography"));
    themeMap.put("Rome", () -> new Themes("Place", "Geography"));
    themeMap.put("Sydney", () -> new Themes("Place", "Geography"));
    themeMap.put("Pyramids", () -> new Themes("Place", "Geography"));
    themeMap.put("GreatWall", () -> new Themes("Place", "Geography"));
    themeMap.put("SuperBowl", () -> new Themes("Sports", "event"));
    themeMap.put("Oscars", () -> new Themes("Culture", "event"));
    themeMap.put("Grammys", () -> new Themes("Culture", "event"));
    themeMap.put("Samsung", () -> new Themes("Technology", "Technology"));
    themeMap.put("Nike", () -> new Themes("Business", "Fashion"));
    themeMap.put("Adidas", () -> new Themes("Business", "Fashion"));
    themeMap.put("NewYear", () -> new Themes("Culture", "Event"));
    themeMap.put("Thanksgiving", () -> new Themes("Culture", "Event"));
    themeMap.put("Christmas", () -> new Themes("Culture", "Event"));
    themeMap.put("Halloween", () -> new Themes("Culture", "Event"));
    themeMap.put("Easter", () -> new Themes("Culture", "Event"));
}

    
    public Themes getTheme(String entity) {
        Supplier<Themes> themeSupplier = themeMap.get(entity);
        return themeSupplier != null ? themeSupplier.get() : null;
    }
    

    public String getCategory(String entity) {
        return categoryMap.get(entity);
    }

   

    public abstract boolean isEntity(String word);

}

   

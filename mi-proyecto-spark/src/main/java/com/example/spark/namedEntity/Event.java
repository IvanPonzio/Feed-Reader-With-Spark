package com.example.spark.namedEntity;
import com.example.spark.namedEntity.themes.Themes;



public class Event extends NamedEntity{
    private String date;
    private Themes theme;
    private int eventFrequency;
    public static int eventCount = 0; // Public static attribute specific to Event class

    
    public Event(String name) {
        super(name, "Event");
        this.eventFrequency = 1;
        eventCount++;                 // Increment eventCount for each new Event instance
    }

    public String getEventDate() {
        return date;
    }

    public void setEventDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Themes getThemeevent() {
        return theme;
    }

    public void setThemeevent(Themes theme) {
        this.theme = theme;
    }

    public int inqEventFrequency() {
        return this.eventFrequency++;
    }

    public int getEventFrequency() {
        return eventFrequency;
    }

    public static int getEventCount() {
        return eventCount;
    }
}
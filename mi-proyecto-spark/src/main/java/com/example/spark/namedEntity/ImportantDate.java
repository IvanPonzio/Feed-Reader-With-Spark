package com.example.spark.namedEntity;
import com.example.spark.namedEntity.themes.Themes;



public class ImportantDate extends NamedEntity{
    private String real_date;
    private String date;
    private Themes theme;
    private int dateFrequency;
    public static int dateCount = 0; // Public static attribute specific to ImportantDate class

    public ImportantDate(String name) {
        super(name, "date");
        dateCount++;                 // Increment dateCount for each new ImportantDate instance
    }

    public String getRealDate() {
        return real_date;
    }

    public void setDateId(String date_id) {
        this.real_date = date_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public Themes getThemeImportantDate() {
        return theme;
    }

    public void setThemeImportanteDate(Themes theme) {
        this.theme = theme;
    }

    public int inqDateFrequency() {
        return this.dateFrequency++;
    }

    public int getDateFrequency() {
        return dateFrequency;
    }

    public static int getDateCount() {
        return dateCount;
    }
}
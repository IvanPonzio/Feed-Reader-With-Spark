package com.example.spark.parser;


import java.util.List;

/* Esta clase modela los atributos y métodos comunes a todos los distintos tipos de parser 
   existentes en la aplicación */

public abstract class GeneralParser {
    // Atributos
    protected List<String> urls; // solo pueden acceder a ellas sus subclases.

    // Constructor, toma una lista de string y la asigna al atributo urls
    public GeneralParser(List<String> urls) {
        this.urls = urls;
    }

    // Retorna la lista URLs
    public List<String> getUrls() {
        return urls;
    }

    // Modifica la lista de URLs
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    // Método para realizar el parseo, se implementa en las subclases
    public abstract void parse();
}

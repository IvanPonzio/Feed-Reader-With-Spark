package com.example.spark.parser;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import com.example.spark.subscription.SingleSubscription;
import com.example.spark.subscription.Subscription;



public class SubscriptionParser extends GeneralParser {
    // Atributos
    private List<String> urlTypes;
    private List<List<String>> urlParams;
    private Subscription subscriptions;

    // Constructor para inicializar el atributo urls de GeneralParser
    public SubscriptionParser(List<String> urls) {
        super(urls);
        urlTypes = new ArrayList<>();
        urlParams = new ArrayList<>();
    }


    
    // Método heredado de GeneralParser, implementamos a la forma que lo necesita esta clase
    public void parse() {
        
        try {
            FileReader reader = new FileReader("mi-proyecto-spark/lib/settings.json");
            
            JSONArray data = new JSONArray(new JSONTokener(reader));
            // Iterar sobre los objetos del JSONArray y obtener las URLs de los feeds
            Subscription subscription = new Subscription(null); // Crear una instancia de Subscription antes del bucle
            for (int i = 0; i < data.length(); i++) {
                JSONObject feed = data.getJSONObject(i);
                String url = feed.getString("url");
                String urlType = feed.getString("urlType");
                JSONArray urlParamsArray = feed.getJSONArray("urlParams");
                List<String> paramsList = new ArrayList<String>();
                for (int j = 0; j < urlParamsArray.length(); j++) {
                    paramsList.add(urlParamsArray.getString(j));
                }
                // Crear una instancia de SingleSubscription con los valores obtenidos
                SingleSubscription singleSubscription = new SingleSubscription(url, paramsList, urlType);

                for (String urlParam : paramsList) {
                    singleSubscription.setUrlParams(urlParam); // Agrega cada parámetro utilizando el método setUlrParams
                }
                subscription.addSingleSubscription(singleSubscription); // Agregar cada SingleSubscription a la misma instancia de Subscription
            }
            subscriptions = subscription; // Asignar la instancia de Subscription a la variable subscriptions

        } catch (FileNotFoundException e) {
            // Manejar la excepción en caso de que el archivo no se encuentre
            e.printStackTrace();
        }  catch (JSONException e) {
            // Manejar la excepción en caso de que haya un error al leer el objeto JSON
            e.printStackTrace();
        }
    }
    
    public Subscription getSubscriptions() {
        return subscriptions;
    }
    
    // Métodos getter para acceder a los valores de las claves "urlType" y "urlParams"
    public List<String> getUrlTypes() {
        return urlTypes;
    }

    public List<List<String>> getUrlParams() {
        return urlParams;
    }


    // Implementación de main para probar la clase SubscriptionParser
    public static void main(String[] args) {
        List<String> urls = new ArrayList<>();
        SubscriptionParser parser = new SubscriptionParser(urls);
        parser.parse();
        System.out.println(parser.getSubscriptions().getSubscriptionsList()); // Obtén la lista de suscripciones directamente
    } }
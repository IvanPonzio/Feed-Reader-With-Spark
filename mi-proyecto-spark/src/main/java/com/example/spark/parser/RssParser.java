package com.example.spark.parser;

import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.*;
import java.sql.Date;
import javax.xml.parsers.*;

//importar paquetes relacionados con XML
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

// importar paquetes relacionados con el modelo
import com.example.spark.feed.Article;
import com.example.spark.feed.Feed;

/* Esta clase implementa el parser de feed de tipo rss (xml)
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 * */

public class RssParser extends GeneralParser {

    private String feedStringXml;   // Atributo para almacenar el contenido del feed
    private Feed feedXml;           // Atributo para almacenar el feed parseado

    // Constructor para inicializar el atributo urls de GeneralParser
    public RssParser(List<String> urls) {
        super(urls);
    }

    // Metodo para almacenar el contenido del feed en un string
    public void setFeedContent(String feedXml) {
        this.feedStringXml = feedXml;
    }

    // Metodo para devolver la lista de articulos del feed
    public List<Article> getArticleList() {
        return feedXml.getArticleList();
    }


    // Metodo heredado de GeneralParser,
    public void parse() {
        try {
            // Crear un objeto DocumentBuilderFactory para crear un objeto DocumentBuilder
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(feedStringXml));
            Document doc = dBuilder.parse(inputSource);
            doc.getDocumentElement().normalize();

            // Crear un objeto Feed
            Feed feed = new Feed(feedStringXml);
            
            // Obtener la lista de elementos de tipo item
            NodeList nList = doc.getElementsByTagName("item");
            for (int i = 0; i < nList.getLength(); i++) {
                // Obtener el elemento item
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    // Obtener los elementos de item (title, link, description, pubDate)
                    Element eElement = (Element) nNode;
                    String title = eElement.getElementsByTagName("title").item(0).getTextContent();
                    String link = eElement.getElementsByTagName("link").item(0).getTextContent();
                    String description = eElement.getElementsByTagName("description").item(0).getTextContent();
                    String pubDateString = eElement.getElementsByTagName("pubDate").item(0).getTextContent();
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", java.util.Locale.US);
                    Date pubDate = new Date(format.parse(pubDateString).getTime());
                    // Crear un objeto Article
                    Article article = new Article(title, description, pubDate, link);
                    // Agregar el objeto Article al objeto Feed
                    feed.addArticle(article);
                }
            }
            feedXml = feed;
            if (feedStringXml == null) {
                System.err.println("El contenido del feed no ha sido establecido.");
                return; // Salir del método o manejar el error apropiadamente
            }
            
        } catch (SAXException e) {
            System.err.println("Error durante el análisis SAX del documento XML: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Error al parsear la fecha: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        } 
    }


    // Método para obtener los títulos de los artículos del feed y guardarlos en una lista
public List<String> getArticleTitles() {
    List<String> titles = new ArrayList<>();
    List<Article> articleList = feedXml.getArticleList();
    for (Article article : articleList) {
        titles.add(article.getTitle());
    }
    return titles;
}

// Método para obtener las descripciones de los artículos del feed y guardarlos en una lista
public List<String> getArticleDescriptions() {
    List<String> descriptions = new ArrayList<>();
    List<Article> articleList = feedXml.getArticleList();
    for (Article article : articleList) {
        descriptions.add(article.getText());
    }
    return descriptions;
}

// // Método para obtener las fechas de publicación de los artículos del feed y guardarlos en una lista
// public List<Date> getArticlePublicationDates() {
//     List<Date> publicationDates = new ArrayList<>();
//     List<Article> articleList = feedXml.getArticleList();
//     for (Article article : articleList) {
//         publicationDates.add(article.getPublicationDate());
//     }
//     return publicationDates;
// }

// Método para obtener los enlaces de los artículos del feed y guardarlos en una lista
public List<String> getArticleLinks() {
    List<String> links = new ArrayList<>();
    List<Article> articleList = feedXml.getArticleList();
    for (Article article : articleList) {
        links.add(article.getLink());
    }
    return links;
}



    public static void main(String[] args) {
        List<String> urls = new ArrayList<String>();
        urls.add("https://rss.nytimes.com/services/xml/rss/nyt/Business.xml");
        RssParser parser = new RssParser(urls);
        parser.parse();
    }

}

package com.example.spark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Comparator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.example.spark.namedEntity.*;
import com.example.spark.feed.Article;
import com.example.spark.feed.Feed;
import com.example.spark.httpRequest.httpRequest;
import com.example.spark.namedEntity.NamedEntity;
import com.example.spark.namedEntity.heuristic.Heuristic;
import com.example.spark.namedEntity.heuristic.QuickHeuristic;
import com.example.spark.parser.RssParser;
import com.example.spark.parser.SubscriptionParser;
import com.example.spark.subscription.Subscription;

public class FeedReaderMain implements Serializable {

    private static void printHelp() {
        System.out.println("Please, call this program in the correct way: FeedReader [-ne]");
    }

    private static List<String> getListUrlsRss() {
        List<String> listUrlsRss = new ArrayList<>();
        SubscriptionParser subscriptionParser = new SubscriptionParser(null);
        subscriptionParser.parse();
        Subscription subscription = subscriptionParser.getSubscriptions();
        subscription.setUrlsList(listUrlsRss);
        return listUrlsRss;
    }

    private static void processDefaultFeedData(List<String> listUrlsRss) {
        for (String url : listUrlsRss) {
            System.out.println("\n" + url + "\n");
            httpRequest httpRequester = new httpRequest();
            String feedXml = httpRequester.getFeedRss(url);
            RssParser rssParser = new RssParser(listUrlsRss);
            rssParser.setFeedContent(feedXml);
            rssParser.parse();
            Feed feed = new Feed("NYT\n");
            feed.setArticleList(rssParser.getArticleList());
            feed.prettyPrint();
        }
    }

    private static void processDefaultFeedDataWithNamedEntities(List<String> listUrlsRss) {
        SparkConf conf = new SparkConf().setAppName("NamedEntityRecognition").setMaster("local[*]");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        Heuristic heuristic = new QuickHeuristic();
        InvertedIndex invertedIndex = new InvertedIndex();

        JavaRDD<String> urlRDD = sparkContext.parallelize(listUrlsRss);
        sparkContext.setLogLevel("WARN");

        JavaRDD<String> feedXmlRDD = urlRDD.map(url -> {
            httpRequest httpRequester = new httpRequest();
            return httpRequester.getFeedRss(url);
        });

        feedXmlRDD.collect().forEach(feedXml -> {
            RssParser rssParser = new RssParser(listUrlsRss);
            rssParser.setFeedContent(feedXml);
            rssParser.parse();
            List<String> titles = rssParser.getArticleTitles();
            List<String> descriptions = rssParser.getArticleDescriptions();
            List<String> links = rssParser.getArticleLinks();
            for (int j = 0; j < titles.size(); j++) {
                String title = titles.get(j);
                String description = descriptions.get(j);
                String link = links.get(j);
                Date publicationDate = new Date();
                Article article = new Article(title, description, publicationDate, link);

                article.prettyPrint();
                article.computeNamedEntities(sparkContext, heuristic);
                System.out.println("Named Entities:");
                for (NamedEntity namedEntity : article.getNamedEntityList()) {
                    namedEntity.prettyPrint();
                    invertedIndex.addEntity(namedEntity.getName(), article);
                    article.setEntityCount(namedEntity.getName(), namedEntity.getFrequency());
                }
            }
        });

        System.out.println("Total per Named Entities:");
        System.out.println("People: " + Person.getPersonCount());
        System.out.println("Places: " + Place.getPlaceCount());
        System.out.println("Organizations: " + Organization.getOrganizationCount());
        System.out.println("Events: " + Event.getEventCount());
        System.out.println("Important Dates: " + ImportantDate.getDateCount());
        System.out.println("Products: " + Product.getProductCount());

        sparkContext.stop();
    }

    private static void processSearchFeedData(List<String> listUrlsRss, String searchName) {
        SparkConf conf = new SparkConf().setAppName("NamedEntityRecognition").setMaster("local[*]");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        Heuristic heuristic = new QuickHeuristic();
        InvertedIndex invertedIndex = new InvertedIndex();

        JavaRDD<String> urlRDD = sparkContext.parallelize(listUrlsRss);
        sparkContext.setLogLevel("WARN");

        JavaRDD<String> feedXmlRDD = urlRDD.map(url -> {
            httpRequest httpRequester = new httpRequest();
            return httpRequester.getFeedRss(url);
        });

        feedXmlRDD.collect().forEach(feedXml -> {
            RssParser rssParser = new RssParser(listUrlsRss);
            rssParser.setFeedContent(feedXml);
            rssParser.parse();
            List<String> titles = rssParser.getArticleTitles();
            List<String> descriptions = rssParser.getArticleDescriptions();
            List<String> links = rssParser.getArticleLinks();
            for (int j = 0; j < titles.size(); j++) {
                String title = titles.get(j);
                String description = descriptions.get(j);
                String link = links.get(j);
                Date publicationDate = new Date();
                Article article = new Article(title, description, publicationDate, link);

                article.computeNamedEntities(sparkContext, heuristic);
                for (NamedEntity namedEntity : article.getNamedEntityList()) {
                    invertedIndex.addEntity(namedEntity.getName(), article);
                    article.setEntityCount(namedEntity.getName(), namedEntity.getFrequency());
                }
            }
        });

        List<Article> matchingArticles = invertedIndex.search(searchName);
        matchingArticles.sort(Comparator.comparingInt(article -> -article.getEntityCount(searchName)));
        System.out.println("\nArticles containing the name \"" + searchName + "\":");
        for (Article article : matchingArticles) {
            article.prettyPrint();
            System.out.println("Entities in article: " + article.getEntityCount(searchName));
        }

        sparkContext.stop();
    }

    public static void main(String[] args) {
        System.out.println("************* FeedReader version 1.0 *************");
        if (args.length == 0) {
            List<String> listUrlsRss = getListUrlsRss();
            processDefaultFeedData(listUrlsRss);
        } else if (args.length == 1 && args[0].equals("-ne")) {
            List<String> listUrlsRss = getListUrlsRss();
            processDefaultFeedDataWithNamedEntities(listUrlsRss);
        } else if (args.length == 2) {
            String searchName = args[1];
            List<String> listUrlsRss = getListUrlsRss();
            processSearchFeedData(listUrlsRss, searchName);
        } else {
            printHelp();
        }
    }
}

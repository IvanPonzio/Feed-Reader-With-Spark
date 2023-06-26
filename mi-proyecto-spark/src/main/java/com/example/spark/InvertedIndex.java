package com.example.spark;
import com.example.spark.feed.Article;

import java.util.*;

public class InvertedIndex {
    private Map<String, List<Article>> invertedIndex;

    public InvertedIndex() {
        invertedIndex = new HashMap<>();
    }

    public void addEntity(String entity, Article document) {
        List<Article> articles = invertedIndex.computeIfAbsent(entity, key -> new ArrayList<>());
        articles.add(document);
       
    }

    public List<Article> search(String entity) {
        return invertedIndex.getOrDefault(entity, new ArrayList<>());
    }
    
    public int getEntityFrequency(String entity) {
    List<Article> articles = invertedIndex.getOrDefault(entity, new ArrayList<>());
    int frequency = 0;
    for (Article article : articles) {
        frequency += article.getEntityCount(entity);
    }
    return frequency;
}

    // Other methods to manipulate the inverted index, perform sorting, etc.
}

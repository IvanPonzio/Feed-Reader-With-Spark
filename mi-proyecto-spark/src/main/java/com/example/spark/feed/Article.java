package com.example.spark.feed;
import org.apache.spark.SparkConf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.spark.namedEntity.heuristic.QuickHeuristic;
import com.example.spark.namedEntity.heuristic.Heuristic;
import java.util.Arrays;
import scala.Tuple2;
import org.apache.spark.api.java.JavaPairRDD;



//import com.example.spark.namedEntity.NamedEntity;
import com.example.spark.namedEntity.*;
import com.example.spark.namedEntity.themes.Themes;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import java.io.Serializable;
import java.util.HashMap;	
import java.util.Map;


/*Esta clase modela el contenido de un articulo (ie, un item en el caso del rss feed) */

public class Article implements Serializable{
	private String title;
	private String text;
	private Date publicationDate;
	private String link;
	private List<String> keywords;
	private Map<String, Integer> entityCounts; // Map to store the count of each named entity occurrence
	
	private List<NamedEntity> namedEntityList = new ArrayList<NamedEntity>();
	
	

	public Article(String title, String text, Date publicationDate, String link) {
		super();
		this.title = title;
		this.text = text;
		this.publicationDate = publicationDate;
		this.link = link;
		this.entityCounts = new HashMap<>();
	}


	public List<NamedEntity> getNamedEntityList() {
        return namedEntityList;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setEntityCount(String entity, int count) {
		entityCounts.put(entity, count);
	}
	
	public int getEntityCount(String entity) {
        return entityCounts.getOrDefault(entity, 0);
    }

	public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }


	public void setText(String text) {
		this.text = text;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return "Article [title=" + title + ", text=" + text + ", publicationDate=" + publicationDate + ", link=" + link
				+ "]";
	}
	
	
	
	public NamedEntity getNamedEntity(String namedEntity){
		for (NamedEntity n: namedEntityList){
			if (n.getName().equals(namedEntity)){				
				return n;
			}
		}
		return null;
	}
	
	public void computeNamedEntities(JavaSparkContext sparkContext, Heuristic h) {
		String text = this.getTitle() + " " + this.getText();
	
		String charsToRemove = ".,;:()'!?\n";
		for (char c : charsToRemove.toCharArray()) {
			text = text.replace(String.valueOf(c), "");
		}
	
		// Split the text into words RDD
		JavaRDD<String> wordsRDD = sparkContext.parallelize(Arrays.asList(text.split(" ")));
	
		// Filter words that are entities
		JavaRDD<String> entityWordsRDD = wordsRDD.filter(h::isEntity);
	
		// Map each entity word to a tuple of (word, 1)
		JavaPairRDD<String, Integer> entityTupleRDD = entityWordsRDD.mapToPair(word -> new Tuple2<>(word, 1));
	
		// Reduce by key to get the count of each named entity
		JavaPairRDD<String, Integer> entityCountRDD = entityTupleRDD.reduceByKey(Integer::sum);
		
		//System.out.println("Total named entities found: " + entityCountRDD.count());

		//List<Tuple2<String, Integer>> entityCountList = entityCountRDD.collect();
		// for (Tuple2<String, Integer> entityCount : entityCountList) {
		//     System.out.println("Named Entity: " + entityCount._1() + ", Count: " + entityCount._2());
		// }

		
		List<NamedEntity> namedEntities = entityCountRDD.map(entityCount -> {
			String word = entityCount._1();
			int count = entityCount._2();
			String category = h.getCategory(word);
			if (category == null) {
				category = "Other";
			}
			NamedEntity n;
			if (category.equals("Person")) {
				Person p = new Person(word);
				p.inqPersonFrequency();
				Themes theme = h.getTheme(word);
				if (theme != null) {
					p.setTheme(theme);
				}
				n = p;
			} else if (category.equals("Organization")) {
				Organization o = new Organization(word);
				o.inqOrganizationFrequency();
				Themes theme = h.getTheme(word);
				if (theme != null) {
					o.setTheme(theme);
				}
				n = o;
			} else if (category.equals("Product")) {
				Product p = new Product(word);
				p.inqProductFrequency();
				Themes theme = h.getTheme(word);
				if (theme != null) {
					p.setTheme(theme);
				}
				n = p;
			} else if (category.equals("Event")) {
				Event e = new Event(word);
				e.inqEventFrequency();
				Themes theme = h.getTheme(word);
				if (theme != null) {
					e.setTheme(theme);
				}
				n = e;
			} else if (category.equals("Place")) {
				Place p = new Place(word);
				p.inqPlaceFrequency();
				Themes theme = h.getTheme(word);
				if (theme != null) {
					p.setTheme(theme);
				}
				n = p;
			} else if (category.equals("ImportantDate")) {
				ImportantDate d = new ImportantDate(word);
				d.inqDateFrequency();
				Themes theme = h.getTheme(word);
				if (theme != null) {
					d.setTheme(theme);
				}
				n = d;
			} else {
				n = new NamedEntity(word, category);
			}
			n.setFrequency(count);
			return n;
		}).collect();
		
		namedEntityList.addAll(namedEntities);
						
		System.out.println("Total named entities found: " + namedEntityList.size());
	}
	
	
	
	public void prettyPrint() {
		System.out.println("\n");
		System.out.println("**********************************************************************************************");
		System.out.println("Title: " + this.getTitle());
		System.out.println("Publication Date: " + this.getPublicationDate());
		System.out.println("Link: " + this.getLink());
		System.out.println("Text: " + this.getText());
		System.out.println("\n");
	
	}
	
	
	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName("NamedEntityRecognition").setMaster("local[*]");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		sparkContext.setLogLevel("WARN");

		// Crear una instancia de la heurística que desees utilizar
		Heuristic heuristic = new QuickHeuristic();
		
		// Crear un artículo de ejemplo
		String title = "Entidades Nombradas en Acción";
    	String text = "Microsoft, Apple, Google, Musk, Biden, Trump, Messi, Federer, USA, Russia, Berlin, Tokyo, Olympics, WorldCup, iPhone, Tesla, " +
            "Netflix, Disney, SpaceX, NASA, Elvis, Madonna, Einstein, Picasso, London, Paris, Rome, Sydney, Pyramids, GreatWall, SuperBowl, " +
            "Oscars, Grammys, Samsung, Nike, Adidas, NewYear, Thanksgiving, Christmas, Halloween. " +
            "Microsoft es una compañía líder en tecnología. Apple es conocida por sus productos innovadores. " +
            "Google es el motor de búsqueda más utilizado en el mundo. Musk es un visionario empresario. " +
            "Biden es el actual presidente de los Estados Unidos. Trump es un controvertido ex presidente. " +
            "Messi es un futbolista argentino reconocido a nivel mundial. Federer es un tenista legendario. " +
            "USA es un país poderoso con una influencia global. Russia es una nación con una rica historia. " +
            "Berlin es una ciudad llena de historia y cultura. Tokyo es la capital de Japón. " +
            "Olympics es un evento deportivo de gran envergadura. WorldCup es el campeonato mundial de fútbol. " +
            "iPhone es un popular smartphone de Apple. Tesla es una marca de vehículos eléctricos. " +
            "Netflix ofrece entretenimiento en línea. Disney es conocida por sus películas y parques temáticos. " +
            "SpaceX es una empresa privada de exploración espacial. NASA es la agencia espacial de Estados Unidos. " +
            "Elvis fue un icónico cantante de rock and roll. Madonna es una reconocida artista pop. " +
            "Einstein fue un brillante científico. Picasso fue un influyente pintor. " +
            "London es la capital de Inglaterra. Paris es la ciudad del amor. Rome es la ciudad eterna. " +
            "Sydney es una vibrante ciudad australiana. Pyramids son antiguas estructuras egipcias. " +
            "GreatWall es una impresionante muralla en China. SuperBowl es el evento deportivo más importante de Estados Unidos. " +
            "Oscars es una ceremonia de premios cinematográficos. Grammys es un reconocimiento a la música. " +
            "Samsung es una empresa de tecnología. Nike es una marca de moda deportiva. " +
            "Adidas es una reconocida marca de ropa y calzado. NewYear es la celebración del año nuevo. " +
            "Thanksgiving es una festividad para dar gracias. Christmas es la Navidad. Halloween es una fiesta de disfraces.";

		Date publicationDate = new Date();
		String link = "https://example.com/article";
	
		Article article = new Article(title, text, publicationDate, link);

		
		// Calcular las entidades nombradas del artículo utilizando la heurística
		article.computeNamedEntities(sparkContext, heuristic);
	
		// Imprimir el artículo y sus entidades nombradas
		article.prettyPrint();
		System.out.println("Named Entities:");
		List<NamedEntity> namedEntities = article.getNamedEntityList();
		if (namedEntities.isEmpty()) {
		    System.out.println("No named entities found.");
		} else {
			for (NamedEntity namedEntity : namedEntities) {
				namedEntity.prettyPrint();
		    }
		}
		System.out.println("People:" + Person.getPersonCount());
		System.out.println("Places:" + Place.getPlaceCount());
		System.out.println("Organizations:" + Organization.getOrganizationCount());
		System.out.println("Events:" + Event.getEventCount());
		System.out.println("Important Dates:" + ImportantDate.getDateCount());
		System.out.println("Products:" + Product.getProductCount());
		sparkContext.stop();
		sparkContext.close();
	}
	
	
}







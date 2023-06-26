#INFORME GRUPAL

## Integrantes

- Ivan Ponzio
- Fernando Alvarado
- Armando Carral

## Introducción
La parte grupal consiste en recuperar los documentos que contengan una determinada palabra o entidad nombrada, ordenados desde la que tiene mayor numero de apariciones hasta la que tiene menor numero de apariciones. Para esto se utilizo el indice invertido generado en la parte individual, el cual contiene la informacion necesaria para realizar la consulta.

Ahora veremos las desventajas y ventajas de cada map-reduce(Y donde se utilizo) en cada proyecto individual.

## Map-Reduce

Este es el Map-Reduce utilizado por Armando Carral:

```
public void computeNamedEntities(Heuristic h, JavaSparkContext sc) {
		String text = this.getTitle() + " " + this.getText();
	
		String charsToRemove = ".,;:()'!?\n";
		for (char c : charsToRemove.toCharArray()) {
			text = text.replace(String.valueOf(c), "");
		}

		// Crear RDD de palabras
		JavaRDD<String> words = sc.parallelize(Arrays.asList(text.split(" ")));
		
		// Filtrar palabras que son entidades nombradas
		JavaRDD<String> namedEntitiesRDD = words.filter(h::isEntity);
		
		// Mapear palabras a tuplas (palabra, entidad nombrada)
		// Mapeo inicial sin modificar frecuencias
		JavaPairRDD<String, NamedEntity> entities = namedEntitiesRDD.mapToPair(s -> {
			String category = h.getCategory(s);
				if (category == null) {
					category = "Other";
				}
				NamedEntity ne = this.getNamedEntity(s);
				if (ne == null) {
					NamedEntity n;
					if (category.equals("Person")) {
						Person p = new Person(s);
						p.inqPersonFrequency();
						Themes theme = h.getTheme(s);
						if (theme != null) {
							p.setTheme(theme);
						}
						n = p;
					} else if (category.equals("Organization")) {
						Organization o = new Organization(s);
						o.inqOrganizationFrequency();
						Themes theme = h.getTheme(s);
						if (theme != null) {
							o.setTheme(theme);
						}
						n = o;
					} else if (category.equals("Product")) {
						Product p = new Product(s);
						p.inqProductFrequency();
						Themes theme = h.getTheme(s);
						if (theme != null) {
							p.setTheme(theme);
						}
						n = p;
					} else if (category.equals("Event")) {
						Event e = new Event(s);
						e.inqEventFrequency();
						Themes theme = h.getTheme(s);
						if (theme != null) {
							e.setTheme(theme);
						}
						n = e;
					} else if (category.equals("Place")) {
						Place p = new Place(s);
						p.inqPlaceFrequency();
						Themes theme = h.getTheme(s);
						if (theme != null) {
							p.setTheme(theme);
						}
						n = p;
					} else if (category.equals("ImportantDate")) {
						ImportantDate d = new ImportantDate(s);
						d.inqDateFrequency();
						Themes theme = h.getTheme(s);
						if (theme != null) {
							d.setTheme(theme);
						}
						n = d;
					} else {
						n = new NamedEntity(s, category);
					}
					n.incFrequency();
					return new Tuple2<String, NamedEntity>(s, n);
				} else {
					ne.incFrequency();
					if (ne instanceof Person) {
						((Person) ne).inqPersonFrequency();
					} else if (ne instanceof Organization) {
						((Organization) ne).inqOrganizationFrequency();
					} else if (ne instanceof Product) {
						((Product) ne).inqProductFrequency();
					} else if (ne instanceof Event) {
						((Event) ne).inqEventFrequency();
					} else if (ne instanceof Place) {
						((Place) ne).inqPlaceFrequency();
					} else if (ne instanceof ImportantDate) {
						((ImportantDate) ne).inqDateFrequency();
					}
					return new Tuple2<String, NamedEntity>(s, ne);
				}
			});

			// Reducir por clave y sumar frecuencias 
			JavaPairRDD<String, NamedEntity> countedEntities = entities.reduceByKey((n1, n2) -> {
				n1.incFrequency(); 
				return n1;
			});

			// Llamar a métodos para incrementar frecuencias de tipo 
			countedEntities.foreach(tuple -> {
				NamedEntity ne = tuple._2;
				if (ne instanceof Person) { 
					((Person) ne).inqPersonFrequency(); 
				} else if (ne instanceof Organization) {
					((Organization) ne).inqOrganizationFrequency();
				} else if (ne instanceof Product) {
					((Product) ne).inqProductFrequency();
				} else if (ne instanceof Event) {
					((Event) ne).inqEventFrequency();
				} else if (ne instanceof Place) {
					((Place) ne).inqPlaceFrequency();
				} else if (ne instanceof ImportantDate) {
				    ((ImportantDate) ne).inqDateFrequency();
				}
			});

			// Recoger resultados
			namedEntityList = countedEntities.values().collect();
	}
```

El de Fernando Alvarado:

```
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
```
Y este el de Ivan Ponzio:
    
```
	public void computeNamedEntities(JavaSparkContext sc, Heuristic h) {
		String text = this.getTitle() + " " + this.getText();
	
		text = text.replaceAll("[.,;:()'!?\\n]", "");
	
		List<String> words = Arrays.asList(text.split(" "));
	
		JavaRDD<String> wordsRDD = sc.parallelize(words);
		Logger.getLogger("org").setLevel(Level.ERROR);
		Logger.getLogger("akka").setLevel(Level.ERROR);
	
		JavaPairRDD<String, NamedEntity> namedEntitiesRDD = wordsRDD.filter(h::isEntity)
				.mapToPair(s -> {
					String category = h.getCategory(s);
					if (category == null) {
						category = "Other";
					}
					NamedEntity ne = this.getNamedEntity(s);
					if (ne == null) {
						ne = createNamedEntity(s, category, h);
					}
					ne.incFrequency();
					return new Tuple2<>(s, ne);
				})
				.reduceByKey((ne1, ne2) -> {
					ne1.incFrequency();
					return ne1;
				})
				.mapToPair(tuple -> new Tuple2<>(tuple._1, tuple._2));
	
		namedEntityList = namedEntitiesRDD.values().collect();
	}
```

# Observaciones de los resultados

Logramos ver que los 3 codigos sigiuen una similitud en cuanto a la forma de realizar el trabajo, pero cada uno tiene sus diferencias en cuanto a la forma de realizarlo.

-Divide el texto en palabras y las procesa de forma paralela utilizando Spark RDD

-Filtran las palabras que son entidades nombradas utilizando la funcion filter

-Mapea cada palabra de entidad a una tupla de (palabra, entidad nombrada) utilizando la función mapToPair.

-Reduce las tuplas por clave para obtener el recuento de cada entidad nombrada utilizando la función reduceByKey.

-En algunas utilizamos una nueva funcion createdNamedEntity para crear una nueva entidad nombrada y en otras utilizamos la funcion getNamedEntity para obtener la entidad nombrada.

-Evitamos el uso de una lista local para almacenar las entidades nombradas y en su lugar utilizamos RDDs

Es decir todos siguen una logica de Map-Reduce.

Al ver que las 3 son muy parecidas elegimos la de Fernando ya que nos parecio la mas completa y la que mas se acercaba a lo que queriamos hacer.

# Implementacion del lab 3 parte 2.


Lo primero que hicimos fue implementar la clase **Inverted Index**, que contiene un mapa llamado invertedIndex que almacena las entidades como claves y una lista de articulos asociados a cada entidad como valor.
Esto nos va a permitir buscar los articulos que contienen una entidad nombrada en particular. 
Tambien creamos la funcion getEntityFrequency que nos permite obtener la frecuencia de una entidad nombrada en particular, esto nos va a ayudar mucho ya que nos va a permitir ordenar las entidades nombradas por frecuencia.

```
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
}
```
Despues en el main (ya modularizado) , creamos otra guarda para este uso, que practicamente lo que hace es llamar al metodo processSearchFeedData, pasandole la lista de URL y el serchName que es el nombre de la entidad que queremos buscar, basicamente lo que hace esa funcion se crea una instancia de InvertedIndex y a medida que se van detectando las entidades nombradas se van agregando.
Luego despues de haber procesado todos lo articulos, se realiza una busqueda en ese index, y como la frecuencia de las entidades nombradas ya esta calculada, se ordenan por frecuencia y se imprimen.

```
    List<Article> matchingArticles = invertedIndex.search(searchName);
        matchingArticles.sort(Comparator.comparingInt(article -> -article.getEntityCount(searchName)));
        System.out.println("\nArticles containing the name \"" + searchName + "\":");
        for (Article article : matchingArticles) {
            article.prettyPrint();
            System.out.println("Entities in article: " + article.getEntityCount(searchName));
        }
```

#Conclusion

En este lab pudimos ver como se puede utilizar Spark para procesar grandes cantidades de datos de forma paralela gracias a RDDs, y como utilizar map-reduce para resolver problemas de forma paralela.Tambien vimos como crear un indice invertido para poder buscar las entidades nombradas de forma mas eficiente.
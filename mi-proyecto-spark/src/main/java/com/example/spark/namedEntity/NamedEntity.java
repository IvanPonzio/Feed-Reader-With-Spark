package com.example.spark.namedEntity;

import com.example.spark.namedEntity.themes.Themes;
import java.io.Serializable;



/*Esta clase modela la nocion de entidad nombrada*/

public class NamedEntity implements Serializable {
	String name;
	String category; 
	int frequency;
	private Themes theme;

	public NamedEntity(String name, String category) {
		super();
		this.name = name;
		this.category = category;
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public void incFrequency() {
		this.frequency++;
	}

	@Override
	public String toString() {
		return "ObjectNamedEntity [name=" + name + ", frequency=" + frequency + "]";
	}
	
	public void prettyPrint() {
		String className = (this.getClass().getSimpleName().equals("NamedEntity")) ? "Otros" : this.getClass().getSimpleName();
		String themeCategory = (this.getTheme() != null && this.getTheme().getCategory() != null) ? this.getTheme().getCategory() : "Otros";
		System.out.println(this.getName() + " aparece " + this.getFrequency() + " veces, con la clase " + className + " y su tema es " + (this.getTheme() != null ? this.getTheme().getName() + " (" + themeCategory + ")" : "Otros"));
	}
	
	
	
	public void setTheme(Themes theme) {
        this.theme = theme;
    }

    public Themes getTheme() {
        return theme;
    }
	
}



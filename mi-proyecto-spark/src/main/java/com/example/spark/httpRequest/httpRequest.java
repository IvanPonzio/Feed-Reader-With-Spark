package com.example.spark.httpRequest;

import java.net.HttpURLConnection;	//Clase para solicitar un HTTP
import java.net.MalformedURLException;
import java.net.URL;				//Clase para trasformar un String un objecto URL
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/* Esta clase se encarga de realizar efectivamente el pedido de feed al servidor de noticias
 * Leer sobre como hacer una http request en java
 * https://www.baeldung.com/java-http-request
 * */

public class httpRequest {
	
	public String getFeedRss(String urlFeed){
		String feedRssXml = null;
		try{
			// Crea objeto URL
			URL feedURL = new URL(urlFeed);
			// Crear objeto HttpURLConnection
			HttpURLConnection conn = (HttpURLConnection) feedURL.openConnection();
			// Establecer el metodo de solicitud
			conn.setRequestMethod("GET");
			
			// Revisamos el codigo de respuesta para saber si la solicitud fue exitosa
			conn.setRequestProperty("Accept", "application/xml");
			int responseCode = conn.getResponseCode(); 
			// Si la respuesta es OK, entonces leemos el contenido
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null){
					response.append(inputLine);
				}
				in.close();
				feedRssXml = response.toString();
			} else {
				System.out.println("GET request not worked");
			}
		}
		catch (MalformedURLException e){
			System.out.println("Malformed URL");
		}
		catch (IOException e){
			System.out.println("IO Exception");
		}		
		return feedRssXml;
	}

	// Metodo dado
	public String getFeedReedit(String urlFeed) {
		String feedReeditJson = null;
		try {
			URL url = new URL(urlFeed);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			
			// Revisamos el codigo de respuesta para saber si la solicitud fue exitosa
			con.setRequestProperty("Accept", "application/json");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				feedReeditJson = response.toString();
			} else {
				System.out.println("GET request not worked");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return feedReeditJson;
	}

}
package com.itunesearchapi.com.rest;

import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import static com.jayway.restassured.RestAssured.*;

import java.util.HashMap;
import com.jayway.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.json.*;

public class ITuneSearchTest {
	
	static String baseUri = "https://itunes.apple.com/search?";
  
	@BeforeClass
	public void setBaseUri() {
		RestAssured.baseURI = baseUri;
	}
	
	@Test
	public void searchWithNoTermValue(){
		Response res = given().param("term","''").get();
		Assert.assertEquals (res.statusCode (), 200);
	}
	
	@Test
	public void testWithTerm(){
		Response res = given().param("term","'jack+johnson'").get();
		
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"Jack Johnson");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(50));
		Assert.assertEquals(results.get("country"),"USA");
	}
	
	@Test
	public void testWithCountry(){
		Response res = given().param("term","'A.R.Rahman'").param("country", "IN").get();
		
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"A. R. Rahman");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(50));
		Assert.assertEquals(results.get("country"),"IND");
	}
	
	
	@Test
	public void testWithMedia(){
		Response res = given().param("term", "'jack+johnson'").param("media", "movie").get();
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"Jack Johnson");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(3));
		Assert.assertEquals(results.get("country"),"USA");
		Assert.assertEquals(results.get("kind"),"feature-movie");
	}
	
	@Test
	public void testWithLimit(){
		Response res = given().param("term","'jack+johnson'").param("limit", "30").get();
		
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"Jack Johnson");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(30));
		Assert.assertEquals(results.get("country"),"USA");
	}
	
	@Test
	public void testWithMediaAndCountry(){
		Response res = given().param("term","'jack+johnson'").param("media", "movie").param("country","US").get();
		
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"Jack Johnson");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(3));
		Assert.assertEquals(results.get("country"),"USA");
		Assert.assertEquals(results.get("kind"),"feature-movie");
	}
	
	@Test
	public void testWithMediaAndLimit(){
		Response res = given().param("term","'jack+johnson'").param("media", "movie").param("limit","2").get();
		
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"Jack Johnson");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(2));
		Assert.assertEquals(results.get("country"),"USA");
		Assert.assertEquals(results.get("kind"),"feature-movie");
	}
	
	@Test
	public void testWithCountryAndLimit(){
		Response res = given().param("term","'jack+johnson'").param("country","US").param("limit","25").get();
		
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"Jack Johnson");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(25));
		Assert.assertEquals(results.get("country"),"USA");
		Assert.assertEquals(results.get("kind"),"song");
	}
	
	@Test
	public void testWithMediaAndCountryAndLimit(){
		Response res = given().param("term","'jack+johnson'").param("media", "movie").param("country","US").param("limit","2").get();
		
		String resString = res.asString().trim();
		
		HashMap<String,String> results = parseResponse(resString);
		Assert.assertEquals (res.statusCode (), 200);
		Assert.assertEquals(results.get("artistName"),"Jack Johnson");
		Assert.assertEquals(results.get("resultCount"),String.valueOf(2));
		Assert.assertEquals(results.get("country"),"USA");
		Assert.assertEquals(results.get("kind"),"feature-movie");
	}

	
	public HashMap<String, String> parseResponse(String response){
		HashMap<String,String> parsedResults = new HashMap<String,String>();
		
		try {
			JSONObject obj = new JSONObject(response);
			
			parsedResults.put("resultCount",String.valueOf(obj.getInt("resultCount")));
			JSONArray results = (JSONArray) obj.get("results");
			
			for (int i=0; i<results.length();i++){
				JSONObject result = results.getJSONObject(i);
				/*System.out.println(result.getString("kind"));
				System.out.println(result.getString("artistName"));
				System.out.println(result.getString("country"));*/
				
				parsedResults.put("kind",result.getString("kind"));
				parsedResults.put("artistName",result.getString("artistName"));
				parsedResults.put("country",result.getString("country"));
				
				if (parsedResults.size()>0)
					break;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return parsedResults;
	}
		
	
}

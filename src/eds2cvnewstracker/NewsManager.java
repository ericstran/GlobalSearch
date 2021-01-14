/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author erics
 */
public class NewsManager {
    
    private String urlString = "";
   
    // NOTE!!  The api key below is Eric Stranquist's api key. Get keys from: http://developer.nytimes.com
    // I also cannot guarantee that the api key provided will be valid in the future.
    private final String baseUrlString = "https://newsapi.org/v2/everything";
    private final String apiKey = "78af998d6b5842b79ffaaeae940e89ef";
    private String searchString = "bitcoin";
    
    private URL url;
    private ArrayList<NewsStory> newsStories;
    private JSONArray articles;
    
    public NewsManager() {
        newsStories = new ArrayList<>();
    }
    
    public JSONArray getArticles() {
        return articles;
    }
    
    public void load(String searchString) throws Exception {
        if (searchString == null || searchString.equals("")) {
            throw new Exception("The search string was empty.");
        }
        
        this.searchString = searchString;
        
        String encodedSearchString; 
        
        try {
            encodedSearchString = URLEncoder.encode(searchString, "UTF-8"); 
        } catch(UnsupportedEncodingException ex){
            throw ex; 
        }
        String settings=SettingsModel.getSettingsAsOption();
        urlString = baseUrlString + "?q=" + encodedSearchString + settings + "&apiKey=" + apiKey;
        
        try {
            url = new URL(urlString); 
        } catch(MalformedURLException muex){
            throw muex; 
        }
        
        String jsonString = ""; 
        
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));
            String inputLine; 
            
            while((inputLine = in.readLine()) != null){
                jsonString += inputLine; 
            }
            
            in.close();
            
        } catch(IOException ioex){
            throw ioex; 
        }
        
        try {
            parseJsonNewsFeed(jsonString); 
        } catch (Exception ex){
            throw ex; 
        }
    }
    
    private void parseJsonNewsFeed(String jsonString) throws Exception {
        
        newsStories.clear();
        
        if (jsonString == null || "".equals(jsonString)) return;
        
        JSONObject jsonObj;
        try {
            jsonObj = (JSONObject)JSONValue.parse(jsonString);
        } catch (Exception ex) {
            throw ex;
        }
        
        if (jsonObj == null) return;
        
        String status = "";
        try {
            status = (String)jsonObj.get("status");
        } catch (Exception ex) {
            throw ex;
        }
        
        if (status == null || !status.equals("ok")) {
            throw new Exception("Status returned from API was not ok.");
        }
        
        try {
            articles = new JSONArray();
            articles = (JSONArray)jsonObj.get("articles");
        } catch (Exception ex) {
            throw ex;
        }
      
        for (Object article : articles) {
            try {
                JSONObject story = (JSONObject)article;
                HashMap source = (HashMap)story.getOrDefault("source", "");
                String sourceId = (String)(source.getOrDefault("id",""));
                String sourceName = (String)(source.getOrDefault("name",""));
                String author = (String)story.getOrDefault("author", "");
                String title = (String)story.getOrDefault("title", "");
                String description = (String)story.getOrDefault("description", "");
                String webUrl = (String)story.getOrDefault("url", "");
                String urlToImage = (String)story.getOrDefault("urlToImage", "");
                String timestamp = (String)story.getOrDefault("publishedAt", "");
                String content = (String)story.getOrDefault("content", "");
                
                NewsStory newsStory = new NewsStory(sourceId,sourceName,author,title,description,webUrl,urlToImage,timestamp,content,(JSONObject)article);
                newsStories.add(newsStory);
                
                
            } catch (Exception ex) {
                throw ex;
            }
        }
    }
    
    public ArrayList<NewsStory> getNewsStories() {
        return newsStories;
    }
    
    public void addNewsStory(NewsStory story) {
        if(story instanceof NewsStory) {
            newsStories.add(story);
        }
    }
    
    public int getNumNewsStories() {        
        return newsStories.size();
    }  
}

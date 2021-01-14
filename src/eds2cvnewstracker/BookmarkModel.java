/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author erics
 */
public class BookmarkModel {
    private static ArrayList<String> bookmarks = new ArrayList<>();
    private static HashMap<String,JSONObject> jsonList = new HashMap<>();
    
    public static void addBookmark(String newBookmark, JSONObject jsonObj){
        bookmarks.add(newBookmark);
        jsonList.put(newBookmark,jsonObj);
    }
    
    public static void removeBookmark(String str) {
        if(bookmarks.contains(str)) {
            bookmarks.remove(str);
            jsonList.remove(str);
        }
    }
    
    public static HashMap<String,JSONObject> getJSONBookmark(String key) {
        return jsonList.get(key);
    }
    
    public static ArrayList<String> getBookmarks(){
        return bookmarks;
    }
    
    public static String sendToJSON() {
        JSONArray array = new JSONArray();
        array.addAll(jsonList.values());
        return array.toJSONString();
    }
    
    public static String initJSONBookmarks(String filePath) {
        bookmarks.clear();
        jsonList.clear();
        
        
        try {
            FileReader reader = new FileReader(filePath);
            BufferedReader in = new BufferedReader(reader);
            JSONArray articles = (JSONArray)JSONValue.parse(in);
            if(articles == null || articles.isEmpty()) {
                return "not working: articles is empty";
            }
            for(Object article : articles) {
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
                
                addBookmark(webUrl,story);
            }
        } catch(IOException e) {
            return "Failure due to IOException";
        }
        return "Success!!";
    }
}
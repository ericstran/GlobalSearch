/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

import org.json.simple.JSONObject;

/**
 *
 * @author erics
 */
public class NewsStory {
    
    public String sourceId;
    public String sourceName;
    public String author;
    public String title;
    public String description;
    public String webUrl;
    public String urlToImage;
    public String timestamp;
    public String content;
    
    public JSONObject jsonStory;
    
    public NewsStory(
            String sourceId, String sourceName, String author, String title, 
            String description, String webUrl, String urlToImage, String timestamp, 
            String content, JSONObject jsonStory) {
        
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.webUrl = webUrl;
        this.urlToImage = urlToImage;
        this.timestamp = timestamp;
        this.content = content;
        this.jsonStory = jsonStory;
    }
}

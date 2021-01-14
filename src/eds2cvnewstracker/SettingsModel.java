/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eds2cvnewstracker;

/**
 *
 * @author erics
 */
public class SettingsModel {
    private static String language = "";
    private static String domain = "";
    
    // for external use
    public static void setLanguage(String newLang) {
        switch(newLang){
            case "Arabic": language = "ar"; break;  // Arabic
            case "German": language = "de"; break;  // German
            case "English": language = "en"; break;  // English
            case "Spanish": language = "es"; break;  // Spanish
            case "French": language = "fr"; break;  // French
            case "Hebrew": language = "he"; break;  // Hebrew
            case "Italian": language = "it"; break;  // Italian
            case "Dutch": language = "nl"; break;  // Dutch
            case "Norwegian": language = "no"; break;  // Norwegian
            case "Portuguese": language = "pt"; break;  // Portuguese
            case "Russian": language = "ru"; break;  // Russian
            case "Northern Sami": language = "se"; break;  // Northern Sami
            case "Udmurt": language = "ud"; break;  // Udmurt
            case "Chinese": language = "zh"; break;  // Chinese
            default: language = ""; break;  // All
        }
    }
    
    // for external use
    public static void setDomain(String dom){
        if(dom != null || dom != "") {
            domain = dom;
        }
    }
    
    // for external use
    public static String getSettingsAsOption() {
            
        String urlOptions = formatChecker();
        return urlOptions;
        
    }
    
    // for internal use
    private static String formatChecker() {
        int counter = 0;
        String options = "", lang="",dom="";
        if(!"".equals(language)) {
            lang = "&language=" + language;
        }
        if(!"".equals(domain)) {
            dom = "&domains=" + domain;
        }
        options += lang;
        options += dom;
        
        return options;
    }
}


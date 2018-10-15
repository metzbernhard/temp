package com.eu.metz.musicModMaker;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


/**
 * @author Bernhard Metz
 * Getting info from config.json.
 */
public class InfoFromFiles {
	
	static final Logger LOGGER = Logger.getLogger(InfoFromFiles.class);

	/**
	 * Open config.json and load jsonData.
	 * @return JsonObject jsonData.
	 */
	public static JsonObject getJson() {
	   
		JsonObject json = new JsonObject();
		try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader("config.json"));
            json = jsonElement.getAsJsonObject();
        } catch (JsonSyntaxException | IOException e) {
            LOGGER.error("JsonSyntaxException while opening config.json");
            Popups.errorMessageTrace("Error", "Couldn't read config.json", e);
        }
		return json;
	}
	
	/**
	 * Get a list from jsonData. 
	 * @param json JsonData to get list from
	 * @param element which jsonList you want to get. 
	 * @return Stringlist created from Json.
	 */
	public static List<String> listFromJson(JsonObject json, String element) {
		
        JsonElement jsonArray = json.get(element).getAsJsonArray();

	    Type listType = new TypeToken<List<String>>() { }.getType();
	
	    //use gson to create a List of String from jsonArray
	    Gson gson = new Gson();
	    return gson.fromJson(jsonArray, listType);
	}
}

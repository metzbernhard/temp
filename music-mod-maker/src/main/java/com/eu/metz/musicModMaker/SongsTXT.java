package com.eu.metz.musicModMaker;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class SongsTXT {
    
    private static Logger logger = Logger.getLogger(SongsTXT.class);

    public SongsTXT() {
        super();
    }

    /**
     * Create the content for the songstxt file.
     * @param songs List of TreeItems from TreeView.
     * @return String to write to file. 
     */
    public String createContentString(ObservableList<TreeItem<SongBase>> songs) {
        
        logger.debug("Starting content for songs.txt");
        StringBuilder content = new StringBuilder();
        
        //add a song-section for every song
        for (TreeItem<SongBase> song : songs) {
            
            content.append(songStart(song.getValue().getName()));
            
            //every song has its triggers as SongBase-children in treeview
            for (TreeItem<SongBase> treeTrigger : song.getChildren()) {
                //create Part-String for every trigger
                SongTrigger trigger = (SongTrigger) treeTrigger.getValue();
                content.append(createSongTrigger(trigger));
            }
            // finish song section
            content.append("\n\t}\n}\n\n");
            
        }
        //return String for whole songs.txt file
        return content.toString();
        
    }
    

    /**
     * Create a "songs".txt file for musicmod with names and modifiers.
     * @param content to put into txt file
     * @param folderName used as name for txt file
     * 
     */
    public void writeTXTFile(String content, String folderName) {

        Path dest = Paths.get(folderName, folderName+".txt");
        logger.debug("Writing content to txt file: " + dest.toString());
        
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                new FileOutputStream(dest.toString()), "utf-8"))) {
            
            writer.write(content);
            
        } catch (IOException  e) {
            e.printStackTrace();
        }
        
    }
    
    
    /**
     * Start String for song paragraph.
     * @param name of song
     * @return String to start. 
     */
    private String songStart(String name) {
        
        String songName = name.substring(0, name.length()-4);
        String start = "song = {\n"
                        + "\tname = \"" + songName + "\"\n\t"
                        + "\n\tchance = {\n"
                        + "\t\tfactor = 1\n";
        logger.debug("Starting String for " + name);
        return start;
    }
    

    /**
     * Create modifier paragraph for every trigger.
     * @param trigger to create paragraph for
     * @return modifier paragraph for trigger.
     * @see SongsTXT#createModifier(SongTrigger)
     */
    private String createSongTrigger(SongTrigger trigger) {
        
        String modifier = "\n\t\tmodifier = {\n"
                        + "\t\t\tfactor = " + trigger.getFactor() + "\n";
        
        modifier += createModifier(trigger);
        
        return modifier;
    }
    

    /**
     * Creates middle part of modifier paragraph according to values.
     * <p>
     * Example:
     * <pre>     
     * modifier = {
     * factor = 2
     * NOT = { <i>(optional) if trigger.not = "NOT"</i>
     *  capital_scope = {
     *      continent = africa
     *  }
     * }
     * </pre>
     * @param trigger to create paragraph for.
     * @return String to insert. 
     */
    private String createModifier(SongTrigger trigger) {
        String modifier = trigger.getTrigger() + " = {\n";
        if(trigger.getModifier() == null || trigger.getModifier().equals("")) {
            modifier += "\t" + trigger.getText() + "\n}";
        } else {
            modifier += "\t" + trigger.getModifier() + "\n}";
        }
        modifier = "\t\t\t" + modifier.replace("\n", "\n\t\t\t");

        if(trigger.getNot().equals("NOT")) {
            modifier = "\t\t\tNOT = {\n" + modifier.replace("\t\t\t", "\t\t\t\t") + "\n\t\t\t}";
        }

        modifier += "\n\t\t}";

        return modifier;
        
    }


    
    

}

package com.eu.metz.musicModMaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.Logger;

public class Asset {
    
    private static Logger logger = Logger.getLogger(Asset.class);
    
    public Asset() {
        super();
    }
    
    public void writeAsset(List<File> fileList, String folderName) {
        
        // building content String for assets
        String content = buildAssetContentString(fileList);
        
        Path dest = Paths.get(folderName, "music_asset");
        logger.debug("Writing content to asset file: " + dest.toString());
        
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                new FileOutputStream(dest.toString()), "utf-8"))) {
            
            writer.write(content);
            
        } catch (IOException  e) {
            e.printStackTrace();
        }
        
    }

    private String buildAssetContentString(List<File> fileList) {
        
        StringBuilder content = new StringBuilder();        
        for (File song : fileList) {
            
            String fileName = song.getName();
            String songName = fileName.substring(0, fileName.length()-4);
            content.append("music = {\n"
                    + "\tname = \"" + songName +"\"\n" 
                    + "\tfile = \"" + fileName + "\"\n}\n");
        }
        return content.toString();
        
    }

}

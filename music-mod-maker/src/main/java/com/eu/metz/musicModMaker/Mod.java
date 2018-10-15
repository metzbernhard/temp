package com.eu.metz.musicModMaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class Mod {
    
	private static Logger logger = Logger.getLogger(Mod.class);
	private String name;
	private String folderName;
	private File folder;
	
	
	public Mod(String name) {
		
		this.name = name;
		this.folderName = name.toLowerCase().replace(" ", "_");
		this.folder = createMod();
		
	}

	
	private File createMod() {
		
		createModfile();
		return createDirectory();
		
	}
	

	private void createModfile() {
		
		String[] file = createContentandName();
		logger.debug("Creating modfile " + file[0] + " with\n" + file[1]);

		try (Writer writer = new BufferedWriter(
							new OutputStreamWriter(
							new FileOutputStream(file[0]), "utf-8"))) {
			writer.write(file[1]);
		} catch (IOException  e) {
		    e.printStackTrace();
		}
	}
	

	/**
	 * Creating Strings for content and name for $MOD.mod.
	 * @return String array with name and content.
	 */
	private String[] createContentandName() {
		String[] file = new String[2];	
		file[0] = this.folderName + ".mod";
		file[1] = "name=\"" + this.name + "\"\n"
				+ "path=\"mod/" + this.folderName + "\"\n"
				+ "tags={\n\t\"Music\"\n}\n"
				+ "supported_version=\"1.26.*.*\"";
		return file;
	}
	

	private File createDirectory() {
		
		File dir = new File(folderName);
		logger.debug("Creating Mod directory :" +dir);
		dir.mkdir();
		return dir;
		
	}


    public void save(ObservableList<TreeItem<SongBase>> songs) {
        
        List<File> songFiles = new ArrayList<File>();
        songs.forEach(item -> songFiles.add(item.getValue().getFile()));
        
        copySongs(songFiles);
        
        createAssetsFile(songFiles);
        
        createSongTXTFile(songs);
        
    }
    

    private void createAssetsFile(List<File> songFiles) {
        
        logger.debug("Creating music.asset file");
        Asset asset = new Asset();
        asset.writeAsset(songFiles, this.folderName);
        
    }


    private void createSongTXTFile(ObservableList<TreeItem<SongBase>> songs) {
        
        SongsTXT songstxt = new SongsTXT();
        
        String content = songstxt.createContentString(songs);
        songstxt.writeTXTFile(content, this.folderName);
        
    }




    /**
     * @param songFiles
     */
    private void copySongs(List<File> songFiles) {
        for (File song : songFiles) {
            Path dest = Paths.get(folderName, song.getName());
            Path src = song.toPath();
            logger.debug("Copying "+song.getName()+" from "+src+" to "+dest);
            try {
                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

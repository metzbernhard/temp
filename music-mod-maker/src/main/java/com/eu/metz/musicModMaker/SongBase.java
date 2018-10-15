package com.eu.metz.musicModMaker;

import java.io.File;
import java.util.Objects;

/**
 * @author Bernhard Metz
 * Class for Songs and Triggers. 
 */
public class SongBase {
    
	// Songname, "Trigger" for triggers
	private File file;
    private String name;
    // these are for SongTrigger objects
    // but are here to work with TableTreeView
    protected String not;
    protected String trigger;
    protected String modifier;	
    protected String text;
    protected String factor;
    
    /**
     * Basic constructor.
     * @param nameString Songname
     */
    public SongBase(File song) {
        this.name = song.getName();
        this.file = song;
        this.not = "";
    }
    
    
    /**
     * Overriding Constructor for Root.
     * @param name String
     */
    public SongBase(String name) {
    	this.name = name;
    }
    
    /*
     * Overriding equals method.
     * @return boolean if attributes equal.
     */
    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o) {
            return true;
        }
        // null and type check
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        // name comparison
        SongBase song = (SongBase) o;
        return Objects.equals(this.name, song.getName());
    }
    
    //All getter and setters for tabletreeview.

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getNot() {
		return not;
	}

	public void setNot(String not) {
		this.not = not;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFactor() {
		return factor;
	}

	public void setFactor(String factor) {
		this.factor = factor;
	}


    public File getFile() {
        return file;
    }


    public void setFile(File file) {
        this.file = file;
    }
    
    
    
}

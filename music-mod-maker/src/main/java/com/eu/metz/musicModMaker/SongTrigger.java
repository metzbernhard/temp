package com.eu.metz.musicModMaker;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Bernhard Metz
 * Trigger for Songs. 
 */
public class SongTrigger extends SongBase {
    
    /**
     * Trigger creator. 
     * @param notCheckbox 
     * @param triggerVal
     * @param modifierVal
     * @param textVal
     * @param factorVal
     */
    public SongTrigger(boolean notCheckbox, String triggerVal, String modifierVal, String textVal, String factorVal) {
        super("Trigger");
        this.trigger = triggerVal;
        this.modifier = modifierVal;
        this.text = textVal;
        this.factor = factorVal;
        //saved as string for tabletreeview representation
        if(notCheckbox) {
        	this.not = "NOT";
        } else {
        	this.not = "";
        }
        
    }
        
    /** 
     * Overriding equals method to compare attributes.
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
        // field comparison
        SongTrigger trigger = (SongTrigger) o;
        return Objects.equals(this.trigger, trigger.trigger)
        		&& Objects.equals(this.not, trigger.not)
        		&& Objects.equals(this.modifier, trigger.modifier)
        		&& Objects.equals(this.text, trigger.text)
        		&& Objects.equals(this.factor, trigger.factor);
    }
    
    /** 
     * Overriding toString for display of Trigger objects in listview.
     * @return String 
     */
    @Override
    public String toString() {
    	
    	List<String> atts = Arrays.asList(this.not, this.trigger, 
    			this.modifier, this.text, this.factor);
    	
    	String result = "";
    	
    	for(int i = 0; i < atts.size(); i++) {
    		if(!atts.get(i).equals("")) {
    			result = result + atts.get(i) + " + ";
    		}
    	}
    	return result.substring(0, result.length()-3);
    }
    
    
    
}

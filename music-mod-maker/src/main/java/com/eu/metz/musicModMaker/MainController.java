package com.eu.metz.musicModMaker;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeTableColumn;

/**
 * GUI Controller Class.
 */

/**
 * @author Bernhard Metz
 * GUI controls and most main stuff.
 */
public class MainController {
    
    private Mod mod;
    
    private Logger logger = Logger.getLogger(MainController.class);
	
	private JsonObject jsonData;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id = "saveMod"
    private MenuItem saveMod;
    
    @FXML // fx:id = "currentMod"
    private Menu currentMod;
    
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML //fx:id="triggerPane"
    private AnchorPane triggerListPane;
    
    @FXML //fx:id="triggerPane"
    private AnchorPane triggerSettingsPane;
    
    @FXML //fx:id="factorPane"
    private AnchorPane factorPane;
    
    @FXML // fx:id accordion
    private Accordion accordion;

    @FXML // fx:id="selectedSongList" 1st accordion
    private ListView<File> selectedSongList; 
    
    @FXML // fx:id="cbxTrigger" 1st accordion right
    private ComboBox<String> cbxTrigger;

    @FXML // fx:id="cbxParam" 1st accordion right
    private ComboBox<String> cbxParam;

    @FXML // fx:id="checkNot" 1st accordion right
    private CheckBox checkNot;
    
    @FXML // fx:id="checkNoTriggers" 1st accordion right
    private CheckBox checkNoTriggers;

    @FXML // fx:id="txtFactor" 1st accordion right
    private TextField txtFactor;
    
    @FXML // fx:id="textParam" 1st accordion right
    private TextField textParam;
    
    @FXML // fx:id="triggerList" 1st accordion right
    private ListView<SongTrigger> triggerList;

    @FXML // fx:id="bigSongList" 2nd accordion
    private TreeTableView<SongBase> bigSongList;
    
    @FXML
    private TreeTableColumn<?, ?> nameCol;

    @FXML
    private TreeTableColumn<?, ?> notCol;

    @FXML
    private TreeTableColumn<?, ?> triggerCol;

    @FXML
    private TreeTableColumn<?, ?> modifierCol;

    @FXML
    private TreeTableColumn<?, ?> textCol;

    @FXML
    private TreeTableColumn<?, ?> factorCol;
    
        
    /**
     * Clear TreeTableView List of Songs. 
     * @param event Mouseclick
     */
    @FXML
    void ClearBigList(ActionEvent event) {
    	bigSongList.getRoot().getChildren().clear();
    }
    

    /**
     * Clear Song Listview.
     * @param event Mouseclick
     */
    @FXML
    void clearSongSelectionList(ActionEvent event) {
    	
    	selectedSongList.getItems().clear();
    }

    
    /**
     * Remove selected song from Song Listview.
     * @param event Mouseclick.
     */
    @FXML
    void removeSong(ActionEvent event) {    	
    	File selection = selectedSongList.getSelectionModel().getSelectedItem();
    	selectedSongList.getItems().remove(selection);
    }
    

    /**
     * Remove selected song from Song TableTreeView.
     * @param event Mouseclick
     */
    @FXML
    void removeSongBigList(ActionEvent event) {
    	TreeItem<SongBase> temp = bigSongList.getSelectionModel().getSelectedItem();
		temp.getParent().getChildren().remove(temp);
    }
    

    /**
     * Opens filedialog to select songs. 
     * @param event Mouseclick
     */
    @FXML
    void selectSongs(ActionEvent event) {    	
    	List<File> songs = Popups.songSelection();
    	if(songs == null) {
    	    return;
    	}
    	
    	songs.forEach(song -> {
    			selectedSongList.getItems().add(song);
    	});
    }
    
    
    /**
     * Add SongTrigger to Triggerlist.
     * @param event Mouseclick.
     */
    @FXML
    void addTriggerToList(ActionEvent event) {
        if(checkSelection()) {
            SongTrigger trigger = createTrigger();
            clearTriggerPane();
            if(!triggerList.getItems().contains(trigger)){
                triggerList.getItems().add(trigger);
            }
        } else {
            Popups.errorMessage("Error", "Your settings are not valid");
            return;
        }
    }
    
    
    /**
     * Clear trigger pane after creating trigger.
     */
    private void clearTriggerPane() {
        checkNot.setSelected(false);
        cbxParam.setValue(null);
        txtFactor.clear();
        textParam.clear();
        textParam.setDisable(true);
    }
    
    
    @FXML
    private void newMod() {
    	String name = Popups.inputName();
    	logger.debug("Creating new Mod: " + name);
    	currentMod.setText("Selected Mod: " + name);
    	this.mod = new Mod(name);
    	saveMod.setDisable(false);
    }
    
    
    @FXML
    private void saveMod() {
        
        logger.debug("Saving Mod");
        this.mod.save(bigSongList.getRoot().getChildren());
        
    }
    

    /**
     * Adding Songs to Song TreeTableView.
     * <p>
     * Iterates through selected songs, creates SongBase Objects.
     * Adds songs to TreeTableView and Triggers as Children.  
     * @param event Mouseclick
     */
    @FXML
    void addSongs(ActionEvent event) {
        
    	//reads song and triggerlist
        List<File> songs = selectedSongList.getItems();
        List<SongTrigger> triggers = triggerList.getItems();
        if (checkListsEmpty(songs, triggers)) {
        	return;
        }
        
        //iterate through Songs, create SongBase objects
        for (File song : songs) {
        	
    		TreeItem<SongBase> temp = new TreeItem<SongBase>(new SongBase(song));
    		boolean alreadyExists = false;
    		
    		//check if song is already in TreeTableView
    		for (TreeItem<SongBase> item : bigSongList.getRoot().getChildren()){
    			if(temp.getValue().equals(item.getValue())) {
    				temp = item;
    				alreadyExists = true;
    				break;
    			}
    		}
    		
    		//create Triggers and add them to song
        	if(checkNoTriggers.isSelected()) {
        		addNoTriggers(temp);        		
        	} else {
        		addTriggers(triggers, temp);
        	}
        	
    		if(!alreadyExists) {
    			bigSongList.getRoot().getChildren().add(temp);
    		}
    		
        }
        selectedSongList.getItems().clear();
        triggerList.getItems().clear();
        bigSongList.getRoot().setExpanded(true);
    }


	/**
	 * Create "trigger" without trigger/modifier for just random play.
	 * @param temp Songbase to add Triggers to. 
	 */
	private void addNoTriggers(TreeItem<SongBase> temp) {
		
		SongTrigger trigger = new SongTrigger(false, 
				"No Trigger, just Factor", "", "", txtFactor.getText());
		temp.getChildren().add(new TreeItem<SongBase>(trigger));
	}


	/**
	 * Creates SongTriggers for all triggers and adds them to SongBase.
	 * @param triggers List of SongTriggers. 
	 * @param song SongBase to add Triggers too. 
	 */
	private void addTriggers(List<SongTrigger> triggers, TreeItem<SongBase> song) {

		for (SongTrigger trigger: triggers) {
		    song.getChildren().add(new TreeItem<SongBase>(trigger));
		}
	}
    
    
    /**
     * Check if either song or trigger list is empty, dialog if so.
     * @param songs List of Song Listview
     * @param triggers List of Trigger Listview
     * @return boolean true if one list is empty
     */
    private boolean checkListsEmpty(List<File> songs, List<SongTrigger> triggers) {
        if(songs.isEmpty()) {
            Popups.errorMessage("No Songs Selected", "Please select Songs to add");
            return true;
        }
        if(triggers.isEmpty() && !checkNoTriggers.isSelected()) {
            Popups.errorMessage("No Triggers Selected", "Please select Triggers");
            return true;
        }
        return false;
    }
    

    /**
     * Create SongTrigger object from GUI elements
     * @return SongTrigger
     */
    private SongTrigger createTrigger() {
        if(checkSelection()) {
            SongTrigger trigger = new SongTrigger(
                    checkNot.isSelected(),
                    cbxTrigger.getValue(),
                    cbxParam.getValue(),
                    textParam.getText(),
                    txtFactor.getText());
            return trigger;
        } else {
            return null;
        }
    }
   

    /**
     * Check if selected settings are valid.
     * @return true if selection is valid.
     */
    private boolean checkSelection() {
        
        boolean valid = cbxTrigger.getValue() != null 
                && !txtFactor.getText().equals("")
                && (cbxParam.getValue() != null 
                	|| !textParam.getText().equals("") 
                	|| cbxTrigger.getValue().contains("Factor"));
        return valid;
    }
    

    /**
     * Function call when noTriggerCheckbox is clicked to control GUI.
     * @param event Mouseclick
     */
    @FXML 
    void noTriggerClick(ActionEvent event) {
        
        if(checkNoTriggers.isSelected()) {            
            triggerSettingsPane.setDisable(true);
            triggerListPane.setDisable(true);
            factorPane.setDisable(false);
            txtFactor.setText("1");
        } else {
            triggerSettingsPane.setDisable(false);
            triggerListPane.setDisable(false);
        }
    }
    

    /**
     * Initializing GUI. 
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        
        bigSongList.setRoot(new TreeItem<SongBase>(new SongBase("Songs")));
    	
    	this.jsonData = InfoFromFiles.getJson();
    	
        initializeGui();
        
        setupColumns();
        
        cbxTrigger.valueProperty().addListener((obs, oldValue, newValue) -> {
            cbxTriggerListener(newValue);
        });
    }
    

    /**
     * Setting up columns and ValueFactory for TreeTableView.
     */
    private void setupColumns() {
    	
    	notCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("not"));
    	nameCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
    	triggerCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("trigger"));
    	modifierCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("modifier"));
    	textCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("text"));
    	factorCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("factor"));
		
	}

	/**
     * Several initializing routines. 
     */
    private void initializeGui() {
        
        List<String> triggers = InfoFromFiles.listFromJson(jsonData, "trigger");
        Collections.sort(triggers);
        cbxTrigger.getItems().setAll(triggers);
        
        accordion.setExpandedPane(accordion.getPanes().get(0));
        
        checkNoTriggers.setOnAction(this::noTriggerClick);
        
        textParam.setDisable(true);
        saveMod.setDisable(true);
    }
    

    /**
     * Listen to trigger Dropbox to control GUI elements. 
     * @param newValue String selected in dropbox.
     */
    private void cbxTriggerListener(String newValue) {
        List<String> modifiers = InfoFromFiles.listFromJson(jsonData, newValue);
        Collections.sort(modifiers);
        
        if(modifiers.contains("text")) {
        	textParam.setDisable(false);
        	textParam.setEditable(true);
        	cbxParam.setDisable(true);
        } else if(modifiers.contains(" No Trigger, just Factor")) {
        	cbxParam.setDisable(true);
        	textParam.setDisable(true);
        } else {
            cbxParam.getItems().setAll(modifiers);
            cbxParam.setDisable(false);
            textParam.setDisable(true);
        }
    }
}

package com.eu.metz.musicModMaker;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.TextInputDialog;

/**
 * @author Bernhard Metz
 * Dialogs for various things.
 */
public class Popups {
	
    private static final String ICO = "ico.png";
    private static final String ICON = "icon.png";
    private static final String CSS = "Popups.css";
    
    
	/**
	 * Opens dialog to select .ogg Songs. 
	 * @return FileList
	 */
	public static List<File> songSelection() {
		
        FileChooser selector = new FileChooser();
        selector.getExtensionFilters().addAll(new ExtensionFilter("ogg music file", "*.ogg"));
        selector.setTitle("Select Music");
        Stage stage = new Stage();
        List<File> list = selector.showOpenMultipleDialog(stage);

        return list;
		
	}
	
	
	 /**
     * Shows Confirmation-Dialog with given text and returns True if user pressed
     * okay.
     *
     * @param text Shown in Confirmation Dialog
     * @param header Shown in Confirmation Dialog
     * @return boolean if user hit ok
     */
    public static boolean confirmation(final String text, final String header) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Best�tigung");
        alert.setHeaderText(header);
        alert.setContentText(text);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Popups.class.getResourceAsStream(ICO)));
        alert.setGraphic(new ImageView(new Image(Popups.class.getResourceAsStream(ICON))));

        alert.getDialogPane().getStylesheets().add(Popups.class.getResource(CSS).toExternalForm());

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            return result.get() == ButtonType.OK;
        } else {
            return false;
        }
    }
    

    /**
     * Error Message for errors without exception/interesting stacktrace.
     *
     * @param header Header for Error Dialog
     * @param text Text for Error Dialog
     */
    static void errorMessage(final String header, final String text) {

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("FEHLER!");
        alert.setHeaderText(header);
        alert.setContentText(text);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Popups.class.getResourceAsStream(ICO)));
        alert.setGraphic(new ImageView(new Image(Popups.class.getResourceAsStream("error.png"))));
        alert.getDialogPane().getStylesheets().add(Popups.class.getResource(CSS).toExternalForm());
        alert.getDialogPane().setMinWidth(400);
        alert.showAndWait();
    }
    

    /**
     * Creates an Error Message including the stacktrace.
     * Also creates a log-file and saves the stacktrace to it.
     *
     * @param header Header for Error Dialog
     * @param text Text for Error Dialog
     * @param exception Exception for which the Dialog gets created
     */
    public static void errorMessageTrace(final String header, final String text, final Exception exception) {

        // create file and get stacktrace as String
        String file = errorLog(exception);
        String stacktrace = ExceptionUtils.getStackTrace(exception);

        // open a new window for the message and text
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("FEHLER!");
        alert.setHeaderText(header);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Popups.class.getResourceAsStream(ICO)));

        Label label = new Label(text + "\n\nFehlermeldung, siehe auch " + file + " :\n\n");
        //textfield for stacktrace
        TextArea txt = new TextArea(stacktrace);
        txt.setMinWidth(650);
        txt.setMinHeight(300);
        txt.setEditable(false);

        GridPane errorfield = new GridPane();
        errorfield.add(label, 1, 1);
        errorfield.add(txt, 1, 2);

        alert.getDialogPane().setContent(errorfield);
        alert.getDialogPane().getStylesheets().add(Popups.class.getResource(CSS).toExternalForm());
        alert.setGraphic(new ImageView(new Image(Popups.class.getResourceAsStream("error.png"))));
        alert.showAndWait();
    }
    
    
    /**
     * Creates a file to save Information from exception.
     * @param exception for which to save information
     * @return String fileName where the information is saved
     */
    private static String errorLog(final Exception exception) {

        String fileName = "log_" + exception.getClass().getSimpleName() + "_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS")) + ".log";
        String stacktrace = ExceptionUtils.getStackTrace(exception);

        Path path = Paths.get(fileName);
        byte[] strToBytes = stacktrace.getBytes();

        try {
            Files.write(path, strToBytes);
        } catch (IOException e1) {
            // how ironic
            return "Konnte Fehler-Logdatei nicht erstellen";

        }
        return fileName;
    }


	/**
	 * Input Dialog for Modname.
	 * @return String entered.
	 */
	public static String inputName() {
		
		TextInputDialog dialog = new TextInputDialog("Sing a long");
		dialog.setTitle("Creating a new Mod");
		dialog.setHeaderText("Modname");
		dialog.setContentText("Enter a name for your Mod:");
		
		Optional<String> result = dialog.showAndWait();
		String name = "";
		if (result.isPresent()) {
			name = result.get();
		} 
		return name;
	}

}

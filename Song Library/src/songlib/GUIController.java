package songlib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

import comparator.SongComparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @author      Sunehar Sandhu
 * @author		Nisha Bhat
 * @version     1.0
 * @since       2020-02-21
 */

public class GUIController {
	
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	
	@FXML TextField name;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year; 
	
	private int index;
	private int currentYear = Calendar.getInstance().get(Calendar.YEAR);
	
	@FXML ListView<Song> listView; 
	
	private ObservableList<Song> obsList;  
	
	//static final Song DEFAULT_SONG = new Song("Old Town Road", "Lil Nas X", "", "");	
	
	public void start(Stage mainStage) {
		
		obsList = FXCollections.observableArrayList(readFile("src/songs.txt")); 
		//obsList = FXCollections.observableArrayList();
		//obsList.add(DEFAULT_SONG);
		
		listView.setItems(obsList);
		
		if (!obsList.isEmpty())
	    	  listView.getSelectionModel().select(0);
		
		showSongDetails();
			    	    
	    // set listener for the items
	    listView
	    .getSelectionModel()
	    .selectedIndexProperty()
	    .addListener(
	    			(obs, oldVal, newVal) -> 
	    			 showSongDetails());
	    
	    
	    //Song list will be saved when program closes
	    mainStage.setOnCloseRequest(e -> saveSongs());
		
	}
	
	
	/**
	  * Selects the songs details on mouse click event 
	  * 
	  * @param arg0 UNUSED
	  */
	@FXML 
	public void handleMouseClick(MouseEvent arg0) {
		index = listView.getSelectionModel().getSelectedIndex();
		
		name.setText(obsList.get(index).getName());
		artist.setText(obsList.get(index).getArtist());
		album.setText(obsList.get(index).getAlbum());
		year.setText(obsList.get(index).getYear());
		
	}
	
	/**
	  * Handles the add button.
	  * 
	  * Lets user add a song.
	  * 
	  * @param e UNUSED
	  */
	
	public void addSong(ActionEvent e) {	
		
		String songName = name.getText();
		String artistName = artist.getText();
		String albumName = album.getText();
		String songYear = year.getText();
		
		Song newSong = new Song(songName, artistName, albumName, songYear);
		
		if ((songName.isBlank() || artistName.isBlank())) {
			popup("You must enter song name and artist");
		}
		
		else if(obsList.contains(newSong)) {	 
			popup("Cannot add duplicate songs");
			clearTextFields();
		}
		
		else if (!songYear.isBlank() && !isValid(songYear)) {
				popup("Please enter a valid year");
		}
		else {
			Alert alert = generateConfirmation("Add", "add " + songName + " - " + artistName);
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.YES) {
				obsList.add(newSong);
				listView.getSelectionModel().select(newSong);		// added song must be currently selected 
			} 
			else {
				alert.close();
			}
			
			FXCollections.sort(obsList, new SongComparator());
			clearTextFields();
		}
		
	}
	
	/**
	  * Handles the edit button.
	  * 
	  * Lets user edit a song.
	  * 
	  * @param e UNUSED
	  */
	public void editSong(ActionEvent e) {
		
		// IF SONG LIST IS EMPTY DO NOTHING
		if (listView.getItems().isEmpty())
			return;
		
		Song selectedSong = listView.getSelectionModel().getSelectedItem();
		Song editSong = new Song (name.getText(), artist.getText(), album.getText(), year.getText());
		
		
		if (name.getText().isBlank() || artist.getText().isBlank()) {
			popup("You must enter song name and artist");
		}
		else if (selectedSong.equals(editSong) && selectedSong.getAlbum().compareTo(editSong.getAlbum()) == 0 
			&& selectedSong.getYear().compareTo(editSong.getYear()) == 0) {
			popup("No edits have been made to this song");
		}
		
		else if (obsList.contains(editSong) && !selectedSong.equals(editSong)) {
			popup("Cannot duplicate songs");
		}
		
		
		else if (!editSong.getYear().isBlank() && !isValid(editSong.getYear())) {
			popup("Please enter a valid year");
		}
		
		else {
			
			Alert alert = generateConfirmation("Edit", "edit " + selectedSong.getName() + " - " + selectedSong.getArtist());
			Optional<ButtonType> result = alert.showAndWait();
		
			if (result.get() == ButtonType.YES) {
				obsList.remove(selectedSong);
				obsList.add(editSong);
				setDetails(editSong);
			} 
		
			else {
				alert.close();
			}
			
			FXCollections.sort(obsList, new SongComparator());
			clearTextFields();
		
		}
	}
	
	/**
	 * Handles the delete button.
	 * 
	 * Lets user delete a song.
	 * 
	 * @param e UNUSED
	 */
	public void deleteSong(ActionEvent e) {
		
		if (listView.getItems().isEmpty()) 
			return;
		
		Song selectedSong = listView.getSelectionModel().getSelectedItem(); 	
		Alert alert = generateConfirmation("Delete", "delete " + selectedSong.getName() + " - " + selectedSong.getArtist());
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.YES) {
			if (index < obsList.size() - 1) 
				listView.getSelectionModel().select(index + 1);
		
			obsList.remove(index); 
			if (index == obsList.size()) {
				index--;
			}
		
		    setDetails(obsList.get(index));
		} 
		else {
			alert.close();
		}
	}
	
	
	/**
     * Dialog popup 
     * 
     * @param message	Shows a dialog popup with the entered message
     */
	public void popup(String message) { 
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText(message);
		
		alert.showAndWait();
	}
	
	/**
     * Clears all text fields
     */
	private void clearTextFields() { 
		name.clear();
		artist.clear(); 
		album.clear();
		year.clear();
	}
	
	
	/**
     * Read songs from songs.txt
     * 
     * @param actionName 	name to set the action title
     * @param actionText 	name to the the action text
     * @return				Alert object 
     */ 
	private Alert generateConfirmation(String actionName, String actionText) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		confirmation.setTitle(actionName + " Confirmation Dialog");
		confirmation.setHeaderText("Confirm " + actionName);
		confirmation.setContentText("Are you sure you want to " + actionText + "?");

		confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

		return confirmation;
	}	
	
	/**
     * Set songs details.
     * 
     * Set the song details based on user input
     * 
     */
	private void setDetails(Song s) {
		
		name.setText(s.getName());
		artist.setText(s.getArtist());
		album.setText(s.getAlbum());
		year.setText(s.getYear());
	}
	
	/**
     * Show songs details.
     * 
     * Populates the song description based on the selected Song
     * 
     */
	private void showSongDetails() {
		 if (listView.getSelectionModel().getSelectedIndex() < 0)
			   return;
		 
		 Song s = listView.getSelectionModel().getSelectedItem();
		 
		 name.setText(s.getName());
		 artist.setText(s.getArtist());
		 album.setText(s.getAlbum());
		 year.setText(s.getYear());
	}
	
	
	/**
     * Save songs to text file songs.txt
     */ 
	private void saveSongs() {
		PrintWriter pw;
		
		try {
			File file = new File("src/songs.txt");
			file.createNewFile();
			pw = new PrintWriter(file);
			for (Song song : obsList) {
				pw.println(song.getName());
				pw.println(song.getArtist());
				pw.println(song.getAlbum());
				pw.println(song.getYear());
			}
			
			pw.close();
		}
		catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	
	/**
     * Read songs from songs.txt
     * 
     * @param filePath 	    File path
     * @return				ArrayList of songs
     */ 
	private ArrayList<Song> readFile(String filePath) {
		ArrayList<Song> songs = new ArrayList<>();
		BufferedReader br;
		Path path = Paths.get(filePath); 
		try {
			if (!new File(filePath).exists()) 
				return songs;
			
			br = Files.newBufferedReader(path); 
			String line = br.readLine();
			
			while (line != null) {
				String name = line;
				
				line = br.readLine(); 
				String artist = line; 
				
				line = br.readLine();
				String album = line;
				
				line = br.readLine();
				String year = line;
				
				Song s = new Song(name, artist, album, year);
				songs.add(s);
				
				line = br.readLine();
			}
		}
		catch (IOException e) {
			e.printStackTrace(); 
		}
		
		Collections.sort(songs, new SongComparator());
		return songs;
	}
	
	/**
     * Checks if year input is valid
     * 
     * @param String year		song year
     * @return 					true if year contains all digits and within valid years, else false
     */
	private boolean isValid(String year) {
		for (int i = 0; i < year.length(); i++) {
						
			if (Character.isDigit(year.charAt(i)))
				continue;
		
			else 
				return false;
		}
		
		int y = Integer.parseInt(year);
		if (y >= 1820 && y <= currentYear)
			return true;
		
		return false;
	
	}
	
}

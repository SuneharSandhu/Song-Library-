package songlib;

/**
 * @author      Sunehar Sandhu
 * @author		Nisha Bhat
 * @version     1.0
 * @since       2020-02-21
 */

public class Song {
	
	// CLASS VARIABLES
	private String name;
	private String artist;
	private String album;
	private String year;
	
	/**
	 * Song Constructor
	 * 
	 * @param name			song name
	 * @param artist		song artist	
	 * @param album			song album
	 * @param year			song year
	 */
	public Song(String name, String artist, String album, String year) {
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
	// GETTERS/SETTERS
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
	/**
	 * THE ARRAYLIST CONTAINS METHOD IMPLICITLY USES THE EQUALS METHOD FROM THE OBJECT CLASS AND 
	 * COMPARES THE INTANCE ITSELF NOT THE INSTANCE FIELDS.
	 * METHOD MUST BE OVERRIDEN FOR USE-CASE
	 */
	
	@Override
	public boolean equals(Object o) { 
	    if (!(o instanceof Song)) 
	    	return false;
	    
	    return ((Song) o).name.toLowerCase().equals(this.name.toLowerCase()) && ((Song) o).artist.toLowerCase().equals(this.artist.toLowerCase());
	}
	
	public String toString() {
		return "Name: " + name + "\n" +
			   "Artist: " + artist;
	}
	
}

package comparator;

import java.util.Comparator;

import songlib.Song;

/**
 * @author      Sunehar Sandhu
 * @author		Nisha Bhat
 * @version     1.0
 * @since       2020-02-21
 */

public class SongComparator implements Comparator<Song> {
	
	/**
	 * New comparator for songs
	 * 
	 * @param s1			first song
	 * @param s2			second song
	 * @return				less than 0 if s1 is earlier in the alphabet, else s1 appears after s2
	 */

	@Override
	public int compare(Song s1, Song s2) {
		
		if (s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase()) == 0) 
			return s1.getArtist().toLowerCase().compareTo(s2.getArtist().toLowerCase());
		
		return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
		
	}
	
	
}

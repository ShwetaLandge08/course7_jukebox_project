package course7.jukebox.songandpodcast;

import java.sql.Time;
import java.util.List;

public interface ISong {
    //method to show all the Songs
    List<Song> showAllSong();
    //method to add song to database table
    int addSongToSongTable(String songName, Time durationOfSong, String songURL, String genre, int albumId, String ArtistId);
    // method to search song by name
    List<Song> searchSongByName(List<Song> songList,String songName);
    // method t search song by artist name
    List<Song> searchSongByArtistName(List<Song> songList, String artistName);
    // method to search song by genre
    List<Song> searchSongByGenre(List<Song> songList, String genre);
    // method to search song by album name
    List<Song> searchSongByAlbumName(List<Song> songList, String albumName);
    // method to play song
    void playSong(String songName);
    // display list data in tabular format
    void displaySongList(List<Song> list);
    // method to choose search and Sort song
    void makeChoice(int choice);
}


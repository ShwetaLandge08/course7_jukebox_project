package course7.jukebox.songandpodcast;

public interface IArtistAndAlbum {
    //method to add artist
    int AddArtist(String artistName);
    // method to add Album
    int AddAlbum(String albumName);
    // method to show ArtistId
    void showAllArtist();
    // method to show album
    void showAllAlbum();
}

package course7.jukebox.playlist;

import course7.jukebox.songandpodcast.Podcast;
import course7.jukebox.songandpodcast.Song;

import java.util.List;

public interface IPlayList {
    int addToPlayListSongAndPodcast(String playlistName, int userId);
    List<Song> showListOfSongForGivenUser(String userName, int playListId);
    List <Podcast> showListOfPodcastForGivenUser(String userName, int playListId);
    List<PlayList> showPlayListForGivenUser(int userId);
    void choiceToPlaySongAndPodcast(int choice);
    int deleteFromPlaylist(String itemId, int PlaylistId);
    void displayPlayList(List<PlayList> list);
}

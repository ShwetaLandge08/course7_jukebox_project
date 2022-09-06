package course7.jukebox.songandpodcast;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IPodcast {
    // method to show all podcast
    List<Podcast> showAllPodcast();
    // method to add podcast
    int addPodcast(String podcastName, String celebrityName, String podcastPublishDate, String podcastURL);
    // method to search podcast by name
    List<Podcast> searchPodcastByName(List<Podcast> list, String podcastName);
    // method t search podcast by celebrity name
    List<Podcast> searchPodcastByCelebrityName(List<Podcast> list, String celebrityName);
    // method to search podcast by publish date
    List<Podcast> searchPodcastByPublishDate(List<Podcast> list, Date publishDate);
    // method to play song
    void playPodcast(String podcastName);
    // display list data in tabular format
    void displayPodcastList(List<Podcast> list);
    // method to choose search and Sort Podcast
    void makeChoice(int choice) throws ParseException;
}


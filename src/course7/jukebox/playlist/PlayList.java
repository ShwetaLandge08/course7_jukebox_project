package course7.jukebox.playlist;

import course7.jukebox.connection.ConnectionClass;
import course7.jukebox.songandpodcast.Podcast;
import course7.jukebox.songandpodcast.Song;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PlayList implements IPlayList{
    // Attributes
    private int playListId;
    private String playListName;

    // constructor
    public PlayList(int playListId, String playListName) {
        this.playListId = playListId;
        this.playListName = playListName;
    }
    public PlayList(){

    }

    // getters

    public int getPlayListId() {
        return playListId;
    }

    public String getPlayListName() {
        return playListName;
    }

    Scanner sc = new Scanner(System.in);

    @Override
    public int addToPlayListSongAndPodcast(String playlistName, int userId) {
        int count = 0;

        int i = addToPlayList(playlistName, userId);
        if(i == 0){
            int playListId = 0;
            try {
                String sql = "select PlayList_Id from PlayList where PlayList_Name = '" + playlistName + "'";
                //System.out.println(sql);
                Statement statement = ConnectionClass.con.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                System.out.println();
                if (resultSet.next()) {
                    playListId = resultSet.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("Enter ItemId you want to add in the list.");
            String ItemId = sc.next();
            try {
                String sql = "insert into PlayListSongAndPodcast values(" + playListId + ", '" + ItemId + "')";
                //System.out.println(sql);
                Statement statement = ConnectionClass.con.createStatement();
                count = statement.executeUpdate(sql);
                if (count == 0) {
                    System.out.println("No Song added into database Song Table.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else if(i == 1){
            int playListId = generatePlayListId();
            System.out.println("Enter ItemId you want to add in the list.");
            String ItemId = sc.next();
            try {
                String sql = "insert into PlayListSongAndPodcast values(" + playListId + ", '" + ItemId + "')";
                //System.out.println(sql);
                Statement statement = ConnectionClass.con.createStatement();
                count = statement.executeUpdate(sql);
                if (count == 0) {
                    System.out.println("No Song added into database Song Table.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    public List<Song> showListOfSongForGivenUser(String userName, int playListId) {
        List<String> itemIdList = new ArrayList<>();
        List<Song> list = new ArrayList<>();
        try{
            String sql = "select pl.Item_Id from PlayListSongAndPodcast pl join playlist p on p.PlayList_Id = pl.PlayList_Id" +
                    " where pl.Item_Id like 'S%' and pl.Playlist_Id in (select p1.playlist_Id from PlayList p1 join Users u on p1.User_Id = u.user_Id" +
                    " where u.User_Name = '" + userName + "'and pl.playlist_Id = " + playListId + ")";
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            int found = 0;
            System.out.println();
            while (resultSet.next()) {
                found = 1;
                String Item_Id = resultSet.getString(1);
                itemIdList.add(Item_Id);
            }
            if(found == 1) {
                for (String item : itemIdList) {
                    try {
                        String sql2 = "select s.Song_Name, s.Genre_Name, s.Duration_Of_Song, a.Artist_Name, al.Album_Name from Song s " +
                                "join Artist a on s.Artist_Id = a.Artist_Id join Album al on s.Ablum_Id = al.Ablum_Id where s.Song_Id = '" + item + "'";
                        Statement st = ConnectionClass.con.createStatement();
                        ResultSet rs = st.executeQuery(sql2);

                        Song song;
                        System.out.println();
                        while (rs.next()) {
                            song = new Song(rs.getString("s.Song_Name"), rs.getString("s.Genre_Name"),
                                    rs.getTime("s.Duration_Of_Song"), rs.getString("a.Artist_Name"),
                                    rs.getString("al.Album_Name"));
                            list.add(song);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if(found == 0)
                System.out.println("No Songs are Found in playlist.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Podcast> showListOfPodcastForGivenUser(String userName, int playListId) {
        List<String> itemIdList = new ArrayList<>();
        List<Podcast> list = new ArrayList<>();
        try{
            String sql = "select pl.Item_Id from PlayListSongAndPodcast pl join playlist p on p.PlayList_Id = pl.PlayList_Id" +
                    " where pl.Item_Id like 'P%' and pl.Playlist_Id in (select p1.playlist_Id from PlayList p1 join Users u on p1.User_Id = u.user_Id" +
                    " where u.User_Name = '" + userName + "'and pl.playlist_Id = " + playListId + ")";
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            int found = 0;
            System.out.println();
            while (resultSet.next()) {
                found = 1;
                String Item_Id = resultSet.getString(1);
                itemIdList.add(Item_Id);
            }
            if(found == 1) {
                for (String item : itemIdList) {
                    try {
                        String sql2 = "select Podcast_Name, Celebrity_Name, Podcast_Publish_Date from Podcast where Podcast_Id = '" + item + "'";
                        //System.out.println(sql2);
                        Statement st = ConnectionClass.con.createStatement();
                        ResultSet rs = st.executeQuery(sql2);

                        Podcast podcast;
                        System.out.println();
                        while (rs.next()) {
                            podcast = new Podcast(rs.getString("Podcast_Name"), rs.getString("Celebrity_Name"),
                                    rs.getDate("Podcast_Publish_Date"));
                            list.add(podcast);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (found == 0)
                System.out.println("No Podcast are Found in the playlist.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<PlayList> showPlayListForGivenUser(int userId) {
        List<PlayList> list = new ArrayList<>();
        try {
            String sql = "select PlayList_Id, PlayList_Name from PlayList where User_Id = " + userId;
            Statement statement = ConnectionClass.con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            int found = 0;
            PlayList playList;
            System.out.println();
            while (resultSet.next()) {
                found = 1;
                playList = new PlayList(resultSet.getInt("PlayList_Id"), resultSet.getString("PlayList_Name"));
                list.add(playList);
            }
            if (found == 0)
                System.out.println("No Playlists are Found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void choiceToPlaySongAndPodcast(int choice) {
        Scanner sc = new Scanner(System.in);
        Song song = new Song();
        Podcast podcast = new Podcast();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter the PlayList Name you want to create.");
                String playlist = sc.nextLine();
                System.out.println("Enter your UserId.");
                int user = sc.nextInt();
                int i = addToPlayListSongAndPodcast(playlist,user);
                System.out.println(i + " Items Added Successfully.");

            }
            case 2 -> {
                System.out.println("Enter the playlistId from which you want to delete song.");
                int playlistId = sc.nextInt();
                System.out.println("Enter ItemId you want to delete from list.");
                String itemId = sc.next();
                int i = deleteFromPlaylist(itemId,playlistId);
                System.out.println(i + " item deleted from list.");
            }
            case 3->{
                System.out.println("Enter the Song name you want to play from List");
                String song1 = sc.nextLine();
                song.playSong(song1);
            }
            case 4->{
                System.out.println("Enter the podcast name you want to play from list.");
                String podcast1 = sc.nextLine();
                podcast.playPodcast(podcast1);
            }
            case 5 -> {
                System.out.println("--Song List--");
                List<Song> songs = song.showAllSong();
                song.displaySongList(songs);
            }
            case 6 -> {
                System.out.println("--Podcast List--");
                List<Podcast> podcasts = podcast.showAllPodcast();
                podcast.displayPodcastList(podcasts);
            }

        }
    }

    @Override
    public int deleteFromPlaylist(String itemId, int playlistId) {
        int count = 0;
        try {
            String sql = "delete from PlayListSongAndPodcast where Item_Id = '" + itemId + "' and PlayList_Id =  " + playlistId;
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            count = statement.executeUpdate(sql);
            if (count == 0) {
                System.out.println("No item deleted from playlist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // method to add playlist
    private int addToPlayList(String PlayListName, int userId) {
        int count = 0;
        int check = checkForExistingPlayList(PlayListName, userId);
        if(check == 0) {
            int playListId = generatePlayListId() + 1;
            try {
                String sql = "insert into PlayList values(" + playListId + ", '" + PlayListName +
                        "', " + userId + ")";
                //System.out.println(sql);
                Statement statement = ConnectionClass.con.createStatement();
                count = statement.executeUpdate(sql);
                if (count == 0) {
                    System.out.println("No Song added into database Song Table.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("This PlayList is already exist for given User. You can Add songs and Podcast.");
        }
        return count;
    }

    // method to generate PlayListId
    private int generatePlayListId() {
        int playListId  = 0;
        try {
            String sql = "select max(PlayList_Id) from PlayList";
            Statement st = ConnectionClass.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                playListId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return playListId;
    }

    // method to check PlayList for given user is already exist or not
    private int checkForExistingPlayList(String PlayListName, int userId) {
        int User_Id = 0;
        try {
            String sql = "select PlayList_Name, User_Id from PlayList  where PlayList_Name = '" + PlayListName + "' and User_Id = " + userId;
            //System.out.println(sql);
            Statement st = ConnectionClass.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                User_Id = rs.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return User_Id;
    }
    @Override
    public void displayPlayList(List<PlayList> list) {
        Comparator<PlayList> comparator;
        comparator = (obj1, obj2) -> obj1.getPlayListName().compareToIgnoreCase(obj2.getPlayListName());
        list.sort(comparator);
        System.out.println("-------------------------------------");
        System.out.printf("%12s %20s", "Playlist Id", "Playlist Name");
        System.out.println();
        System.out.println("-------------------------------------");
        for (PlayList playList : list) {
            System.out.format("%12s %20s", playList.getPlayListId(), playList.getPlayListName().trim());
            System.out.println();
        }
        System.out.println("-------------------------------------");
    }
}

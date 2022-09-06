package course7.jukebox.songandpodcast;

import course7.jukebox.connection.ConnectionClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Song implements ISong {

    //Attributes
    private String songName;
    private String genre;
    private Time durationOfSong;
    private String album;
    private String artist;

    //getters
    public String getSongName() {
        return songName;
    }
    public String getGenre() {
        return genre;
    }
    public String getAlbum() {
        return album;
    }
    public String getArtist() {
        return artist;
    }

    // constructor
    public Song(String songName, String genre, Time durationOfSong, String artist, String album) {
        this.songName = songName;
        this.genre = genre;
        this.durationOfSong = durationOfSong;
        this.artist = artist;
        this.album = album;
    }

    public Song() {

    }

    @Override
    public List<Song> showAllSong() {
        List<Song> list = new ArrayList<>();
        try {
            String sql = "select s.Song_Name, s.Genre_Name, s.Duration_Of_Song, a.Artist_Name, al.Album_Name from Song s " +
                    "join Artist a on s.Artist_Id = a.Artist_Id join Album al on s.Ablum_Id = al.Ablum_Id";
            Statement statement = ConnectionClass.con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            int found = 0;
            Song song;
            System.out.println();
            while (resultSet.next()) {
                found = 1;
                song = new Song(resultSet.getString("s.Song_Name"), resultSet.getString("s.Genre_Name"),
                        resultSet.getTime("s.Duration_Of_Song"), resultSet.getString("a.Artist_Name"),
                        resultSet.getString("al.Album_Name"));
                list.add(song);
            }
            if (found == 0)
                System.out.println("No Songs are Found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int addSongToSongTable(String songName, Time durationOfSong, String songURL, String genre, int albumId, String ArtistId) {
        int count = 0;
        String songId = generateSongId();
        try {
            String sql = "insert into Song values('" + songId + "', '" + songName + "', '" + durationOfSong + "', '" + songURL + "', '" +
                    genre + "', " + albumId + ", '" + ArtistId + "')";
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            count = statement.executeUpdate(sql);
            if (count == 0) {
                System.out.println("No Song added into database Song Table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<Song> searchSongByName(List<Song> songList, String songName) {
        Comparator<Song> comparator;
        comparator = (obj1, obj2) -> obj1.getSongName().compareToIgnoreCase(obj2.getSongName());
        Predicate<Song> predicate = s -> (songName.equalsIgnoreCase(s.getSongName()));
        return songList.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Song> searchSongByArtistName(List<Song> list, String artistName) {
        Comparator<Song> comparator;
        comparator = (obj1, obj2) -> obj1.getSongName().compareToIgnoreCase(obj2.getSongName());
        Predicate<Song> predicate = s -> (artistName.equalsIgnoreCase(s.getArtist()));
        return list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Song> searchSongByGenre(List<Song> list, String genre) {
        Comparator<Song> comparator;
        comparator = (obj1, obj2) -> obj1.getSongName().compareToIgnoreCase(obj2.getSongName());
        Predicate<Song> predicate = s -> (genre.equalsIgnoreCase(s.getGenre()));
        return list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Song> searchSongByAlbumName(List<Song> list, String albumName) {
        Comparator<Song> comparator;
        comparator = (obj1, obj2) -> obj1.getSongName().compareToIgnoreCase(obj2.getSongName());
        Predicate<Song> predicate = s -> (albumName.equalsIgnoreCase(s.getAlbum()));
        return list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public void playSong(String songName) {
        Scanner sc = new Scanner(System.in);
        try {
            String sql = "select Song_URL from Song where Song_Name = '" + songName + "'";
            Statement statement = ConnectionClass.con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                String songUrl = resultSet.getString(1);
                //System.out.println(songUrl);
                PlaySong audioPlayer = new PlaySong(songUrl);

                audioPlayer.play();

                while (true) {
                    System.out.println("1. pause");
                    System.out.println("2. resume");
                    System.out.println("3. restart");
                    System.out.println("4. stop");
                    System.out.println("5. Jump to specific time");
                    System.out.println("Enter your choice.");
                    int c = sc.nextInt();
                    audioPlayer.gotoChoice(c);
                    if (c == 4)
                        break;
                }
            }
            else{
                System.out.println("You have entered wrong song Name. please enter again.");
                String song = sc.nextLine();
                playSong(song);
            }
            //sc.close();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    @Override
    public void displaySongList(List<Song> list) {
        Comparator<Song> comparator;
        comparator = (obj1, obj2) -> obj1.getSongName().compareToIgnoreCase(obj2.getSongName());
        list.sort(comparator);
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.printf("%20s %15s %10s %15s %20s", "Song Name", "Genre", "Duration", "Artist Name", "Album Name");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------");
        for (Song song : list) {
            System.out.format("%20s %15s %10s %15s %20s", song.songName.trim(), song.genre.trim(), song.durationOfSong, song.artist.trim(), song.album.trim());
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------------------------");
    }

    @Override
    public void makeChoice(int choice) {
        Scanner sc = new Scanner(System.in);
        List<Song> songs = showAllSong();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter the song you want to play from list.");
                String song = sc.nextLine();
                playSong(song);
            }
            case 2 -> {
                System.out.println("Enter the name of song you want to search.");
                String songName = sc.nextLine();
                List<Song> songs1 = searchSongByName(songs, songName);
                displaySongList(songs1);
            }
            case 3 -> {
                System.out.println("Enter the genre of song you want to search.");
                String genreName = sc.nextLine();
                List<Song> songs2 = searchSongByGenre(songs, genreName);
                displaySongList(songs2);
            }
            case 4 -> {
                System.out.println("Enter the artist of song you want to search.");
                String artistName = sc.nextLine();
                List<Song> songs3 = searchSongByArtistName(songs, artistName);
                displaySongList(songs3);
            }
            case 5 -> {
                System.out.println("Enter the album of song you want to search.");
                String albumName = sc.nextLine();
                List<Song> songs4 = searchSongByAlbumName(songs, albumName);
                displaySongList(songs4);
            }
            //default -> System.out.println("You have entered Wrong option.");
        }

    }

    // method to play song
    private String generateSongId() {
        String songId;
        Random random = new Random();
        int randomNumber = random.nextInt(9999);
        songId = "S" + randomNumber;
        return songId;
    }
}



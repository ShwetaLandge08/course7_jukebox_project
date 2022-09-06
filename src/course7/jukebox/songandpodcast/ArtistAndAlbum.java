package course7.jukebox.songandpodcast;

import course7.jukebox.connection.ConnectionClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class ArtistAndAlbum implements IArtistAndAlbum {
    private String albumName;
    private String artistName;

    public ArtistAndAlbum(){

    }
    public String getAlbumName() {
        return albumName;
    }
    public String getArtistName() {
        return artistName;
    }
    @Override
    public int AddArtist(String artistName) {
        int count = 0;
        String artistId = generateArtistId();
        try {
            String sql = "insert into Artist values('" + artistId + "', '" + artistName + "')";
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            count = statement.executeUpdate(sql);
            if (count == 0) {
                System.out.println("No Artist added into database Artist Table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public int AddAlbum(String albumName) {
        int count = 0;
        int albumId = getMaxAlbumId() + 1;
        try {
            String sql = "insert into Album values('" + albumId + "', '" + albumName + "')";
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            count = statement.executeUpdate(sql);
            if (count == 0) {
                System.out.println("No Album added into database Album Table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void showAllArtist(){
        try {
            String sql = "select * from Artist";
            Statement st = ConnectionClass.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int found = 0;
            System.out.format("%20s %20s", "ArtistId", "Artist Name");
            while(rs.next()) {
                found = 1;
                System.out.format("%20s %20s", rs.getString(1), rs.getString(2));
            }
            if (found == 0)
                System.out.println("No Artist are Found.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showAllAlbum(){
        try {
            String sql = "select * from Album";
            Statement st = ConnectionClass.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int found = 0;
            System.out.format("%20s %20s", "AlbumId", "Album Name");
            while(rs.next()) {
                found = 1;
                System.out.format("%20s %20s", rs.getString(1), rs.getString(2));
            }
            if (found == 0)
                System.out.println("No Album are Found.");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* // search album by album name
     public String searchAlbumByName(String albumName) {
         List<ArtistAndAlbum> list = new ArrayList<>();
         try {
             String sql = "select * from Album";
             Statement st = ConnectionClass.con.createStatement();
             ResultSet rs = st.executeQuery(sql);
             int found = 0;
             while(rs.next()) {
                 found = 1;
                ArtistAndAlbum artistAndAlbum = new ArtistAndAlbum(rs.getString("Ablum_Id"), rs.getString("Album_Name"));
                list.add(artistAndAlbum);
             }
             if (found == 0)
                 System.out.println("No Album are Found.");
         }
         catch (SQLException e) {
             e.printStackTrace();
         }
         Predicate<ArtistAndAlbum> predicate = s -> (albumName.equalsIgnoreCase(s.getAlbumName()));
         return list.stream().filter(predicate).toString();
     }*/
    // method to generate ArtistId
    private String generateArtistId() {
        String artistId;
        Random random = new Random();
        int randomNumber = random.nextInt(9999);
        artistId = "A" + randomNumber;
        return artistId;
    }

    // method to get Max AlbumId
    int getMaxAlbumId() {
        int albumId  = 0;
        try {
            String sql = "select max(Ablum_Id) from Album";
            Statement st = ConnectionClass.con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if(rs.next()) {
                albumId = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return albumId;
    }
}

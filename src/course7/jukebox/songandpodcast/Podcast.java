package course7.jukebox.songandpodcast;

import course7.jukebox.connection.ConnectionClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Podcast implements IPodcast {
    // Attributes
    private String podcastName;
    private String celebrityName;
    private Date podcastPublishDate;

    // getter

    public String getPodcastName() {
        return podcastName;
    }
    public String getCelebrityName() {
        return celebrityName;
    }
    public Date getPodcastPublishDate() {
        return podcastPublishDate;
    }

    // constructor
    public Podcast(String podcastName, String celebrityName, Date podcastPublishDate) {
        this.podcastName = podcastName;
        this.celebrityName = celebrityName;
        this.podcastPublishDate = podcastPublishDate;
    }
    public Podcast(){

    }
    @Override
    public List<Podcast> showAllPodcast() {
        List<Podcast> list = new ArrayList<>();
        try {
            String sql = "select Podcast_Name, Celebrity_Name, Podcast_Publish_Date from Podcast";
            Statement statement = ConnectionClass.con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            int found = 0;
            Podcast podcast;
            while (resultSet.next()) {
                found = 1;
                podcast = new Podcast(resultSet.getString("Podcast_Name"), resultSet.getString("Celebrity_Name"),
                        resultSet.getDate("Podcast_Publish_Date"));
                list.add(podcast);
            }
            if (found == 0)
                System.out.println("No Podcast are Found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int addPodcast(String podcastName, String celebrityName, String podcastPublishDate, String podcastURL) {
        int count = 0;
        String podcastId = generatePodcastId();
        try {
            String sql = "insert into Podcast values('" + podcastId + "', '" + podcastName + "', '" + celebrityName + "', '" + podcastPublishDate +
                    "', '" + podcastURL + "')";
            //System.out.println(sql);
            Statement statement = ConnectionClass.con.createStatement();
            count = statement.executeUpdate(sql);
            if (count == 0) {
                System.out.println("No Podcast added into database Podcast Table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<Podcast> searchPodcastByName(List<Podcast> list, String podcastName) {
        Comparator<Podcast>comparator;
        comparator = (obj1, obj2) -> obj1.getPodcastName().compareToIgnoreCase(obj2.getPodcastName());
        Predicate<Podcast> predicate = s -> (podcastName.equalsIgnoreCase(s.getPodcastName()));
        return list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Podcast> searchPodcastByCelebrityName(List<Podcast> list, String celebrityName) {
        Comparator<Podcast>comparator;
        comparator = (obj1, obj2) -> obj1.getCelebrityName().compareToIgnoreCase(obj2.getCelebrityName());
        Predicate<Podcast> predicate = s -> (celebrityName.equalsIgnoreCase(s.getCelebrityName()));
        return list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Podcast> searchPodcastByPublishDate(List<Podcast> list, Date publishDate) {
        Comparator<Podcast>comparator;
        comparator = Comparator.comparing(Podcast::getPodcastPublishDate);
        Predicate<Podcast> predicate = s -> (publishDate.equals(s.getPodcastPublishDate()));
        return list.stream().filter(predicate).sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public void playPodcast(String podcastName) {
        Scanner sc = new Scanner(System.in);
        try {
            String sql = "select Podcast_URL from Podcast where Podcast_Name = '" + podcastName + "'";
            Statement statement = ConnectionClass.con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                String podcastURL = resultSet.getString(1);
                //System.out.println(songUrl);
                PlaySong audioPlayer = new PlaySong(podcastURL);

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
            else {
                System.out.println("You have entered wrong Podcast Name. please enter again.");
                String podcast = sc.nextLine();
                playPodcast(podcast);
            }
            //sc.close();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    @Override
    public void displayPodcastList(List<Podcast> list) {
        Comparator<Podcast> comparator;
        comparator = (obj1, obj2) -> obj1.getPodcastName().compareToIgnoreCase(obj2.getPodcastName());
        list.sort(comparator);
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.printf("%25s %30s %15s", "Podcast Name", "Celebrity name", "Publish Date");
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------");
        for (Podcast podcast : list) {
            System.out.format("%25s %30s %15s",podcast.podcastName.trim(), podcast.celebrityName.trim(),podcast.podcastPublishDate);
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------------------------------");
    }

    @Override
    public void makeChoice(int choice) throws ParseException {
        Scanner sc = new Scanner(System.in);
        List<Podcast> list = showAllPodcast();
        switch (choice) {
            case 1 -> {
                System.out.println("Enter the Podcast you want to play from list.");
                String podcast = sc.nextLine();
                playPodcast(podcast);
            }
            case 2 -> {
                System.out.println("Enter the Name of Podcast you want to search.");
                String podcastName = sc.nextLine();
                List<Podcast> podcasts = searchPodcastByName(list, podcastName);
                displayPodcastList(podcasts);
            }
            case 3 -> {
                System.out.println("Enter the Celebrity Name of Podcast you want to search.");
                String celebrity = sc.nextLine();
                List<Podcast> podcasts = searchPodcastByCelebrityName(list, celebrity);
                displayPodcastList(podcasts);
            }
            case 4 -> {
                System.out.println("Enter the Publish date of Podcast you want to search.");
                Date date = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String input = sc.nextLine();
                if(null!= input && input.trim().length() > 0){
                    date = format.parse(input);
                }
                List<Podcast> podcasts = searchPodcastByPublishDate(list, date);
                displayPodcastList(podcasts);
            }
            //default -> System.out.println("You have entered Wrong option.");
        }

    }

    // method to generate podcastId
    private String generatePodcastId() {
        String podcastId;
        Random random = new Random();
        int randomNumber = random.nextInt(9999);
        podcastId = "P" + randomNumber;
        return podcastId;
    }
}
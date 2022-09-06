package course7.jukebox.main;

import course7.jukebox.playlist.PlayList;
import course7.jukebox.songandpodcast.Podcast;
import course7.jukebox.songandpodcast.Song;
import course7.jukebox.userlogin.User;

import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class DisplayChoice {
    Scanner sc = new Scanner(System.in);
    Song song = new Song();
    User user = new User();
    Podcast podcast = new Podcast();
    PlayList playList = new PlayList();

    public void displayChoice1() throws ParseException {

        int num;
        while (true) {
            System.out.println("Lets have Fun. Choose what you want.");
            System.out.println("1. Show Song List.");
            System.out.println("2. Show Podcast List.");
            System.out.println("3. Explore Song.");
            System.out.println("4. Explore Podcast.");
            System.out.println("5. Create Playlist and Add items to playlist.");
            System.out.println("6. Stop Application.");
            System.out.println("Enter your choice.");
            num = sc.nextInt();
            setChoice(num);
            if(num == 6)
                break;
        }
    }
    public void displayChoice2(){
        System.out.println("Enter Your Username.");
        String userName = sc.nextLine();
        System.out.println("Enter your Password");
        String password = sc.nextLine();
        int i = user.checkForExistingUser(userName, password);
        if(i == 0){
            System.out.println("This User is Not present.");
        }
        else{
            // show list of sng and podcast added by user
            System.out.println("You have log in Successfully.");
            System.out.println("--Your Playlist--");
            List<PlayList> playLists = playList.showPlayListForGivenUser(i);
            playList.displayPlayList(playLists);
            System.out.println("Which Playlist you want to see. Enter Id of that Playlist.");
            int playListId = sc.nextInt();
            System.out.println("--Song List--");
            List<Song> songs = playList.showListOfSongForGivenUser(userName,playListId);
            song.displaySongList(songs);
            System.out.println("--Podcast List--");
            List<Podcast> podcasts = playList.showListOfPodcastForGivenUser(userName,playListId);
            podcast.displayPodcastList(podcasts);
            while(true) {
                System.out.println("1. Add item to playlist");
                System.out.println("2. Delete item from Playlist.");
                System.out.println("3. Play Song  from list.");
                System.out.println("4. Play Podcast from list.");
                System.out.println("5. Display all Songs.");
                System.out.println("6.Display all Podcast.");
                System.out.println("7. Stop Application");
                System.out.println("Enter your choice.");
                int choice = sc.nextInt();
                playList.choiceToPlaySongAndPodcast(choice);
                if(choice == 7)
                    break;
            }
        }
    }
    private void setChoice(int ch) throws ParseException {
        switch (ch){
            case 1->{
                System.out.println("--Song List--");
                List<Song> songs = song.showAllSong();
                song.displaySongList(songs);
            }
            case 2->{
                System.out.println("--Podcast List--");
                List<Podcast> podcasts = podcast.showAllPodcast();
                podcast.displayPodcastList(podcasts);
            }
            case 3->{
                while(true) {
                    System.out.println("1. Play Song.");
                    System.out.println("2. Search Song by Song Name.");
                    System.out.println("3. Search Song by Genre Name.");
                    System.out.println("4. Search Song by Artist Name.");
                    System.out.println("5. Search Song by Album Name.");
                    System.out.println("6. Stop Search.");
                    System.out.println("Enter your choice.");
                    int c = sc.nextInt();
                    song.makeChoice(c);
                    if (c == 6)
                        break;
                }
            }
            case 4-> {
                while(true) {
                    System.out.println("1. Play Podcast.");
                    System.out.println("2. Search Podcast by Name.");
                    System.out.println("3. Search Podcast by Celebrity Name.");
                    System.out.println("4. Search Podcast by Publish date.");
                    System.out.println("5. Stop Search.");
                    System.out.println("Enter your choice.");
                    int c = sc.nextInt();
                    podcast.makeChoice(c);
                    if (c == 5)
                        break;
                }
            }
            case 5-> {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the PlayList Name you want to create.");
                String playlist = scanner.nextLine();
                System.out.println("Enter your UserId.");
                int user = scanner.nextInt();
                int i = playList.addToPlayListSongAndPodcast(playlist, user);
                System.out.println(i + " Item added to your Playlist.");
            }
            case 6 -> sc.close();
            default-> {
                System.out.println("You have enter wrong number. please enter the right one.");
                int nu = sc.nextInt();
                setChoice(nu);
            }
        }
    }
}

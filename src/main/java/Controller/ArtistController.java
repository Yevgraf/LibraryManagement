package Controller;

import Data.ArtistData;
import Model.Artist;

import java.util.List;

public class ArtistController {
    private ArtistData artistData;

    public ArtistController() {
        artistData = new ArtistData();
    }

    public void createArtist(String name) {
        Artist artist = new Artist(name);
        artistData.save(List.of(artist));
    }

    public List<Artist> listArtists() {
        return artistData.listArtists();
    }
}

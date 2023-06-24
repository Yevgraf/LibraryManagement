package Model;

import java.time.LocalDate;

public class CD {
    private int id;
    private String title;
    private Artist artist;
    private int releaseYear;
    private int numTracks;
    private Category category;
    private int quantity;



    // Constructor
    public CD(int id, String title, Artist artist, int releaseYear, int numTracks, Category category, int quantity) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.numTracks = numTracks;
        this.category = category;
        this.quantity = quantity;
    }

    public CD(int cdId, String cdTitle) {
        this.id = cdId;
        this.title = cdTitle;
    }



    public CD(String title, Artist artist, int releaseYear, int numTracks, Category category, int quantity) {
        this.title = title;
        this.artist = artist;
        this.releaseYear = releaseYear;
        this.numTracks = numTracks;
        this.category = category;
        this.quantity = quantity;
    }

    public CD() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CD{" + '\n' +
                "id: " + id + '\n' +
                "title: '" + title + '\'' + '\n' +
                "artist: " + artist + '\n' +
                "releaseYear: " + releaseYear + '\n' +
                "numTracks: " + numTracks + '\n' +
                "category: " + category + '\n' +
                "quantity: " + quantity + '\n' +
                '}';
    }
}

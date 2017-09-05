package model;

public class Merchandise {
    private String name;
    private int numOfSongs;
    private String thumbnail;

    public Merchandise() {
    }

    public Merchandise(String name, int numOfSongs, String thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}


/**
 * Autchor: chong.
 */
//public class Merchandise {
//    private String name;
//    private int numOfSongs;
//    private int thumbnail;
//
//    public Merchandise() {
//    }
//
//    public Merchandise(String name, int numOfSongs, int thumbnail) {
//        this.name = name;
//        this.numOfSongs = numOfSongs;
//        this.thumbnail = thumbnail;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getNumOfSongs() {
//        return numOfSongs;
//    }
//
//    public void setNumOfSongs(int numOfSongs) {
//        this.numOfSongs = numOfSongs;
//    }
//
//    public int getThumbnail() {
//        return thumbnail;
//    }
//
//    public void setThumbnail(int thumbnail) {
//        this.thumbnail = thumbnail;
//    }
//}



package model;

import java.util.ArrayList;

/**
 * Created by Chong on 10/2/2017.
 */

public class HomePageBean {

    private String name;
    private int numOfSongs;
    private String thumbnail;
    private int id;
    private ArrayList<String> banner_urls;

    private int view_type;

    public HomePageBean() {
    }


    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<String> getBanner_urls() {
        return banner_urls;
    }

    public void setBanner_urls(ArrayList<String> banner_urls) {
        this.banner_urls = banner_urls;
    }


}

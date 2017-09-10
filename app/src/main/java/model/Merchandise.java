package model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class Merchandise implements Serializable {
    private String name;
    private int numOfSongs;
    private String thumbnail;
    private int id;

    private static final String JSON_ID = "id";
    private static final String JSON_NAME = "name";
    private static final String JSON_NUMOFSONGS = "numOfSongs";
    private static final String JSON_THUMBNAIL = "thumbnail";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Merchandise(JSONObject json) throws JSONException {
        id = json.getInt(JSON_ID);
        name = json.getString(JSON_NAME);
        numOfSongs = json.getInt(JSON_NUMOFSONGS);
        thumbnail = json.getString(JSON_THUMBNAIL);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, id);
        json.put(JSON_NAME, name);
        json.put(JSON_NUMOFSONGS, numOfSongs);
        json.put(JSON_THUMBNAIL, thumbnail);
        return json;
    }
}



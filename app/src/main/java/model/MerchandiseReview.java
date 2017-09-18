package model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by chong on 9/14/2017.
 */

public class MerchandiseReview {

    private String avatar;
    private String name;
    private String time;
    private String review;
    private int rate_star;

    public MerchandiseReview() {
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRateStar() {
        return rate_star;
    }

    public void setRateStar(int rate_star) {
        this.rate_star = rate_star;
    }
}


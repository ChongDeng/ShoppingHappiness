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
    private ArrayList<BannerBean> banner_beans;
    private ArrayList<CommonFunctionBean> common_func_beans;
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

    public ArrayList<BannerBean> getBannerBeans() {
        return banner_beans;
    }

    public void setBannerBeans(ArrayList<BannerBean> banner_beans) {
        this.banner_beans = banner_beans;
    }


    public ArrayList<CommonFunctionBean> getCommonFuncBeans() {
        return common_func_beans;
    }

    public void setCommonFuncBeans(ArrayList<CommonFunctionBean> common_func_beans) {
        this.common_func_beans = common_func_beans;
    }

    public class BannerBean{
        String url;
        String target_data;
        String title;

        public BannerBean() {

        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTargetData() {
            return target_data;
        }

        public void setTargetData(String target_data) {
            this.target_data = target_data;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    public class CommonFunctionBean{
        String pic_url;
        String pic_description;
        String target_data;

        public CommonFunctionBean(){

        }

        public String getPicUrl() {
            return pic_url;
        }

        public void setPicUrl(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getPicDescription() {
            return pic_description;
        }

        public void setPicDescription(String pic_description) {
            this.pic_description = pic_description;
        }

        public String getTargetData() {
            return target_data;
        }

        public void setTargetData(String target_data) {
            this.target_data = target_data;
        }


    }
}

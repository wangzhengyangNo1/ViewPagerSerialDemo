package com.wzhy.viewpagerserial.banner;

public class BannerEntity implements Cloneable {
    private int id;
    private String imgUrl;
    private String desc;

    public BannerEntity() {
    }

    public BannerEntity(int id, String imgUrl, String desc) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    protected BannerEntity clone() {
        return new BannerEntity(getId(), getImgUrl(), getDesc());
    }
}

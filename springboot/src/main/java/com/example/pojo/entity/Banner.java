package com.example.pojo.entity;

import java.io.Serializable;

public class Banner implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    private String imgUrl;

    public Banner() {
    }

    @Override
    public String toString() {
        return "Banner{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }

    public Banner(Integer id, String imgUrl) {
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

package com.plancrazyappfrontofficespring.model;

import jakarta.persistence.*;

@Entity
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="picture_id", nullable = false, unique = true)
    private Long pictureId;

    @Column(nullable = false, unique = true)
    private String url;

    Picture() {

    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

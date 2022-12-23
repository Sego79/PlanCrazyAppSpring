package com.plancrazyappfrontofficespring.controller.dto;

import com.plancrazyappfrontofficespring.model.AppUser;

public class AppUserDto {

    private Long appUserId;

    private String nickname;

    private String firstName;

    private String lastName;

    private String address;

    private Integer postcode;

    private String city;

    private String phoneNumber;

    private String email;

    private String password;

//    private Boolean isActive = true;
//
//    private Boolean isAdmin = false;
//
//    private Boolean isSuperAdmin = false;
//
//    private List<Picture> pictureList = new ArrayList<>();
//
//    private List<UserTaskAssociation> UserTaskAssociationList = new ArrayList<>();

    public AppUserDto() {
    }

    public static AppUserDto transformIntoDto(AppUser g) {
        AppUserDto dto = new AppUserDto();
        dto.setAppUserId(g.getAppUserId());
        dto.setNickname(g.getNickname());
        dto.setFirstName(g.getFirstName());
        dto.setLastName(g.getLastName());
        dto.setAddress(g.getAddress());
        dto.setPostcode(g.getPostcode());
        dto.setCity(g.getCity());
        dto.setPhoneNumber(g.getPhoneNumber());
        dto.setEmail(g.getEmail());
        dto.setPassword(g.getPassword());
        return dto;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

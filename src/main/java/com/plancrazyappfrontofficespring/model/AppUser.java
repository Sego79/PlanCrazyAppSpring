package com.plancrazyappfrontofficespring.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "app_user")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_user_id", nullable = false, unique = true)
    private Long appUserId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String address;

    private Integer postcode;

    private String city;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roleList;

    @OneToMany
    private List<Picture> pictureList = new ArrayList<>();

    @OneToMany(mappedBy = "appUser", fetch = FetchType.EAGER)
    private List<UserTaskAssociation> UserTaskAssociationList = new ArrayList<>();

    public AppUser() {

    }

    public AppUser(String nickname, String firstName, String lastName, String address, Integer postcode, String city, String phoneNumber, String email, String password) {
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    public AppUser(Long appUserId, String nickname, String firstName, String lastName, String address, Integer postcode, String city, String phoneNumber, String email, String password, Boolean isActive) {
        this.appUserId = appUserId;
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "appUserId=" + appUserId +
                ", nickname='" + nickname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", postcode=" + postcode +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roleList=" + roleList +
                '}';
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

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Picture> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<Picture> pictureList) {
        this.pictureList = pictureList;
    }

    public List<UserTaskAssociation> getUserTaskAssociationList() {
        return UserTaskAssociationList;
    }

    public void setUserTaskAssociationList(List<UserTaskAssociation> userTaskAssociationList) {
        UserTaskAssociationList = userTaskAssociationList;
    }
}

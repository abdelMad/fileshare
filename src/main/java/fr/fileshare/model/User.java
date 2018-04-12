package fr.fileshare.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String email;
    private String password;
    private String fname;
    private String lname;
    private String gender;
    private String address;
    private String telephone;
    private String loginStatus;
    private boolean emailChecked;
    private boolean telChecked;
    private Date registerDate;
    private String birthDate;


    private String description;
    private String image;
    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<Mission> publishedMissions;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Mission> chosedMissions;
    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    private Set<Message> messagesSent;
    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private Set<Message> messagesReceived;

    public User() {
    }

    public int getId() {
        return id;
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

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFullName() {
        return fname + " " + lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean isEmailChecked() {
        return emailChecked;
    }

    public void setEmailChecked(boolean emailChecked) {
        this.emailChecked = emailChecked;
    }

    public boolean isTelChecked() {
        return telChecked;
    }

    public void setTelChecked(boolean telChecked) {
        this.telChecked = telChecked;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Mission> getPublishedMissions() {
        return publishedMissions;
    }

    public void setPublishedMissions(Set<Mission> publishedMissions) {
        this.publishedMissions = publishedMissions;
    }

    public Set<Mission> getChosedMissions() {
        return chosedMissions;
    }

    public void setChosedMissions(Set<Mission> chosedMissions) {
        this.chosedMissions = chosedMissions;
    }

    public Set<Message> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(Set<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public Set<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(Set<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}

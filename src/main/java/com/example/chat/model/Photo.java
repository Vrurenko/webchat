package com.example.chat.model;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "a_photo")
public class Photo {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="user_id", unique = true)
    private User user;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "photo")
    private byte[] photo;

    public Photo() {
    }

    public Photo(User user, byte[] photo) {
        this.user = user;
        this.photo = photo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", user=" + user +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}

package com.example.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="a_discussion")
public class Discussion {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER)
    private Set<Message> messages = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(name = "a_discussion_user",
            joinColumns = @JoinColumn(name = "discussion_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> users = new HashSet<>();

    public Discussion() {
    }

    public Discussion(Set<User> users) {
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Discussion{" +
                "id=" + id +
                ", messages=" + messages +
                ", users=" + users +
                '}';
    }
}

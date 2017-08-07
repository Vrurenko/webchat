package com.example.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="a_message")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private long id;

    @Column(name="text", nullable = false, length = 2047)
    private String text;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User sender;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Discussion recipient;


    public Message() {
    }

    public Message(String text, Date date) {
        this.text = text;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Discussion getRecipient() {
        return recipient;
    }

    public void setRecipient(Discussion recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", sender=" + sender +
                '}';
    }
}

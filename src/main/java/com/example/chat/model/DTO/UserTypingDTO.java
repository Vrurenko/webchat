package com.example.chat.model.DTO;

public class UserTypingDTO {

    private String login;
    private long discussionID;

    public UserTypingDTO() {
    }

    public UserTypingDTO(String login, long discussionID) {
        this.login = login;
        this.discussionID = discussionID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getDiscussionID() {
        return discussionID;
    }

    public void setDiscussionID(long discussionID) {
        discussionID = discussionID;
    }
}

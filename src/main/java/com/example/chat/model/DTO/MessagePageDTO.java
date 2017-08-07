package com.example.chat.model.DTO;

public class MessagePageDTO {
    private long discussionID;
    private int page;

    public MessagePageDTO() {
    }

    public MessagePageDTO(int page, long discussionID) {
        this.page = page;
        this.discussionID = discussionID;
    }

    public long getDiscussionID() {
        return discussionID;
    }

    public void setDiscussionID(long discussionID) {
        this.discussionID = discussionID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "MessagePageDTO{" +
                "discussionID=" + discussionID +
                ", page=" + page +
                '}';
    }
}

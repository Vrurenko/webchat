package com.example.chat.service;

import com.example.chat.model.Message;

import java.util.List;

public interface IMessageService {

    public List<Message> loadMessages(long ID, int page);

    public Message saveAndFlush(Message message);

}

package com.example.chat.service;

import com.example.chat.model.Message;
import com.example.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class MessageService implements IMessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> loadMessages(long ID, int page) {
        List<Message> list = messageRepository.loadMessages(ID, new PageRequest(page - 1, 10));
        Collections.reverse(list);
        return list;
    }

    @Override
    public Message saveAndFlush(Message message) {
        return messageRepository.saveAndFlush(message);
    }


}

package com.example.chat.service;

import com.example.chat.model.Discussion;
import com.example.chat.model.User;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IDiscussionService {

    public Discussion save(Discussion discussion);

    public List<Discussion> findAll();

    public List<Discussion> findByUser(User user);

    public Discussion findById(long id);

    public void flush();

}

package com.example.chat.service;

import com.example.chat.model.Discussion;
import com.example.chat.model.User;
import com.example.chat.repository.DiscussionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DiscussionService implements IDiscussionService {

    private final DiscussionRepository discussionRepository;

    @Autowired
    public DiscussionService(DiscussionRepository discussionRepository) {
        this.discussionRepository = discussionRepository;
    }

    @Override
    public Discussion save(Discussion discussion) {
        return discussionRepository.save(discussion);
    }

    @Override
    public List<Discussion> findAll() {
        return discussionRepository.findAll();
    }

    @Override
    public List<Discussion> findByUser(User user) {
        return discussionRepository.findByUser(user);
    }

    @Override
    public Discussion findById(long id) {
        return discussionRepository.findOne(id);
    }

    @Override
    public void flush() {
        discussionRepository.flush();
    }

}

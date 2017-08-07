package com.example.chat.service;

import com.example.chat.model.User;

import java.util.List;

public interface IUserService {

    public List<User> findUsersByKeyWord(String keyWord);

    public User save(User user);

    public User register(User user);

    public User findById(long id);

}

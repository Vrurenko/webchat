package com.example.chat.repository;

import com.example.chat.model.Discussion;
import com.example.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    @Query("SELECT d FROM Discussion d INNER JOIN d.users u WHERE u = :user")
    List<Discussion> findByUser(@Param("user")User user);

}

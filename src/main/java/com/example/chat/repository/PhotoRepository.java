package com.example.chat.repository;

import com.example.chat.model.Photo;
import com.example.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByUser(@Param("user_id") User user);

}

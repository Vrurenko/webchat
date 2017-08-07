package com.example.chat.repository;

import com.example.chat.model.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.recipient.id = :id  ORDER BY m.id DESC")
    List<Message> loadMessages(@Param("id")long ID, Pageable pageable);
}

package com.example.chat.repository;

import com.example.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(@Param("login") String login);

    @Query("SELECT u FROM User u WHERE UPPER(u.login) LIKE CONCAT('%',UPPER(:key),'%')")
    List<User> findUsersByKeyWord(@Param("key") String keyWord);

}

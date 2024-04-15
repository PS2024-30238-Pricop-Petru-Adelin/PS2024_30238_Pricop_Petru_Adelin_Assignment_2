package com.olxapplication.repository;

import com.olxapplication.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findBySenderId(String id);
    List<Message> findByReceiverId(String id);
    List<Message> findBySenderIdAndReceiverId(String senderId, String receiverId);
}

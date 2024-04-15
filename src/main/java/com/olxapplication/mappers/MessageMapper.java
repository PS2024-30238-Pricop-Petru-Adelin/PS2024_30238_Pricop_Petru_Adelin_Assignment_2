package com.olxapplication.mappers;

import com.olxapplication.constants.MessageMessages;
import com.olxapplication.dtos.MessageWebDTO;
import com.olxapplication.entity.Message;
import com.olxapplication.repository.MessageRepository;
import com.olxapplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
public class MessageMapper {
//    private final UserRepository userRepository;

    public static MessageWebDTO toMessageWebDTO(Message message){
        return MessageWebDTO.builder()
                .id(message.getId())
                .msg(message.getMsg())
                .sender(message.getSender().getId())
                .receiver(message.getReceiver().getId())
                .build();
    }

//    public static Message toEntity(MessageWebDTO messageWebDTO){
//        return Message.builder()
//                .id(messageWebDTO.getId())
//                .msg(messageWebDTO.getMsg())
//                .sender(userRepository.findById(messageWebDTO.getSender()))
//                .build();
//    }
}

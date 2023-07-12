package com.charles.server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryContent implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    
    private Long chatId;
    
    private Long groupId;
    
}

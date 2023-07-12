package com.charles.server.entity.vo;

import com.charles.server.entity.ChatUser;
import com.charles.server.entity.model.ChangeRelation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRelationResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ChangeRelation changeRelation;
    
    private ChatUser user;
    
}

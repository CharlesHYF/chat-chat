package com.charles.server.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Charles-H
 * 
 * 返回的好友信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainInfo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<History> historyList;
    
    private List<Friend> friendList;
    
    private List<Friend> strangerList;
    
    private List<Friend> BlackList;
    
    private List<Group> groupList;
    
}

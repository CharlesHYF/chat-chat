/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : chat-chat

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 12/07/2023 16:26:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apply_friend
-- ----------------------------
DROP TABLE IF EXISTS `apply_friend`;
CREATE TABLE `apply_friend`  (
  `apply_id` bigint(0) NOT NULL COMMENT '主动申请的用户id',
  `be_applied_id` bigint(0) NOT NULL COMMENT '被动申请的用户id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '申请状态：0申请中，1通过，2拒绝',
  PRIMARY KEY (`apply_id`, `be_applied_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apply_friend
-- ----------------------------
INSERT INTO `apply_friend` VALUES (1679030911179550722, 1679027848079233026, '1');

-- ----------------------------
-- Table structure for apply_group
-- ----------------------------
DROP TABLE IF EXISTS `apply_group`;
CREATE TABLE `apply_group`  (
  `group_id` bigint(0) NOT NULL COMMENT '聊天室id',
  `user_id` bigint(0) NOT NULL COMMENT '申请用户id',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '申请状态：0:申请中，1：通过，2：未通过',
  PRIMARY KEY (`group_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of apply_group
-- ----------------------------

-- ----------------------------
-- Table structure for chat_content
-- ----------------------------
DROP TABLE IF EXISTS `chat_content`;
CREATE TABLE `chat_content`  (
  `chat_id` bigint(0) NOT NULL AUTO_INCREMENT,
  `send_id` bigint(0) NOT NULL,
  `receive_id` bigint(0) NULL DEFAULT NULL,
  `group_id` bigint(0) NOT NULL DEFAULT 0,
  `content` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `send_time` datetime(0) NOT NULL,
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1664293715952386051 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_content
-- ----------------------------

-- ----------------------------
-- Table structure for chat_group
-- ----------------------------
DROP TABLE IF EXISTS `chat_group`;
CREATE TABLE `chat_group`  (
  `group_id` bigint(0) NOT NULL,
  `creator_id` bigint(0) NOT NULL COMMENT '创建人id',
  `group_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime(0) NOT NULL,
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_group
-- ----------------------------

-- ----------------------------
-- Table structure for chat_user
-- ----------------------------
DROP TABLE IF EXISTS `chat_user`;
CREATE TABLE `chat_user`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账号(小写字母开头，不少于10，不大于20)',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码(不小于6，不大于10)',
  `username` varchar(22) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名(不小于2，不大于10)',
  `gender` int(0) NOT NULL DEFAULT 2 COMMENT '性别(0:男，1:女, 2:保密，默认保密)',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电子邮箱',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户头像',
  `individual_sign` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '这个人很懒，什么也没留下' COMMENT '个性签名(可为空)',
  `status` int(0) NOT NULL DEFAULT 0 COMMENT '用户状态(0:离线，1:在线, 2:隐身)',
  `login_time` datetime(0) NULL DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_user
-- ----------------------------
INSERT INTO `chat_user` VALUES (1679027848079233026, 'w123456789', '$2a$10$DiNH2oOwtl/twrih30tsnu6gxbF2NtbQRdTwXxe4DnDjRZWGdA8Xq', '测试员', 2, 'w1232456789@gmail.com', 'chat-chat-client/src/main/resources/static/images/user.png', '这个人很懒，什么也没留下', 0, '2023-07-12 15:31:50');
INSERT INTO `chat_user` VALUES (1679030911179550722, 'a123456789', '$2a$10$RScv3tv.jSmSEeLqXuRFKeiWLWe7bZbn3X0wAiQKPWHZAasercALi', '测试员2号', 2, 'a123456789@gmail.com', 'chat-chat-client/src/main/resources/static/images/user.png', '这个人很懒，什么也没留下', 0, '2023-07-12 15:32:29');

-- ----------------------------
-- Table structure for group_member
-- ----------------------------
DROP TABLE IF EXISTS `group_member`;
CREATE TABLE `group_member`  (
  `mid` bigint(0) NOT NULL,
  `group_id` bigint(0) NOT NULL,
  `user_id` bigint(0) NOT NULL,
  `join_time` datetime(0) NOT NULL,
  PRIMARY KEY (`mid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_member
-- ----------------------------

-- ----------------------------
-- Table structure for user_friend
-- ----------------------------
DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend`  (
  `relation_id` bigint(0) NOT NULL,
  `add_user` bigint(0) NOT NULL,
  `being_added_user` bigint(0) NOT NULL,
  `relation` int(0) NOT NULL COMMENT '0:好友，1:陌生人，2:黑名单',
  `add_time` datetime(0) NOT NULL,
  PRIMARY KEY (`relation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_friend
-- ----------------------------
INSERT INTO `user_friend` VALUES (1679031001701019650, 1679030911179550722, 1679027848079233026, 0, '2023-07-12 15:32:40');

-- ----------------------------
-- Procedure structure for Register
-- ----------------------------
DROP PROCEDURE IF EXISTS `Register`;
delimiter ;;
CREATE PROCEDURE `Register`()
BEGIN
    DECLARE seed INT;
    DECLARE username VARCHAR(20);
    
    SELECT MAX(id) + 1 INTO seed FROM `user`;
    
    SET @sql = CONCAT('SELECT CEILING(RAND(', seed, ') * 1000000) INTO @username');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
    
    INSERT INTO `user` (username)
    VALUES (@username);
    
    SELECT @username;
END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;

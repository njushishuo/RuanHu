/*
Navicat MySQL Data Transfer

Source Server         : REMOTE
Source Server Version : 50547
Source Host           : 110.173.17.140:3306
Source Database       : RuanHu

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2016-07-19 00:24:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for AlibabaKey
-- ----------------------------
DROP TABLE IF EXISTS `AlibabaKey`;
CREATE TABLE `AlibabaKey` (
  `AccessKeySecret` varchar(255) DEFAULT NULL,
  `AccessKeyId` varchar(255) NOT NULL,
  PRIMARY KEY (`AccessKeyId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `author_id` bigint(20) NOT NULL,
  `question_id` bigint(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `vote_count` bigint(20) DEFAULT '0',
  `solution` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_3q5s4b88xp78n3c49dtxfs97e` (`author_id`),
  KEY `FK_10g8xii7lw9kq0kcsobgmtw72` (`question_id`),
  CONSTRAINT `FK_10g8xii7lw9kq0kcsobgmtw72` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_3q5s4b88xp78n3c49dtxfs97e` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=460 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` longtext,
  `author_id` bigint(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `answer_id` bigint(20) DEFAULT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_j94pith5sd971k29j6ysxuk7` (`author_id`),
  KEY `comment_answer_idx` (`answer_id`),
  KEY `comment_question_idx` (`question_id`),
  CONSTRAINT `comment_answer` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`) ON DELETE CASCADE,
  CONSTRAINT `comment_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_j94pith5sd971k29j6ysxuk7` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `answer_count` bigint(20) DEFAULT '0',
  `vote_count` bigint(20) DEFAULT '0',
  `view_count` bigint(20) DEFAULT '0',
  `author_id` bigint(20) NOT NULL,
  `title` varchar(100) DEFAULT '',
  `content` longtext,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_a3dib35x299yvhfk7pau0kw5w` (`author_id`),
  CONSTRAINT `question_user` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) DEFAULT 'unknow',
  `location` varchar(100) DEFAULT 'unknow',
  `user_name` varchar(100) NOT NULL,
  `password` varchar(45) NOT NULL,
  `photo_uri` varchar(255) DEFAULT NULL,
  `description` longtext,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `birth_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name_UNIQUE` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1019 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vote
-- ----------------------------
DROP TABLE IF EXISTS `vote`;
CREATE TABLE `vote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` enum('down','up') NOT NULL DEFAULT 'up',
  `author_id` bigint(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `last_updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `answer_id` bigint(20) DEFAULT NULL,
  `question_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6nch3y92lphrbsh0o5c7o0jov` (`author_id`),
  KEY `vote_answer_idx` (`answer_id`),
  KEY `vote_question_idx` (`question_id`),
  CONSTRAINT `FK_6nch3y92lphrbsh0o5c7o0jov` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `vote_answer` FOREIGN KEY (`answer_id`) REFERENCES `answer` (`id`) ON DELETE CASCADE,
  CONSTRAINT `vote_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
DROP TRIGGER IF EXISTS `update_question_insert`;
DELIMITER ;;
CREATE TRIGGER `update_question_insert` AFTER INSERT ON `answer` FOR EACH ROW begin
        update question
        set answer_count = answer_count + 1
        where id = new.question_id;
end
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_question_delete`;
DELIMITER ;;
CREATE TRIGGER `update_question_delete` AFTER DELETE ON `answer` FOR EACH ROW begin
        update question
        set answer_count = answer_count - 1
        where id = old.question_id;
end
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_question_or_answer_insert`;
DELIMITER ;;
CREATE TRIGGER `update_question_or_answer_insert` AFTER INSERT ON `vote` FOR EACH ROW begin
     declare num int;
     if new.type = 'up'  then
     set num = 1;
     else
     set num = -1;
     end if;

      if new.question_id is not null then
          update question set vote_count = vote_count+num where id = new.question_id;
     else
          update answer set vote_count = vote_count+num where id = new.answer_id;
    end if;
end
;;
DELIMITER ;
DROP TRIGGER IF EXISTS `update_question_or_answer_delete`;
DELIMITER ;;
CREATE TRIGGER `update_question_or_answer_delete` AFTER DELETE ON `vote` FOR EACH ROW begin
     declare num int;
     if old.type = 'up'  then
     set num = 1;
     else
     set num = -1;
     end if;

      if old.question_id is not null then
          update question set vote_count = vote_count-num where id = old.question_id;
     else
          update answer set vote_count = vote_count-num where id = old.answer_id;
    end if;
end
;;
DELIMITER ;

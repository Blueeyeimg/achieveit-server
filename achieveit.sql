/*
Navicat MySQL Data Transfer

Source Server         : first_mysql
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : achieveit

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2020-03-07 14:46:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `activity`
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `activity_id` int(255) NOT NULL AUTO_INCREMENT,
  `primary_activity` varchar(255) NOT NULL,
  `secondary_activity` varchar(255) NOT NULL,
  PRIMARY KEY (`activity_id`),
  KEY `activity_id` (`activity_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of activity
-- ----------------------------

-- ----------------------------
-- Table structure for `client`
-- ----------------------------
DROP TABLE IF EXISTS `client`;
CREATE TABLE `client` (
  `client_id` varchar(255) NOT NULL,
  `contacter` varchar(255) NOT NULL,
  `company_name` varchar(255) NOT NULL DEFAULT '',
  `client_level` int(11) NOT NULL DEFAULT '0',
  `email` varchar(255) NOT NULL DEFAULT '',
  `telephone` varchar(50) NOT NULL DEFAULT '',
  `address` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`client_id`),
  KEY `client_fk_1` (`contacter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of client
-- ----------------------------

-- ----------------------------
-- Table structure for `employee`
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `employee_id` varchar(255) NOT NULL,
  `employee_name` varchar(255) NOT NULL DEFAULT '',
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `department` varchar(255) NOT NULL DEFAULT '',
  `telephone` varchar(50) NOT NULL DEFAULT '',
  `title` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('1', 'robert', '123456', 'robert11@qq.com', 'DAET', '13658205654', '项目经理');

-- ----------------------------
-- Table structure for `project_basic_info`
-- ----------------------------
DROP TABLE IF EXISTS `project_basic_info`;
CREATE TABLE `project_basic_info` (
  `project_id` varchar(255) NOT NULL,
  `project_name` varchar(255) NOT NULL,
  `client_id` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL DEFAULT 'appied',
  `exp_start_date` date NOT NULL,
  `exp_end_date` date NOT NULL,
  `project_boss_id` varchar(255) NOT NULL,
  `milestone` varchar(255) NOT NULL DEFAULT '',
  `technology` varchar(255) NOT NULL DEFAULT '',
  `business_domain` varchar(255) NOT NULL DEFAULT '',
  `main_functions` varchar(255) NOT NULL DEFAULT '',
  `output_link` varchar(255) NOT NULL DEFAULT '' COMMENT 'this is a url.',
  `git_address` varchar(255) NOT NULL DEFAULT '',
  `file_system_address` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`project_id`),
  KEY `project_fk_1` (`client_id`),
  KEY `project_fk_2` (`project_boss_id`),
  CONSTRAINT `project_fk_1` FOREIGN KEY (`client_id`) REFERENCES `client` (`client_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `project_fk_2` FOREIGN KEY (`project_boss_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_basic_info
-- ----------------------------

-- ----------------------------
-- Table structure for `project_device`
-- ----------------------------
DROP TABLE IF EXISTS `project_device`;
CREATE TABLE `project_device` (
  `project_id` varchar(255) NOT NULL,
  `device_id` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '0：未归还，1：已归还',
  `device_manager_id` varchar(255) NOT NULL,
  `checkin_date` date NOT NULL,
  `total_use_time` int(11) NOT NULL DEFAULT '0' COMMENT '以月为单位',
  `last_verify_date` date NOT NULL,
  `return_date` date NOT NULL COMMENT '当state为1时使用该变量',
  PRIMARY KEY (`project_id`,`device_id`),
  KEY `project_device_fk_2` (`device_manager_id`),
  CONSTRAINT `project_device_fk_1` FOREIGN KEY (`project_id`) REFERENCES `project_basic_info` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `project_device_fk_2` FOREIGN KEY (`device_manager_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_device
-- ----------------------------

-- ----------------------------
-- Table structure for `project_function`
-- ----------------------------
DROP TABLE IF EXISTS `project_function`;
CREATE TABLE `project_function` (
  `project_id` varchar(255) NOT NULL,
  `primary_function` varchar(255) NOT NULL,
  `secondary_function` varchar(255) NOT NULL,
  PRIMARY KEY (`project_id`,`primary_function`,`secondary_function`),
  KEY `primary_function` (`primary_function`),
  KEY `secondary_function` (`secondary_function`),
  CONSTRAINT `project_function` FOREIGN KEY (`project_id`) REFERENCES `project_basic_info` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_function
-- ----------------------------

-- ----------------------------
-- Table structure for `project_member`
-- ----------------------------
DROP TABLE IF EXISTS `project_member`;
CREATE TABLE `project_member` (
  `project_id` varchar(255) NOT NULL,
  `employee_id` varchar(255) NOT NULL,
  `boss_in_project_id` varchar(255) NOT NULL DEFAULT '',
  `role` varchar(255) NOT NULL COMMENT '可选择多个role，用；分割',
  `access_git` varchar(255) NOT NULL DEFAULT 'R',
  `access_file_system` varchar(255) NOT NULL DEFAULT 'R',
  `in_email_list` int(11) NOT NULL DEFAULT '0',
  `in_timesheet_module` int(11) NOT NULL DEFAULT '0',
  `fault_track` int(11) NOT NULL DEFAULT '0' COMMENT 'pm、tl、dl：1',
  PRIMARY KEY (`project_id`,`employee_id`),
  KEY `project_member_fk_2` (`employee_id`),
  KEY `project_member_fk_3` (`boss_in_project_id`),
  CONSTRAINT `project_member_fk_1` FOREIGN KEY (`project_id`) REFERENCES `project_basic_info` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `project_member_fk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `project_member_fk_3` FOREIGN KEY (`boss_in_project_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_member
-- ----------------------------

-- ----------------------------
-- Table structure for `project_risk`
-- ----------------------------
DROP TABLE IF EXISTS `project_risk`;
CREATE TABLE `project_risk` (
  `project_id` varchar(255) NOT NULL,
  `risk_id` varchar(255) NOT NULL,
  `type` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL,
  `risk_level` int(11) NOT NULL DEFAULT '0',
  `influence` varchar(255) NOT NULL,
  `reactive_strategy` varchar(255) NOT NULL,
  `risk_state` varchar(50) NOT NULL,
  `risk_owner_id` varchar(255) NOT NULL,
  `risk_track_frequency` decimal(10,0) NOT NULL,
  PRIMARY KEY (`project_id`,`risk_id`),
  KEY `project_risk_fk_2` (`risk_owner_id`),
  KEY `risk_id` (`risk_id`),
  CONSTRAINT `project_risk_fk_1` FOREIGN KEY (`project_id`) REFERENCES `project_basic_info` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `project_risk_fk_2` FOREIGN KEY (`risk_owner_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of project_risk
-- ----------------------------

-- ----------------------------
-- Table structure for `review_defect_info`
-- ----------------------------
DROP TABLE IF EXISTS `review_defect_info`;
CREATE TABLE `review_defect_info` (
  `review_defect_id` int(11) NOT NULL,
  `project_id` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL COMMENT 'review, defect',
  `provider_id` varchar(255) NOT NULL,
  `state` int(11) NOT NULL DEFAULT '0' COMMENT '0: 未处理 1: 已处理',
  `solver_id` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `link` varchar(255) NOT NULL,
  PRIMARY KEY (`review_defect_id`),
  KEY `review_defect_info_fk_1` (`project_id`),
  KEY `review_defect_info_fk_2` (`provider_id`),
  KEY `review_defect_info_fk_3` (`solver_id`),
  CONSTRAINT `review_defect_info_fk_1` FOREIGN KEY (`project_id`) REFERENCES `project_basic_info` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `review_defect_info_fk_2` FOREIGN KEY (`provider_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `review_defect_info_fk_3` FOREIGN KEY (`solver_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of review_defect_info
-- ----------------------------

-- ----------------------------
-- Table structure for `risk_related`
-- ----------------------------
DROP TABLE IF EXISTS `risk_related`;
CREATE TABLE `risk_related` (
  `project_id` varchar(255) NOT NULL,
  `risk_id` varchar(255) NOT NULL,
  `risk_related_id` varchar(255) NOT NULL,
  PRIMARY KEY (`project_id`,`risk_id`,`risk_related_id`),
  KEY `risk_related_fk_3` (`risk_related_id`),
  KEY `risk_related_fk_2` (`risk_id`),
  CONSTRAINT `risk_related_fk_1` FOREIGN KEY (`project_id`) REFERENCES `project_basic_info` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `risk_related_fk_2` FOREIGN KEY (`risk_id`) REFERENCES `project_risk` (`risk_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `risk_related_fk_3` FOREIGN KEY (`risk_related_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of risk_related
-- ----------------------------

-- ----------------------------
-- Table structure for `timesheet`
-- ----------------------------
DROP TABLE IF EXISTS `timesheet`;
CREATE TABLE `timesheet` (
  `timesheet_id` int(11) NOT NULL AUTO_INCREMENT,
  `project_id` varchar(255) NOT NULL,
  `employee_id` varchar(255) NOT NULL,
  `primary_function` varchar(255) NOT NULL,
  `secondary_funtion` varchar(255) NOT NULL,
  `primary_activity` varchar(255) NOT NULL,
  `secondary_activity` varchar(255) NOT NULL,
  `date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `state` varchar(255) NOT NULL COMMENT '草稿、已提交、已确认、打回',
  PRIMARY KEY (`timesheet_id`),
  KEY `timeheet_fk_1` (`project_id`),
  KEY `timeheet_fk_2` (`employee_id`),
  KEY `timeheet_fk_3` (`primary_function`),
  KEY `timeheet_fk_4` (`secondary_funtion`),
  KEY `timeheet_fk_5` (`primary_activity`),
  KEY `timeheet_fk_6` (`secondary_activity`),
  CONSTRAINT `timeheet_fk_1` FOREIGN KEY (`project_id`) REFERENCES `project_basic_info` (`project_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `timeheet_fk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employee_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `timeheet_fk_3` FOREIGN KEY (`primary_function`) REFERENCES `project_function` (`primary_function`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `timeheet_fk_4` FOREIGN KEY (`secondary_funtion`) REFERENCES `project_function` (`secondary_function`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of timesheet
-- ----------------------------

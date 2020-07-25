/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : erp

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2020-07-25 21:18:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `a_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `c_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dep_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for detail
-- ----------------------------
DROP TABLE IF EXISTS `detail`;
CREATE TABLE `detail` (
  `d_id` int(11) NOT NULL AUTO_INCREMENT,
  `d_date` datetime NOT NULL,
  `d_description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `p_id` int(11) NOT NULL,
  `a_id` int(11) NOT NULL,
  `dep_id` int(11) NOT NULL,
  `c_id` int(11) NOT NULL,
  `d_earning` decimal(19,2) NOT NULL,
  `d_expense` decimal(19,2) NOT NULL,
  `d_balance` decimal(19,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`d_id`),
  UNIQUE KEY `d_date` (`d_date` DESC),
  KEY `idx_project_account_department_category` (`p_id`,`a_id`,`dep_id`,`c_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `p_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_username` varchar(32) NOT NULL,
  `u_password` varchar(32) NOT NULL,
  `u_level` int(11) NOT NULL,
  PRIMARY KEY (`u_id`),
  UNIQUE KEY `u_username` (`u_username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for voucher
-- ----------------------------
DROP TABLE IF EXISTS `voucher`;
CREATE TABLE `voucher` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `d_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `d_id` (`d_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

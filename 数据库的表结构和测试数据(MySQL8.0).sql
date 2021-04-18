/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : erp

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2021-04-18 20:29:05
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('2', '账户1');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `c_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('2', '分类1');
INSERT INTO `category` VALUES ('3', '分类2');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `dep_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES ('1', 'FBI');
INSERT INTO `department` VALUES ('2', '部门1');

-- ----------------------------
-- Table structure for detail
-- ----------------------------
DROP TABLE IF EXISTS `detail`;
CREATE TABLE `detail` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `date` datetime NOT NULL,
                          `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                          `project_id` int(11) NOT NULL,
                          `account_id` int(11) NOT NULL,
                          `department_id` int(11) NOT NULL,
                          `category_id` int(11) NOT NULL,
                          `earning` decimal(19,2) NOT NULL,
                          `expense` decimal(19,2) NOT NULL,
                          `balance` decimal(19,2) NOT NULL DEFAULT '0.00',
                          `reimbursement` bit(1) DEFAULT NULL COMMENT '是否已报销',
                          `create_time` datetime DEFAULT NULL,
                          `alter_time` datetime DEFAULT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `d_date` (`date` DESC),
                          KEY `idx_category_department_account_project` (`category_id`,`department_id`,`account_id`,`project_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of detail
-- ----------------------------
INSERT INTO `detail` VALUES ('13', '2018-01-01 17:03:32', '测试数据1', '2', '2', '2', '2', '15.00', '61.00', '-46.00', null, null, null);
INSERT INTO `detail` VALUES ('14', '2018-01-08 17:03:32', '测试数据2', '2', '2', '2', '2', '91.00', '76.00', '-31.00', null, null, null);
INSERT INTO `detail` VALUES ('15', '2018-01-15 17:03:32', '测试数据3', '2', '2', '2', '2', '59.00', '20.00', '8.00', null, null, null);
INSERT INTO `detail` VALUES ('16', '2018-01-22 17:03:32', '测试数据4', '2', '2', '2', '2', '79.00', '25.00', '62.00', null, null, null);
INSERT INTO `detail` VALUES ('17', '2018-01-29 17:03:32', '测试数据5', '2', '2', '2', '3', '31.00', '24.00', '69.00', null, null, null);
INSERT INTO `detail` VALUES ('18', '2018-02-05 17:03:32', '测试数据6', '2', '2', '2', '3', '77.00', '98.00', '48.00', null, null, null);
INSERT INTO `detail` VALUES ('19', '2018-02-12 17:03:32', '测试数据7', '2', '2', '2', '3', '60.00', '34.00', '74.00', null, null, null);
INSERT INTO `detail` VALUES ('20', '2018-02-19 17:03:32', '测试数据8', '2', '2', '2', '2', '18.00', '47.00', '45.00', null, null, null);
INSERT INTO `detail` VALUES ('21', '2018-02-26 17:03:32', '测试数据9', '2', '2', '2', '3', '88.00', '8.00', '125.00', null, null, null);
INSERT INTO `detail` VALUES ('22', '2018-03-05 17:03:32', '测试数据10', '2', '2', '2', '2', '37.00', '45.00', '117.00', null, null, null);
INSERT INTO `detail` VALUES ('23', '2018-03-12 17:03:32', '测试数据11', '2', '2', '2', '2', '2.00', '20.00', '99.00', null, null, null);
INSERT INTO `detail` VALUES ('24', '2018-03-19 17:03:32', '测试数据12', '2', '2', '2', '3', '88.00', '29.00', '158.00', null, null, null);
INSERT INTO `detail` VALUES ('25', '2018-03-26 17:03:32', '测试数据13', '2', '2', '2', '2', '36.00', '44.00', '150.00', null, null, null);
INSERT INTO `detail` VALUES ('26', '2018-04-02 17:03:32', '测试数据14', '2', '2', '2', '3', '17.00', '79.00', '88.00', null, null, null);
INSERT INTO `detail` VALUES ('27', '2018-04-09 17:03:32', '测试数据15', '2', '2', '2', '3', '54.00', '16.00', '126.00', null, null, null);
INSERT INTO `detail` VALUES ('28', '2018-04-16 17:03:32', '测试数据16', '2', '2', '2', '3', '71.00', '16.00', '181.00', null, null, null);
INSERT INTO `detail` VALUES ('29', '2018-04-23 17:03:32', '测试数据17', '2', '2', '2', '3', '89.00', '37.00', '233.00', null, null, null);
INSERT INTO `detail` VALUES ('30', '2018-04-30 17:03:32', '测试数据18', '2', '2', '2', '2', '94.00', '77.00', '250.00', null, null, null);
INSERT INTO `detail` VALUES ('31', '2018-05-07 17:03:32', '测试数据19', '2', '2', '2', '2', '72.00', '79.00', '243.00', null, null, null);
INSERT INTO `detail` VALUES ('32', '2018-05-14 17:03:32', '测试数据20', '2', '2', '2', '2', '45.00', '24.00', '264.00', null, null, null);
INSERT INTO `detail` VALUES ('33', '2018-05-21 17:03:32', '测试数据21', '2', '2', '2', '3', '19.00', '44.00', '239.00', null, null, null);
INSERT INTO `detail` VALUES ('34', '2018-05-28 17:03:32', '测试数据22', '2', '2', '2', '3', '27.00', '46.00', '220.00', null, null, null);
INSERT INTO `detail` VALUES ('35', '2018-06-04 17:03:32', '测试数据23', '2', '2', '2', '3', '10.00', '95.00', '135.00', null, null, null);
INSERT INTO `detail` VALUES ('36', '2018-06-11 17:03:32', '测试数据24', '2', '2', '2', '2', '48.00', '82.00', '101.00', null, null, null);
INSERT INTO `detail` VALUES ('37', '2018-06-18 17:03:32', '测试数据25', '2', '2', '2', '3', '66.00', '22.00', '145.00', null, null, null);
INSERT INTO `detail` VALUES ('38', '2018-06-25 17:03:32', '测试数据26', '2', '2', '2', '3', '47.00', '76.00', '116.00', null, null, null);
INSERT INTO `detail` VALUES ('39', '2018-07-02 17:03:32', '测试数据27', '2', '2', '2', '2', '12.00', '36.00', '92.00', null, null, null);
INSERT INTO `detail` VALUES ('40', '2018-07-09 17:03:32', '测试数据28', '2', '2', '2', '2', '82.00', '26.00', '148.00', null, null, null);
INSERT INTO `detail` VALUES ('41', '2018-07-16 17:03:32', '测试数据29', '2', '2', '2', '3', '2.00', '17.00', '133.00', null, null, null);
INSERT INTO `detail` VALUES ('42', '2018-07-23 17:03:32', '测试数据30', '2', '2', '2', '2', '65.00', '43.00', '155.00', null, null, null);
INSERT INTO `detail` VALUES ('43', '2018-07-30 17:03:32', '测试数据31', '2', '2', '2', '3', '45.00', '12.00', '188.00', null, null, null);
INSERT INTO `detail` VALUES ('44', '2018-08-06 17:03:32', '测试数据32', '2', '2', '2', '2', '29.00', '87.00', '130.00', null, null, null);
INSERT INTO `detail` VALUES ('45', '2018-08-13 17:03:32', '测试数据33', '2', '2', '2', '2', '0.00', '96.00', '34.00', null, null, null);
INSERT INTO `detail` VALUES ('46', '2018-08-20 17:03:32', '测试数据34', '2', '2', '2', '2', '66.00', '33.00', '67.00', null, null, null);
INSERT INTO `detail` VALUES ('47', '2018-08-27 17:03:32', '测试数据35', '2', '2', '2', '3', '26.00', '82.00', '11.00', null, null, null);
INSERT INTO `detail` VALUES ('48', '2018-09-03 17:03:32', '测试数据36', '2', '2', '2', '3', '9.00', '2.00', '18.00', null, null, null);
INSERT INTO `detail` VALUES ('49', '2018-09-10 17:03:32', '测试数据37', '2', '2', '2', '2', '58.00', '15.00', '61.00', null, null, null);
INSERT INTO `detail` VALUES ('50', '2018-09-17 17:03:32', '测试数据38', '2', '2', '2', '2', '9.00', '82.00', '-12.00', null, null, null);
INSERT INTO `detail` VALUES ('51', '2018-09-24 17:03:32', '测试数据39', '2', '2', '2', '2', '6.00', '84.00', '-90.00', null, null, null);
INSERT INTO `detail` VALUES ('52', '2018-10-01 17:03:32', '测试数据40', '2', '2', '2', '2', '44.00', '65.00', '-111.00', null, null, null);
INSERT INTO `detail` VALUES ('53', '2018-10-08 17:03:32', '测试数据41', '2', '2', '2', '2', '87.00', '18.00', '-42.00', null, null, null);
INSERT INTO `detail` VALUES ('54', '2018-10-15 17:03:32', '测试数据42', '2', '2', '2', '2', '3.00', '76.00', '-115.00', null, null, null);
INSERT INTO `detail` VALUES ('55', '2018-10-22 17:03:32', '测试数据43', '2', '2', '2', '2', '33.00', '17.00', '-99.00', null, null, null);
INSERT INTO `detail` VALUES ('56', '2018-10-29 17:03:32', '测试数据44', '2', '2', '2', '3', '76.00', '38.00', '-61.00', null, null, null);
INSERT INTO `detail` VALUES ('57', '2018-11-05 17:03:32', '测试数据45', '2', '2', '2', '2', '92.00', '89.00', '-58.00', null, null, null);
INSERT INTO `detail` VALUES ('58', '2018-11-12 17:03:32', '测试数据46', '2', '2', '2', '2', '89.00', '15.00', '16.00', null, null, null);
INSERT INTO `detail` VALUES ('59', '2018-11-19 17:03:32', '测试数据47', '2', '2', '2', '3', '57.00', '30.00', '43.00', null, null, null);
INSERT INTO `detail` VALUES ('60', '2018-11-26 17:03:32', '测试数据48', '2', '2', '2', '2', '72.00', '37.00', '78.00', null, null, null);
INSERT INTO `detail` VALUES ('61', '2018-12-03 17:03:32', '测试数据49', '2', '2', '2', '3', '3.00', '64.00', '17.00', null, null, null);
INSERT INTO `detail` VALUES ('62', '2018-12-10 17:03:32', '测试数据50', '2', '2', '2', '2', '91.00', '72.00', '36.00', null, null, null);
INSERT INTO `detail` VALUES ('63', '2018-12-17 17:03:32', '测试数据51', '2', '2', '2', '2', '70.00', '41.00', '65.00', null, null, null);
INSERT INTO `detail` VALUES ('64', '2018-12-24 17:03:32', '测试数据52', '2', '2', '2', '2', '25.00', '92.00', '-2.00', null, null, null);
INSERT INTO `detail` VALUES ('65', '2018-12-31 17:03:32', '测试数据53', '2', '2', '2', '2', '90.00', '24.00', '64.00', null, null, null);
INSERT INTO `detail` VALUES ('66', '2019-01-07 17:03:32', '测试数据54', '2', '2', '2', '2', '84.00', '19.00', '129.00', null, null, null);
INSERT INTO `detail` VALUES ('67', '2019-01-14 17:03:32', '测试数据55', '2', '2', '2', '3', '14.00', '64.00', '79.00', null, null, null);
INSERT INTO `detail` VALUES ('68', '2019-01-21 17:03:32', '测试数据56', '2', '2', '2', '2', '7.00', '21.00', '65.00', null, null, null);
INSERT INTO `detail` VALUES ('69', '2019-01-28 17:03:32', '测试数据57', '2', '2', '2', '3', '72.00', '61.00', '76.00', null, null, null);
INSERT INTO `detail` VALUES ('70', '2019-02-04 17:03:32', '测试数据58', '2', '2', '2', '3', '5.00', '52.00', '29.00', null, null, null);
INSERT INTO `detail` VALUES ('71', '2019-02-11 17:03:32', '测试数据59', '2', '2', '2', '2', '70.00', '12.00', '87.00', null, null, null);
INSERT INTO `detail` VALUES ('72', '2019-02-18 17:03:32', '测试数据60', '2', '2', '2', '3', '68.00', '29.00', '126.00', null, null, null);
INSERT INTO `detail` VALUES ('73', '2019-02-25 17:03:32', '测试数据61', '2', '2', '2', '3', '29.00', '21.00', '134.00', null, null, null);
INSERT INTO `detail` VALUES ('74', '2019-03-04 17:03:32', '测试数据62', '2', '2', '2', '2', '12.00', '59.00', '87.00', null, null, null);
INSERT INTO `detail` VALUES ('75', '2019-03-11 17:03:32', '测试数据63', '2', '2', '2', '3', '93.00', '50.00', '130.00', null, null, null);
INSERT INTO `detail` VALUES ('76', '2019-03-18 17:03:32', '测试数据64', '2', '2', '2', '2', '66.00', '76.00', '120.00', null, null, null);
INSERT INTO `detail` VALUES ('77', '2019-03-25 17:03:32', '测试数据65', '2', '2', '2', '3', '85.00', '78.00', '127.00', null, null, null);
INSERT INTO `detail` VALUES ('78', '2019-04-01 17:03:32', '测试数据66', '2', '2', '2', '2', '8.00', '44.00', '91.00', null, null, null);
INSERT INTO `detail` VALUES ('79', '2019-04-08 17:03:32', '测试数据67', '2', '2', '2', '2', '47.00', '35.00', '103.00', null, null, null);
INSERT INTO `detail` VALUES ('80', '2019-04-15 17:03:32', '测试数据68', '2', '2', '2', '2', '61.00', '76.00', '88.00', null, null, null);
INSERT INTO `detail` VALUES ('81', '2019-04-22 17:03:32', '测试数据69', '2', '2', '2', '2', '84.00', '24.00', '148.00', null, null, null);
INSERT INTO `detail` VALUES ('82', '2019-04-29 17:03:32', '测试数据70', '2', '2', '2', '2', '98.00', '51.00', '195.00', '', null, null);
INSERT INTO `detail` VALUES ('83', '2019-05-06 17:03:32', '测试数据71', '2', '2', '2', '3', '85.00', '72.00', '208.00', null, null, null);
INSERT INTO `detail` VALUES ('84', '2019-05-13 17:03:32', '测试数据72', '2', '2', '2', '3', '0.00', '64.00', '144.00', null, null, null);
INSERT INTO `detail` VALUES ('85', '2019-05-20 17:03:32', '测试数据73', '2', '2', '2', '2', '75.00', '94.00', '125.00', null, null, null);
INSERT INTO `detail` VALUES ('86', '2019-05-27 17:03:32', '测试数据74', '2', '2', '2', '2', '67.00', '87.00', '105.00', null, null, null);
INSERT INTO `detail` VALUES ('87', '2019-06-03 17:03:32', '测试数据75', '2', '2', '2', '2', '33.00', '53.00', '85.00', null, null, null);
INSERT INTO `detail` VALUES ('88', '2019-06-10 17:03:32', '测试数据76', '2', '2', '2', '2', '84.00', '23.00', '146.00', null, null, null);
INSERT INTO `detail` VALUES ('89', '2019-06-17 17:03:32', '测试数据77', '2', '2', '2', '3', '94.00', '55.00', '185.00', null, null, null);
INSERT INTO `detail` VALUES ('90', '2019-06-24 17:03:32', '测试数据78', '2', '2', '2', '3', '93.00', '22.00', '256.00', null, null, null);
INSERT INTO `detail` VALUES ('91', '2019-07-01 17:03:32', '测试数据79', '2', '2', '2', '2', '48.00', '67.00', '237.00', null, null, null);
INSERT INTO `detail` VALUES ('92', '2019-07-08 17:03:32', '测试数据80', '2', '2', '2', '3', '55.00', '77.00', '215.00', null, null, null);
INSERT INTO `detail` VALUES ('93', '2019-07-15 17:03:32', '测试数据81', '2', '2', '2', '2', '46.00', '85.00', '176.00', null, null, null);
INSERT INTO `detail` VALUES ('94', '2019-07-22 17:03:32', '测试数据82', '2', '2', '2', '3', '69.00', '82.00', '163.00', null, null, null);
INSERT INTO `detail` VALUES ('95', '2019-07-29 17:03:32', '测试数据83', '2', '2', '2', '3', '61.00', '61.00', '163.00', null, null, null);
INSERT INTO `detail` VALUES ('96', '2019-08-05 17:03:32', '测试数据84', '2', '2', '2', '2', '60.00', '54.00', '169.00', null, null, null);
INSERT INTO `detail` VALUES ('97', '2019-08-12 17:03:32', '测试数据85', '2', '2', '2', '2', '66.00', '33.00', '202.00', null, null, null);
INSERT INTO `detail` VALUES ('98', '2019-08-19 17:03:32', '测试数据86', '2', '2', '2', '2', '64.00', '47.00', '219.00', null, null, null);
INSERT INTO `detail` VALUES ('99', '2019-08-26 17:03:32', '测试数据87', '2', '2', '2', '3', '80.00', '36.00', '263.00', null, null, null);
INSERT INTO `detail` VALUES ('100', '2019-09-02 17:03:32', '测试数据88', '2', '2', '2', '3', '8.00', '58.00', '213.00', null, null, null);
INSERT INTO `detail` VALUES ('101', '2019-09-09 17:03:32', '测试数据89', '2', '2', '2', '3', '95.00', '78.00', '230.00', null, null, null);
INSERT INTO `detail` VALUES ('102', '2019-09-16 17:03:32', '测试数据90', '2', '2', '2', '3', '2.00', '78.00', '154.00', null, null, null);
INSERT INTO `detail` VALUES ('103', '2019-09-23 17:03:32', '测试数据91', '2', '2', '2', '3', '3.00', '4.00', '153.00', null, null, null);
INSERT INTO `detail` VALUES ('104', '2019-09-30 17:03:32', '测试数据92', '2', '2', '2', '3', '45.00', '2.00', '196.00', null, null, null);
INSERT INTO `detail` VALUES ('105', '2019-10-07 17:03:32', '测试数据93', '2', '2', '2', '2', '33.00', '47.00', '182.00', null, null, null);
INSERT INTO `detail` VALUES ('106', '2019-10-14 17:03:32', '测试数据94', '2', '2', '2', '3', '28.00', '29.00', '181.00', null, null, null);
INSERT INTO `detail` VALUES ('107', '2019-10-21 17:03:32', '测试数据95', '2', '2', '2', '3', '83.00', '6.00', '258.00', null, null, null);
INSERT INTO `detail` VALUES ('108', '2019-10-28 17:03:32', '测试数据96', '2', '2', '2', '2', '66.00', '94.00', '230.00', null, null, null);
INSERT INTO `detail` VALUES ('109', '2019-11-04 17:03:32', '测试数据97', '2', '2', '2', '3', '77.00', '89.00', '218.00', '', null, null);
INSERT INTO `detail` VALUES ('110', '2019-11-11 17:03:32', '测试数据98', '2', '2', '2', '2', '85.00', '13.00', '290.00', '', null, null);

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `detail_id` int(11) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `d_id` (`detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of picture
-- ----------------------------

-- ----------------------------
-- Table structure for project
-- ----------------------------
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
                           `id` int(11) NOT NULL AUTO_INCREMENT,
                           `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `p_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of project
-- ----------------------------
INSERT INTO `project` VALUES ('2', '项目1');
INSERT INTO `project` VALUES ('1', '项目2');

-- ----------------------------
-- Table structure for rbac_group
-- ----------------------------
DROP TABLE IF EXISTS `rbac_group`;
CREATE TABLE `rbac_group` (
                              `id` int(11) NOT NULL AUTO_INCREMENT,
                              `name` varchar(64) NOT NULL,
                              `description` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of rbac_group
-- ----------------------------
INSERT INTO `rbac_group` VALUES ('1', '管理员组', '管理员拥有所有权限');
INSERT INTO `rbac_group` VALUES ('2', '用户组', '用户没有用户管理的权限');
INSERT INTO `rbac_group` VALUES ('3', '参观者组', '参观者只有查询权限');

-- ----------------------------
-- Table structure for rbac_group_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `rbac_group_role_relation`;
CREATE TABLE `rbac_group_role_relation` (
                                            `group_id` int(11) NOT NULL,
                                            `role_id` int(11) NOT NULL,
                                            PRIMARY KEY (`group_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of rbac_group_role_relation
-- ----------------------------
INSERT INTO `rbac_group_role_relation` VALUES ('1', '1');
INSERT INTO `rbac_group_role_relation` VALUES ('2', '2');
INSERT INTO `rbac_group_role_relation` VALUES ('3', '3');

-- ----------------------------
-- Table structure for rbac_permission
-- ----------------------------
DROP TABLE IF EXISTS `rbac_permission`;
CREATE TABLE `rbac_permission` (
                                   `id` int(11) NOT NULL AUTO_INCREMENT,
                                   `perm` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `name` varchar(64) NOT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `name` (`name`),
                                   UNIQUE KEY `perm` (`perm`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of rbac_permission
-- ----------------------------
INSERT INTO `rbac_permission` VALUES ('1', 'rbac:*', '用户管理权限');
INSERT INTO `rbac_permission` VALUES ('2', 'detail:read', '明细查询权限');
INSERT INTO `rbac_permission` VALUES ('3', 'detail:create', '明细新增权限');
INSERT INTO `rbac_permission` VALUES ('4', 'detail:delete', '明细删除权限');
INSERT INTO `rbac_permission` VALUES ('5', 'detail:update', '明细修改权限');
INSERT INTO `rbac_permission` VALUES ('6', 'statistics:read', '统计查询权限');
INSERT INTO `rbac_permission` VALUES ('7', 'option:read', '类别查询权限');
INSERT INTO `rbac_permission` VALUES ('8', 'option:create', '类别新增权限');
INSERT INTO `rbac_permission` VALUES ('9', 'option:update', '类别修改权限');
INSERT INTO `rbac_permission` VALUES ('10', 'excel:read', 'Excel导出权限');
INSERT INTO `rbac_permission` VALUES ('11', 'excel:create', 'Excel导入权限');

-- ----------------------------
-- Table structure for rbac_role
-- ----------------------------
DROP TABLE IF EXISTS `rbac_role`;
CREATE TABLE `rbac_role` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `name` varchar(64) NOT NULL,
                             `description` varchar(255) DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of rbac_role
-- ----------------------------
INSERT INTO `rbac_role` VALUES ('2', '用户', '');
INSERT INTO `rbac_role` VALUES ('3', '参观者', '');

-- ----------------------------
-- Table structure for rbac_role_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `rbac_role_permission_relation`;
CREATE TABLE `rbac_role_permission_relation` (
                                                 `role_id` int(11) NOT NULL,
                                                 `permission_id` int(11) NOT NULL,
                                                 PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of rbac_role_permission_relation
-- ----------------------------
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '2');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '3');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '4');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '5');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '6');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '7');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '8');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '9');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '10');
INSERT INTO `rbac_role_permission_relation` VALUES ('2', '11');
INSERT INTO `rbac_role_permission_relation` VALUES ('3', '2');
INSERT INTO `rbac_role_permission_relation` VALUES ('3', '6');
INSERT INTO `rbac_role_permission_relation` VALUES ('3', '7');

-- ----------------------------
-- Table structure for rbac_user
-- ----------------------------
DROP TABLE IF EXISTS `rbac_user`;
CREATE TABLE `rbac_user` (
                             `id` int(11) NOT NULL AUTO_INCREMENT,
                             `username` varchar(32) NOT NULL,
                             `password` varchar(32) NOT NULL,
                             `group_id` int(11) NOT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of rbac_user
-- ----------------------------
INSERT INTO `rbac_user` VALUES ('1', 'admin', 'b55209d0ffd2652ae7e4255fb6d661f1', '1');
INSERT INTO `rbac_user` VALUES ('2', 'user', '776f56487114cdabd79cc21505c9f25d', '2');
INSERT INTO `rbac_user` VALUES ('4', 'visitor', 'f663847b7e6a975347a60cd72b77a343', '3');

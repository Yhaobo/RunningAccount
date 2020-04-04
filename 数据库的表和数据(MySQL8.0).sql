-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: erp
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `a_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'账户1');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `c_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (4,'分类1');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `dep_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1,'部门1');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail`
--

DROP TABLE IF EXISTS `detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
  `d_balance` decimal(19,2) NOT NULL,
  PRIMARY KEY (`d_id`),
  UNIQUE KEY `d_date` (`d_date`),
  KEY `a_id` (`a_id`),
  KEY `p_id` (`p_id`),
  KEY `dep_id` (`dep_id`),
  KEY `c_id` (`c_id`),
  CONSTRAINT `detail_ibfk_1` FOREIGN KEY (`c_id`) REFERENCES `category` (`id`),
  CONSTRAINT `detail_ibfk_2` FOREIGN KEY (`a_id`) REFERENCES `account` (`id`),
  CONSTRAINT `detail_ibfk_3` FOREIGN KEY (`p_id`) REFERENCES `project` (`id`),
  CONSTRAINT `detail_ibfk_4` FOREIGN KEY (`dep_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail`
--

LOCK TABLES `detail` WRITE;
/*!40000 ALTER TABLE `detail` DISABLE KEYS */;
INSERT INTO `detail` VALUES (6,'2019-09-16 13:58:00','测试数据',1,1,1,4,0.00,0.00,150.00),(8,'2019-10-16 13:59:00','测试数据',1,1,1,4,0.00,0.00,150.00),(10,'2019-07-16 13:00:00','测试数据',1,1,1,4,0.00,0.00,150.00),(11,'2019-11-16 14:58:00','测试数据',1,1,1,4,0.00,0.00,150.00),(16,'2019-08-16 13:03:00','测试数据',1,1,1,4,0.00,0.00,150.00),(19,'2019-06-16 12:59:00','测试数据',1,1,1,4,0.00,0.00,150.00),(20,'2019-05-16 11:59:00','测试数据',1,1,1,4,0.00,0.00,150.00),(21,'2019-04-16 10:59:00','测试数据',1,1,1,4,0.00,0.00,150.00),(22,'2019-02-16 09:59:00','测试数据',1,1,1,4,0.00,0.00,100.00),(53,'2019-12-18 16:29:00','测试数据',1,1,1,4,0.00,0.00,150.00),(54,'2019-12-18 16:31:00','测试数据',1,1,1,4,0.00,0.00,150.00),(55,'2019-12-18 16:32:00','测试数据',1,1,1,4,0.00,0.00,150.00),(56,'2019-12-18 16:33:00','测试数据',1,1,1,4,0.00,0.00,150.00),(58,'2019-12-18 16:34:00','测试数据',1,1,1,4,0.00,0.00,150.00),(60,'2019-12-18 16:35:00','测试数据',1,1,1,4,0.00,0.00,150.00),(61,'2019-12-18 17:29:00','测试数据',1,1,1,4,0.00,0.00,150.00),(62,'2019-12-18 18:29:00','测试数据',1,1,1,4,0.00,0.00,150.00),(63,'2019-12-18 19:29:00','测试数据',1,1,1,4,0.00,0.00,150.00),(64,'2019-12-18 21:29:00','测试数据',1,1,1,4,0.00,0.00,150.00),(66,'2019-12-19 16:30:00','测试数据',1,1,1,4,0.00,0.00,150.00),(67,'2019-12-18 16:30:00','测试数据',1,1,1,4,0.00,0.00,150.00),(68,'2019-12-18 17:30:00','测试数据',1,1,1,4,0.00,0.00,150.00),(73,'2019-12-11 13:46:00','测试数据',1,1,1,4,0.00,0.00,150.00),(74,'2020-02-11 13:54:00','测试数据',1,1,1,4,0.00,0.00,50.00),(90,'2018-12-28 17:11:00','测试数据',1,1,1,4,0.00,0.00,0.00),(91,'2020-05-15 01:06:00','测试数据',9,1,1,4,0.00,0.00,50.00),(92,'2020-03-17 18:10:00','测试数据',9,1,1,4,0.00,0.00,50.00),(93,'2020-03-18 14:51:00','测试数据',9,1,1,4,0.00,0.00,50.00),(94,'2020-03-26 16:58:00','测试数据',9,1,1,4,0.00,0.00,50.00),(95,'2020-03-28 12:13:00','测试数据',9,1,1,4,0.00,0.00,50.00),(96,'2020-03-29 18:09:00','测试数据',1,1,1,4,0.00,0.00,50.00),(97,'2020-03-29 18:19:00','测试数据',1,1,1,4,0.00,0.00,50.00),(98,'2020-04-01 00:00:00','测试数据',1,1,1,4,100.00,0.00,150.00),(99,'2020-01-01 00:00:00','测试数据',9,1,1,4,0.00,100.00,50.00),(100,'2019-03-01 16:56:33','测试数据',9,1,1,4,50.00,0.00,150.00),(101,'2019-01-02 16:57:58','测试数据',9,1,1,4,100.00,0.00,100.00),(102,'2020-04-03 17:23:05','测试数据',9,1,1,4,0.00,100.00,50.00),(103,'2020-04-03 17:27:15','无',9,1,1,4,0.00,0.00,50.00),(104,'2020-04-03 17:28:27','无',1,1,1,4,0.00,0.00,50.00);
/*!40000 ALTER TABLE `detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `p_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'项目1'),(9,'项目2');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `u_id` int(11) NOT NULL AUTO_INCREMENT,
  `u_username` varchar(32) NOT NULL,
  `u_password` varchar(32) NOT NULL,
  `u_level` int(11) NOT NULL,
  PRIMARY KEY (`u_id`),
  UNIQUE KEY `u_username` (`u_username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','b55209d0ffd2652ae7e4255fb6d661f1',0),(2,'visitor','f663847b7e6a975347a60cd72b77a343',2),(3,'user','776f56487114cdabd79cc21505c9f25d',1);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-04 21:53:54

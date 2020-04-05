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
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail`
--

LOCK TABLES `detail` WRITE;
/*!40000 ALTER TABLE `detail` DISABLE KEYS */;
INSERT INTO `detail` VALUES (1,'2010-01-01 00:00:00','测试数据4',1,1,1,4,2.00,0.00,2.00),(2,'2010-02-01 00:00:00','测试数据8',1,1,1,4,8.00,1.00,9.00),(3,'2010-03-01 00:00:00','测试数据7',1,1,1,4,2.00,7.00,4.00),(4,'2010-04-01 00:00:00','测试数据10',9,1,1,4,5.00,0.00,9.00),(5,'2010-05-01 00:00:00','测试数据9',9,1,1,4,6.00,6.00,9.00),(6,'2010-06-01 00:00:00','测试数据1',1,1,1,4,5.00,0.00,14.00),(7,'2010-07-01 00:00:00','测试数据6',9,1,1,4,2.00,7.00,9.00),(8,'2010-08-01 00:00:00','测试数据1',9,1,1,4,2.00,9.00,2.00),(9,'2010-09-01 00:00:00','测试数据3',9,1,1,4,6.00,1.00,7.00),(10,'2010-10-01 00:00:00','测试数据2',1,1,1,4,9.00,1.00,15.00),(11,'2010-11-01 00:00:00','测试数据5',9,1,1,4,9.00,6.00,18.00),(12,'2010-12-01 00:00:00','测试数据1',9,1,1,4,5.00,6.00,17.00),(13,'2011-01-01 00:00:00','测试数据2',9,1,1,4,5.00,5.00,17.00),(14,'2011-02-01 00:00:00','测试数据6',9,1,1,4,3.00,2.00,18.00),(15,'2011-03-01 00:00:00','测试数据1',1,1,1,4,2.00,8.00,12.00),(16,'2011-04-01 00:00:00','测试数据8',9,1,1,4,5.00,5.00,12.00),(17,'2011-05-01 00:00:00','测试数据2',1,1,1,4,5.00,7.00,10.00),(18,'2011-06-01 00:00:00','测试数据5',1,1,1,4,0.00,4.00,6.00),(19,'2011-07-01 00:00:00','测试数据4',1,1,1,4,6.00,1.00,11.00),(20,'2011-08-01 00:00:00','测试数据5',1,1,1,4,8.00,8.00,11.00),(21,'2011-09-01 00:00:00','测试数据9',9,1,1,4,9.00,9.00,11.00),(22,'2011-10-01 00:00:00','测试数据4',1,1,1,4,0.00,6.00,5.00),(23,'2011-11-01 00:00:00','测试数据7',1,1,1,4,3.00,2.00,6.00),(24,'2011-12-01 00:00:00','测试数据9',1,1,1,4,9.00,4.00,11.00),(25,'2012-01-01 00:00:00','测试数据2',1,1,1,4,0.00,4.00,7.00),(26,'2012-02-01 00:00:00','测试数据9',1,1,1,4,8.00,8.00,7.00),(27,'2012-03-01 00:00:00','测试数据1',9,1,1,4,1.00,4.00,4.00),(28,'2012-04-01 00:00:00','测试数据1',1,1,1,4,3.00,9.00,-2.00),(29,'2012-05-01 00:00:00','测试数据4',1,1,1,4,4.00,2.00,0.00),(30,'2012-06-01 00:00:00','测试数据10',1,1,1,4,7.00,6.00,1.00),(31,'2012-07-01 00:00:00','测试数据8',1,1,1,4,5.00,4.00,2.00),(32,'2012-08-01 00:00:00','测试数据9',1,1,1,4,7.00,6.00,3.00),(33,'2012-09-01 00:00:00','测试数据3',1,1,1,4,8.00,1.00,10.00),(34,'2012-10-01 00:00:00','测试数据3',9,1,1,4,8.00,8.00,10.00),(35,'2012-11-01 00:00:00','测试数据7',9,1,1,4,3.00,0.00,13.00),(36,'2012-12-01 00:00:00','测试数据9',9,1,1,4,7.00,1.00,19.00),(37,'2013-01-01 00:00:00','测试数据4',9,1,1,4,4.00,7.00,16.00),(38,'2013-02-01 00:00:00','测试数据1',9,1,1,4,2.00,6.00,12.00),(39,'2013-03-01 00:00:00','测试数据3',9,1,1,4,6.00,6.00,12.00),(40,'2013-04-01 00:00:00','测试数据9',9,1,1,4,0.00,6.00,6.00),(41,'2013-05-01 00:00:00','测试数据7',1,1,1,4,4.00,7.00,3.00),(42,'2013-06-01 00:00:00','测试数据3',9,1,1,4,8.00,2.00,9.00),(43,'2013-07-01 00:00:00','测试数据6',9,1,1,4,2.00,4.00,7.00),(44,'2013-08-01 00:00:00','测试数据9',9,1,1,4,5.00,0.00,12.00),(45,'2013-09-01 00:00:00','测试数据8',9,1,1,4,0.00,6.00,6.00),(46,'2013-10-01 00:00:00','测试数据2',9,1,1,4,1.00,7.00,0.00),(47,'2013-11-01 00:00:00','测试数据8',9,1,1,4,1.00,1.00,0.00),(48,'2013-12-01 00:00:00','测试数据6',1,1,1,4,4.00,3.00,1.00),(49,'2014-01-01 00:00:00','测试数据9',1,1,1,4,0.00,2.00,-1.00),(50,'2014-02-01 00:00:00','测试数据10',9,1,1,4,7.00,6.00,0.00),(51,'2014-03-01 00:00:00','测试数据7',1,1,1,4,2.00,9.00,-7.00),(52,'2014-04-01 00:00:00','测试数据1',1,1,1,4,6.00,2.00,-3.00),(53,'2014-05-01 00:00:00','测试数据8',1,1,1,4,4.00,9.00,-8.00),(54,'2014-06-01 00:00:00','测试数据2',1,1,1,4,4.00,5.00,-9.00),(55,'2014-07-01 00:00:00','测试数据8',9,1,1,4,8.00,4.00,-5.00),(56,'2014-08-01 00:00:00','测试数据4',9,1,1,4,6.00,2.00,-1.00),(57,'2014-09-01 00:00:00','测试数据3',1,1,1,4,2.00,3.00,-2.00),(58,'2014-10-01 00:00:00','测试数据1',9,1,1,4,3.00,4.00,-3.00),(59,'2014-11-01 00:00:00','测试数据9',9,1,1,4,8.00,5.00,0.00),(60,'2014-12-01 00:00:00','测试数据10',1,1,1,4,1.00,0.00,1.00),(61,'2015-01-01 00:00:00','测试数据10',1,1,1,4,3.00,7.00,-3.00),(62,'2015-02-01 00:00:00','测试数据2',1,1,1,4,1.00,5.00,-7.00),(63,'2015-03-01 00:00:00','测试数据5',1,1,1,4,2.00,9.00,-14.00),(64,'2015-04-01 00:00:00','测试数据5',1,1,1,4,8.00,5.00,-11.00),(65,'2015-05-01 00:00:00','测试数据3',9,1,1,4,0.00,2.00,-13.00),(66,'2015-06-01 00:00:00','测试数据10',9,1,1,4,3.00,4.00,-14.00),(67,'2015-07-01 00:00:00','测试数据6',1,1,1,4,6.00,8.00,-16.00),(68,'2015-08-01 00:00:00','测试数据9',1,1,1,4,6.00,8.00,-18.00),(69,'2015-09-01 00:00:00','测试数据3',1,1,1,4,0.00,2.00,-20.00),(70,'2015-10-01 00:00:00','测试数据3',1,1,1,4,8.00,0.00,-12.00),(71,'2015-11-01 00:00:00','测试数据2',9,1,1,4,1.00,0.00,-11.00),(72,'2015-12-01 00:00:00','测试数据4',1,1,1,4,0.00,4.00,-15.00),(73,'2016-01-01 00:00:00','测试数据7',1,1,1,4,4.00,7.00,-18.00),(74,'2016-02-01 00:00:00','测试数据7',1,1,1,4,9.00,0.00,-9.00),(75,'2016-03-01 00:00:00','测试数据2',1,1,1,4,5.00,2.00,-6.00),(76,'2016-04-01 00:00:00','测试数据4',1,1,1,4,8.00,6.00,-4.00),(77,'2016-05-01 00:00:00','测试数据2',1,1,1,4,5.00,1.00,0.00),(78,'2016-06-01 00:00:00','测试数据5',9,1,1,4,7.00,2.00,5.00),(79,'2016-07-01 00:00:00','测试数据3',1,1,1,4,2.00,4.00,3.00),(80,'2016-08-01 00:00:00','测试数据5',9,1,1,4,7.00,3.00,7.00),(81,'2016-09-01 00:00:00','测试数据1',9,1,1,4,1.00,0.00,8.00),(82,'2016-10-01 00:00:00','测试数据3',9,1,1,4,4.00,3.00,9.00),(83,'2016-11-01 00:00:00','测试数据2',9,1,1,4,9.00,1.00,17.00),(84,'2016-12-01 00:00:00','测试数据6',9,1,1,4,9.00,2.00,24.00),(85,'2017-01-01 00:00:00','测试数据1',9,1,1,4,1.00,6.00,19.00),(86,'2017-02-01 00:00:00','测试数据3',1,1,1,4,7.00,3.00,23.00),(87,'2017-03-01 00:00:00','测试数据4',9,1,1,4,1.00,2.00,22.00),(88,'2017-04-01 00:00:00','测试数据3',1,1,1,4,9.00,9.00,22.00),(89,'2017-05-01 00:00:00','测试数据10',9,1,1,4,5.00,2.00,25.00),(90,'2017-06-01 00:00:00','测试数据5',1,1,1,4,8.00,9.00,24.00),(91,'2017-07-01 00:00:00','测试数据2',9,1,1,4,8.00,4.00,28.00),(92,'2017-08-01 00:00:00','测试数据5',1,1,1,4,9.00,1.00,36.00),(93,'2017-09-01 00:00:00','测试数据5',1,1,1,4,5.00,8.00,33.00),(94,'2017-10-01 00:00:00','测试数据8',9,1,1,4,0.00,9.00,24.00),(95,'2017-11-01 00:00:00','测试数据8',1,1,1,4,9.00,9.00,24.00),(96,'2017-12-01 00:00:00','测试数据6',9,1,1,4,8.00,9.00,23.00),(97,'2018-01-01 00:00:00','测试数据1',9,1,1,4,1.00,0.00,24.00),(98,'2018-02-01 00:00:00','测试数据10',1,1,1,4,4.00,4.00,24.00),(99,'2018-03-01 00:00:00','测试数据1',9,1,1,4,3.00,0.00,27.00),(100,'2018-04-01 00:00:00','测试数据9',1,1,1,4,7.00,1.00,33.00),(101,'2018-05-01 00:00:00','测试数据7',9,1,1,4,0.00,6.00,27.00),(102,'2018-06-01 00:00:00','测试数据4',9,1,1,4,9.00,6.00,30.00),(103,'2018-07-01 00:00:00','测试数据2',1,1,1,4,2.00,8.00,24.00),(104,'2018-08-01 00:00:00','测试数据4',9,1,1,4,4.00,3.00,25.00),(105,'2018-09-01 00:00:00','测试数据8',9,1,1,4,5.00,8.00,22.00),(106,'2018-10-01 00:00:00','测试数据1',9,1,1,4,7.00,3.00,26.00),(107,'2018-11-01 00:00:00','测试数据5',9,1,1,4,0.00,1.00,25.00),(108,'2018-12-01 00:00:00','测试数据3',9,1,1,4,4.00,6.00,23.00),(109,'2019-01-01 00:00:00','测试数据3',1,1,1,4,7.00,8.00,22.00),(110,'2019-02-01 00:00:00','测试数据7',1,1,1,4,8.00,1.00,29.00),(111,'2019-03-01 00:00:00','测试数据6',9,1,1,4,6.00,2.00,33.00),(112,'2019-04-01 00:00:00','测试数据5',9,1,1,4,9.00,3.00,39.00),(113,'2019-05-01 00:00:00','测试数据8',9,1,1,4,5.00,0.00,44.00),(114,'2019-06-01 00:00:00','测试数据9',1,1,1,4,2.00,7.00,39.00),(115,'2019-07-01 00:00:00','测试数据10',1,1,1,4,1.00,6.00,34.00),(116,'2019-08-01 00:00:00','测试数据7',1,1,1,4,1.00,0.00,35.00),(117,'2019-09-01 00:00:00','测试数据9',9,1,1,4,4.00,0.00,39.00),(118,'2019-10-01 00:00:00','测试数据8',9,1,1,4,5.00,1.00,43.00),(119,'2019-11-01 00:00:00','测试数据6',9,1,1,4,0.00,7.00,36.00),(120,'2019-12-01 00:00:00','测试数据7',9,1,1,4,8.00,0.00,44.00),(121,'2020-01-01 17:04:50','无',1,1,1,4,0.00,0.00,44.00);
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

-- Dump completed on 2020-04-05 17:08:18

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'花明天的钱圆今天的梦'),(2,'账户1');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'996.669.ICU'),(2,'分类1'),(3,'分类2');
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
INSERT INTO `department` VALUES (1,'一个人就是一支军队'),(2,'部门1');
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detail`
--

DROP TABLE IF EXISTS `detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `digest` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `project_id` int(11) NOT NULL,
  `account_id` int(11) NOT NULL,
  `department_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `earning` decimal(19,2) DEFAULT NULL,
  `expense` decimal(19,2) DEFAULT NULL,
  `balance` decimal(19,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_date` (`date` DESC),
  KEY `idx_category_department_account_project` (`category_id`,`department_id`,`account_id`,`project_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=118 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detail`
--

LOCK TABLES `detail` WRITE;
/*!40000 ALTER TABLE `detail` DISABLE KEYS */;
INSERT INTO `detail` VALUES (13,'2018-01-01 17:03:32','测试数据1',2,2,2,2,15.00,61.00,-46.00),(14,'2018-01-08 17:03:32','测试数据2',2,2,2,2,91.00,76.00,-31.00),(15,'2018-01-15 17:03:32','测试数据3',2,2,2,2,59.00,20.00,8.00),(16,'2018-01-22 17:03:32','测试数据4',2,2,2,2,79.00,25.00,62.00),(17,'2018-01-29 17:03:32','测试数据5',2,2,2,3,31.00,24.00,69.00),(18,'2018-02-05 17:03:32','测试数据6',2,2,2,3,77.00,98.00,48.00),(19,'2018-02-12 17:03:32','测试数据7',2,2,2,3,60.00,34.00,74.00),(20,'2018-02-19 17:03:32','测试数据8',2,2,2,2,18.00,47.00,45.00),(21,'2018-02-26 17:03:32','测试数据9',2,2,2,3,88.00,8.00,125.00),(22,'2018-03-05 17:03:32','测试数据10',2,2,2,2,37.00,45.00,117.00),(23,'2018-03-12 17:03:32','测试数据11',2,2,2,2,2.00,20.00,99.00),(24,'2018-03-19 17:03:32','测试数据12',2,2,2,3,88.00,29.00,158.00),(25,'2018-03-26 17:03:32','测试数据13',2,2,2,2,36.00,44.00,150.00),(26,'2018-04-02 17:03:32','测试数据14',2,2,2,3,17.00,79.00,88.00),(27,'2018-04-09 17:03:32','测试数据15',2,2,2,3,54.00,16.00,126.00),(28,'2018-04-16 17:03:32','测试数据16',2,2,2,3,71.00,16.00,181.00),(29,'2018-04-23 17:03:32','测试数据17',2,2,2,3,89.00,37.00,233.00),(30,'2018-04-30 17:03:32','测试数据18',2,2,2,2,94.00,77.00,250.00),(31,'2018-05-07 17:03:32','测试数据19',2,2,2,2,72.00,79.00,243.00),(32,'2018-05-14 17:03:32','测试数据20',2,2,2,2,45.00,24.00,264.00),(33,'2018-05-21 17:03:32','测试数据21',2,2,2,3,19.00,44.00,239.00),(34,'2018-05-28 17:03:32','测试数据22',2,2,2,3,27.00,46.00,220.00),(35,'2018-06-04 17:03:32','测试数据23',2,2,2,3,10.00,95.00,135.00),(36,'2018-06-11 17:03:32','测试数据24',2,2,2,2,48.00,82.00,101.00),(37,'2018-06-18 17:03:32','测试数据25',2,2,2,3,66.00,22.00,145.00),(38,'2018-06-25 17:03:32','测试数据26',2,2,2,3,47.00,76.00,116.00),(39,'2018-07-02 17:03:32','测试数据27',2,2,2,2,12.00,36.00,92.00),(40,'2018-07-09 17:03:32','测试数据28',2,2,2,2,82.00,26.00,148.00),(41,'2018-07-16 17:03:32','测试数据29',2,2,2,3,2.00,17.00,133.00),(42,'2018-07-23 17:03:32','测试数据30',2,2,2,2,65.00,43.00,155.00),(43,'2018-07-30 17:03:32','测试数据31',2,2,2,3,45.00,12.00,188.00),(44,'2018-08-06 17:03:32','测试数据32',2,2,2,2,29.00,87.00,130.00),(45,'2018-08-13 17:03:32','测试数据33',2,2,2,2,0.00,96.00,34.00),(46,'2018-08-20 17:03:32','测试数据34',2,2,2,2,66.00,33.00,67.00),(47,'2018-08-27 17:03:32','测试数据35',2,2,2,3,26.00,82.00,11.00),(48,'2018-09-03 17:03:32','测试数据36',2,2,2,3,9.00,2.00,18.00),(49,'2018-09-10 17:03:32','测试数据37',2,2,2,2,58.00,15.00,61.00),(50,'2018-09-17 17:03:32','测试数据38',2,2,2,2,9.00,82.00,-12.00),(51,'2018-09-24 17:03:32','测试数据39',2,2,2,2,6.00,84.00,-90.00),(52,'2018-10-01 17:03:32','测试数据40',2,2,2,2,44.00,65.00,-111.00),(53,'2018-10-08 17:03:32','测试数据41',2,2,2,2,87.00,18.00,-42.00),(54,'2018-10-15 17:03:32','测试数据42',2,2,2,2,3.00,76.00,-115.00),(55,'2018-10-22 17:03:32','测试数据43',2,2,2,2,33.00,17.00,-99.00),(56,'2018-10-29 17:03:32','测试数据44',2,2,2,3,76.00,38.00,-61.00),(57,'2018-11-05 17:03:32','测试数据45',2,2,2,2,92.00,89.00,-58.00),(58,'2018-11-12 17:03:32','测试数据46',2,2,2,2,89.00,15.00,16.00),(59,'2018-11-19 17:03:32','测试数据47',2,2,2,3,57.00,30.00,43.00),(60,'2018-11-26 17:03:32','测试数据48',2,2,2,2,72.00,37.00,78.00),(61,'2018-12-03 17:03:32','测试数据49',2,2,2,3,3.00,64.00,17.00),(62,'2018-12-10 17:03:32','测试数据50',2,2,2,2,91.00,72.00,36.00),(63,'2018-12-17 17:03:32','测试数据51',2,2,2,2,70.00,41.00,65.00),(64,'2018-12-24 17:03:32','测试数据52',2,2,2,2,25.00,92.00,-2.00),(65,'2018-12-31 17:03:32','测试数据53',2,2,2,2,90.00,24.00,64.00),(66,'2019-01-07 17:03:32','测试数据54',2,2,2,2,84.00,19.00,129.00),(67,'2019-01-14 17:03:32','测试数据55',2,2,2,3,14.00,64.00,79.00),(68,'2019-01-21 17:03:32','测试数据56',2,2,2,2,7.00,21.00,65.00),(69,'2019-01-28 17:03:32','测试数据57',2,2,2,3,72.00,61.00,76.00),(70,'2019-02-04 17:03:32','测试数据58',2,2,2,3,5.00,52.00,29.00),(71,'2019-02-11 17:03:32','测试数据59',2,2,2,2,70.00,12.00,87.00),(72,'2019-02-18 17:03:32','测试数据60',2,2,2,3,68.00,29.00,126.00),(73,'2019-02-25 17:03:32','测试数据61',2,2,2,3,29.00,21.00,134.00),(74,'2019-03-04 17:03:32','测试数据62',2,2,2,2,12.00,59.00,87.00),(75,'2019-03-11 17:03:32','测试数据63',2,2,2,3,93.00,50.00,130.00),(76,'2019-03-18 17:03:32','测试数据64',2,2,2,2,66.00,76.00,120.00),(77,'2019-03-25 17:03:32','测试数据65',2,2,2,3,85.00,78.00,127.00),(78,'2019-04-01 17:03:32','测试数据66',2,2,2,2,8.00,44.00,91.00),(79,'2019-04-08 17:03:32','测试数据67',2,2,2,2,47.00,35.00,103.00),(80,'2019-04-15 17:03:32','测试数据68',2,2,2,2,61.00,76.00,88.00),(81,'2019-04-22 17:03:32','测试数据69',2,2,2,2,84.00,24.00,148.00),(82,'2019-04-29 17:03:32','测试数据70',2,2,2,2,98.00,51.00,195.00),(83,'2019-05-06 17:03:32','测试数据71',2,2,2,3,85.00,72.00,208.00),(84,'2019-05-13 17:03:32','测试数据72',2,2,2,3,0.00,64.00,144.00),(85,'2019-05-20 17:03:32','测试数据73',2,2,2,2,75.00,94.00,125.00),(86,'2019-05-27 17:03:32','测试数据74',2,2,2,2,67.00,87.00,105.00),(87,'2019-06-03 17:03:32','测试数据75',2,2,2,2,33.00,53.00,85.00),(88,'2019-06-10 17:03:32','测试数据76',2,2,2,2,84.00,23.00,146.00),(89,'2019-06-17 17:03:32','测试数据77',2,2,2,3,94.00,55.00,185.00),(90,'2019-06-24 17:03:32','测试数据78',2,2,2,3,93.00,22.00,256.00),(91,'2019-07-01 17:03:32','测试数据79',2,2,2,2,48.00,67.00,237.00),(92,'2019-07-08 17:03:32','测试数据80',2,2,2,3,55.00,77.00,215.00),(93,'2019-07-15 17:03:32','测试数据81',2,2,2,2,46.00,85.00,176.00),(94,'2019-07-22 17:03:32','测试数据82',2,2,2,3,69.00,82.00,163.00),(95,'2019-07-29 17:03:32','测试数据83',2,2,2,3,61.00,61.00,163.00),(96,'2019-08-05 17:03:32','测试数据84',2,2,2,2,60.00,54.00,169.00),(97,'2019-08-12 17:03:32','测试数据85',2,2,2,2,66.00,33.00,202.00),(98,'2019-08-19 17:03:32','测试数据86',2,2,2,2,64.00,47.00,219.00),(99,'2019-08-26 17:03:32','测试数据87',2,2,2,3,80.00,36.00,263.00),(100,'2019-09-02 17:03:32','测试数据88',2,2,2,3,8.00,58.00,213.00),(101,'2019-09-09 17:03:32','测试数据89',2,2,2,3,95.00,78.00,230.00),(102,'2019-09-16 17:03:32','测试数据90',2,2,2,3,2.00,78.00,154.00),(103,'2019-09-23 17:03:32','测试数据91',2,2,2,3,3.00,4.00,153.00),(104,'2019-09-30 17:03:32','测试数据92',2,2,2,3,45.00,2.00,196.00),(105,'2019-10-07 17:03:32','测试数据93',2,2,2,2,33.00,47.00,182.00),(106,'2019-10-14 17:03:32','测试数据94',2,2,2,3,28.00,29.00,181.00),(107,'2019-10-21 17:03:32','测试数据95',2,2,2,3,83.00,6.00,258.00),(108,'2019-10-28 17:03:32','测试数据96',2,2,2,2,66.00,94.00,230.00),(109,'2019-11-04 17:03:32','测试数据97',2,2,2,3,77.00,89.00,218.00),(110,'2019-11-11 17:03:32','测试数据98',2,2,2,2,85.00,13.00,290.00),(111,'2019-11-18 17:03:32','测试数据99',2,2,2,3,6.00,57.00,239.00),(112,'2019-11-25 17:03:32','测试数据100',2,2,2,2,58.00,38.00,259.00),(114,'2020-10-16 22:15:28','无',1,1,1,1,999.99,0.00,-8989.01),(117,'2020-08-29 12:00:00','无',1,1,1,1,10.00,9999.00,-9989.00);
/*!40000 ALTER TABLE `detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uri` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `detail_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `d_id` (`detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture`
--

LOCK TABLES `picture` WRITE;
/*!40000 ALTER TABLE `picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `picture` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (1,'人类补完计划'),(2,'项目1');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `level` tinyint(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','b55209d0ffd2652ae7e4255fb6d661f1',0),(2,'user','776f56487114cdabd79cc21505c9f25d',1),(3,'visitor','f663847b7e6a975347a60cd72b77a343',2);
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

-- Dump completed on 2020-10-18 20:18:09

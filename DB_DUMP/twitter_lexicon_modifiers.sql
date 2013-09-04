CREATE DATABASE  IF NOT EXISTS `twitter_lexicon` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `twitter_lexicon`;
-- MySQL dump 10.13  Distrib 5.6.11, for Win32 (x86)
--
-- Host: localhost    Database: twitter_lexicon
-- ------------------------------------------------------
-- Server version	5.6.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `modifiers`
--

DROP TABLE IF EXISTS `modifiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modifiers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modifier` varchar(15) NOT NULL,
  `value` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modifiers`
--

LOCK TABLES `modifiers` WRITE;
/*!40000 ALTER TABLE `modifiers` DISABLE KEYS */;
INSERT INTO `modifiers` VALUES (127,'very',2),(128,'absolutely',2),(129,'greatly',2),(130,'extremely',2),(131,'really',2),(132,'profoundly',2),(133,'acutely',2),(134,'almighty',2),(135,'awfully',2),(136,'deeply',2),(137,'exceedingly',2),(138,'exceptionally',2),(139,'excessively',2),(140,'exorbitantly',2),(141,'extra',2),(142,'extraordinarily',2),(143,'far',2),(144,'highly',2),(145,'hugely',2),(146,'immensely',2),(147,'immoderately',2),(148,'inordinately',2),(149,'intensely',2),(150,'markedly',2),(151,'mortally',2),(152,'notably',2),(153,'overly',2),(154,'radically',2),(155,'remarkably',2),(156,'seriously',2),(157,'severely',2),(158,'strikingly',2),(159,'super',2),(160,'surpassingly',2),(161,'terribly',2),(162,'terrifically',2),(163,'too',2),(164,'totally',2),(165,'ultra',2),(166,'uncommonly',2),(167,'unduly',2),(168,'unusually',2),(169,'moderately',0.5),(170,'mildly',0.5),(171,'little',0.5),(172,'averagely',0.5),(173,'enough',0.5),(174,'fairly',0.5),(175,'gently',0.5),(176,'passably',0.5),(177,'pretty',0.5),(178,'quite',0.5),(179,'rather',0.5),(180,'reasonably',0.5),(181,'slightly',0.5),(182,'some',0.5),(183,'somewhat',0.5),(184,'temperately',0.5),(185,'tolerably',0.5),(186,'tolerantly',0.5),(190,'so',2);
/*!40000 ALTER TABLE `modifiers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-09-03 22:47:16

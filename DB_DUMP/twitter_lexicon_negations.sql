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
-- Table structure for table `negations`
--

DROP TABLE IF EXISTS `negations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `negations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `negation` varchar(15) NOT NULL,
  `value` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `negations`
--

LOCK TABLES `negations` WRITE;
/*!40000 ALTER TABLE `negations` DISABLE KEYS */;
INSERT INTO `negations` VALUES (1,'not',-1),(2,'never',-1),(3,'can\'t',-1),(4,'don\'t',-1),(5,'doesn\'t',-1),(6,'wouldn\'t',-1),(7,'won\'t',-1),(8,'wasn\'t',-1),(9,'weren\'t',-1),(10,'couldn\'t',-1),(11,'shouldn\'t',-1),(12,'didn\'t',-1),(13,'no',-1),(14,'isn\'t',-1),(15,'can \' t',-1),(16,'don \' t',-1),(17,'doesn \' t',-1),(18,'wouldn \' t',-1),(19,'wasn \' t',-1),(20,'weren \' t',-1),(21,'couldn \' t',-1),(22,'shouldn \' t',-1),(23,'ditn \' t',-1),(24,'isn \' t',-1),(25,'n\'t',-1),(26,'ain\'t',-1),(27,'aint',-1),(28,'cant',-1),(29,'dont',-1),(30,'doesnt',-1),(31,'wouldnt',-1),(32,'wasnt',-1),(33,'werent',-1),(34,'wont',-1),(35,'couldnt',-1),(36,'shouldnt',-1),(37,'didnt',-1),(38,'isnt',-1);
/*!40000 ALTER TABLE `negations` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-09-03 22:47:14

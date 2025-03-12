CREATE DATABASE  IF NOT EXISTS `megacitycab_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `megacitycab_db`;
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: megacitycab_db
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `billNumber` int(11) NOT NULL AUTO_INCREMENT,
  `bookingNumber` int(11) NOT NULL,
  `baseAmount` double NOT NULL,
  `discountAmount` double DEFAULT 0,
  `taxAmount` double NOT NULL,
  `totalFare` double NOT NULL,
  `billDate` datetime DEFAULT current_timestamp(),
  `paymentStatus` enum('PENDING','PAID','FAILED') DEFAULT 'PENDING',
  PRIMARY KEY (`billNumber`),
  KEY `bill_ibfk_1` (`bookingNumber`),
  CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`bookingNumber`) REFERENCES `booking` (`bookingNumber`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (21,123,1200,0,120,1320,'2025-03-12 04:44:58','PENDING'),(22,124,1200,0,120,47760,'2025-03-12 04:55:10','PENDING'),(23,124,1200,2000,0.1,46840,'2025-03-12 05:25:30','PENDING'),(24,123,1200,500,0.1,6700,'2025-03-12 09:53:46','PENDING'),(25,127,1200,0,120,14400,'2025-03-12 11:40:30','PENDING');
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `bookingNumber` int(11) NOT NULL AUTO_INCREMENT,
  `customerID` int(11) NOT NULL,
  `bookingDateTime` datetime DEFAULT current_timestamp(),
  `pickupLocation` varchar(255) NOT NULL,
  `destination` varchar(255) NOT NULL,
  `distance` double NOT NULL,
  `status` enum('PENDING','CONFIRMED','CANCELLED','COMPLETED') DEFAULT 'PENDING',
  `cabID` int(11) DEFAULT NULL,
  `driverID` int(11) DEFAULT NULL,
  PRIMARY KEY (`bookingNumber`),
  KEY `customerID` (`customerID`),
  KEY `cabID` (`cabID`),
  KEY `driverID` (`driverID`),
  KEY `idx_booking_status` (`status`),
  CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `customer` (`customerID`),
  CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`cabID`) REFERENCES `cab` (`cabID`),
  CONSTRAINT `booking_ibfk_3` FOREIGN KEY (`driverID`) REFERENCES `driver` (`driverID`)
) ENGINE=InnoDB AUTO_INCREMENT=128 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES (123,144,'2025-03-27 09:34:00','Kandy','Kandy',50,'COMPLETED',45,561),(124,144,'2025-03-22 10:00:00','Colombo','Jaffna',397,'COMPLETED',45,561),(126,145,'2025-03-12 09:52:09','Kandy','Galle',52,'CONFIRMED',45,561),(127,144,'2025-03-13 16:44:00','Colombo','Galle',119,'COMPLETED',46,561);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cab`
--

DROP TABLE IF EXISTS `cab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cab` (
  `cabID` int(11) NOT NULL AUTO_INCREMENT,
  `vehicleNumber` varchar(20) NOT NULL,
  `model` varchar(100) NOT NULL,
  `category` enum('MINI','SEDAN','SUV','LUXURY') NOT NULL,
  `capacity` int(11) NOT NULL,
  `currentLocation` varchar(255) DEFAULT NULL,
  `status` enum('AVAILABLE','BUSY','MAINTENANCE') DEFAULT 'AVAILABLE',
  `lastUpdated` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `driverID` int(11) DEFAULT NULL,
  PRIMARY KEY (`cabID`),
  UNIQUE KEY `vehicleNumber` (`vehicleNumber`),
  KEY `driverID` (`driverID`),
  KEY `idx_cab_status` (`status`),
  CONSTRAINT `cab_ibfk_1` FOREIGN KEY (`driverID`) REFERENCES `driver` (`driverID`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cab`
--

LOCK TABLES `cab` WRITE;
/*!40000 ALTER TABLE `cab` DISABLE KEYS */;
INSERT INTO `cab` VALUES (45,'CAB-1234','Hyundai Creta','SUV',7,'kandy','AVAILABLE','2025-03-11 18:31:47',561),(46,'CFAB-1234','Toyota','SEDAN',4,'Colombo','AVAILABLE','2025-03-12 09:53:12',561);
/*!40000 ALTER TABLE `cab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `customerID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `mobileNumber` varchar(15) NOT NULL,
  `phoneNumber` varchar(45) DEFAULT NULL,
  `registrationDate` datetime DEFAULT current_timestamp(),
  `status` enum('ACTIVE','INACTIVE','BLOCKED') DEFAULT 'ACTIVE',
  `userID` int(11) DEFAULT NULL,
  PRIMARY KEY (`customerID`),
  KEY `userID` (`userID`),
  KEY `idx_customer_mobile` (`mobileNumber`),
  CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (144,'Customer','','123456789','123456789','2025-03-11 17:57:20','ACTIVE',338),(145,'testCustomer','','741852963','741852963','2025-03-12 07:28:58','ACTIVE',340);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `driverID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `licenseNumber` varchar(50) NOT NULL,
  `contactNumber` varchar(15) NOT NULL,
  `phoneNumber` varchar(15) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `status` enum('AVAILABLE','BUSY','OFFLINE') DEFAULT 'AVAILABLE',
  `userID` int(11) DEFAULT NULL,
  PRIMARY KEY (`driverID`),
  UNIQUE KEY `licenseNumber` (`licenseNumber`),
  KEY `userID` (`userID`),
  KEY `idx_driver_status` (`status`),
  CONSTRAINT `driver_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=562 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (561,'Driver','T-E-S-T','741852963','741852963','kandy','AVAILABLE',339);
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `systemhelp`
--

DROP TABLE IF EXISTS `systemhelp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `systemhelp` (
  `helpID` int(11) NOT NULL AUTO_INCREMENT,
  `topic` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `lastUpdate` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`helpID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `systemhelp`
--

LOCK TABLES `systemhelp` WRITE;
/*!40000 ALTER TABLE `systemhelp` DISABLE KEYS */;
/*!40000 ALTER TABLE `systemhelp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traiffplan`
--

DROP TABLE IF EXISTS `traiffplan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traiffplan` (
  `planID` int(11) NOT NULL AUTO_INCREMENT,
  `baseRate` double NOT NULL,
  `perKmRate` double NOT NULL,
  `category` enum('MINI','SEDAN','SUV','LUXURY') NOT NULL,
  `waitingChargePerHour` double NOT NULL,
  `peakHourMultiplier` double DEFAULT 1,
  `category_type` varchar(50) NOT NULL,
  PRIMARY KEY (`planID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traiffplan`
--

LOCK TABLES `traiffplan` WRITE;
/*!40000 ALTER TABLE `traiffplan` DISABLE KEYS */;
/*!40000 ALTER TABLE `traiffplan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `role` enum('CUSTOMER','ADMIN','DRIVER') NOT NULL,
  `lastLoginDate` datetime DEFAULT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=341 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (337,'admin','$2a$10$snuUQl98oO9VR4PQydwyFu1gGE/des8cmLSBLfSXba/O0Cj89xrve','admin@gmail.com','ADMIN','2025-03-11 17:56:29'),(338,'Customer','$2a$10$hz9AzXB2ImiYlGI9VnMS4uWSFJ/9glN4gAS2xtxT1O2GZy/ycbdp6','customer@gmail.com','CUSTOMER','2025-03-11 17:57:20'),(339,'Driver','$2a$10$1VYOLcXShbhGFSibrAmU/OF4xB40oqUOnHNVezZ4juO6LBDEgKoki','driver@gmail.com','DRIVER','2025-03-11 17:59:00'),(340,'testCustomer','$2a$10$KQuCNJ8OCiwo.h0rhGgybelWd9ygv9nC.vVjwDlqmgpLo0f86CpSG','test@gmail.com','CUSTOMER','2025-03-12 07:28:58');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-12 13:43:19

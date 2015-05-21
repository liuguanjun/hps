-- MySQL dump 10.13  Distrib 5.6.20, for osx10.8 (x86_64)
--
-- Host: localhost    Database: hps
-- ------------------------------------------------------
-- Server version	5.6.20

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
-- Table structure for table `hps_electric_unit`
--
drop table if exists `hps_electirc_zhinajin_setting`;
DROP TABLE IF EXISTS `hps_electric_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hps_electric_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_user_name` varchar(50) DEFAULT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  `last_update_user_id` bigint(20) DEFAULT NULL,
  `last_update_user_name` varchar(50) DEFAULT NULL,
  `ceng` varchar(255) DEFAULT NULL,
  `danyuan` varchar(255) DEFAULT NULL,
  `door_no` varchar(255) DEFAULT NULL,
  `end1` int(11) DEFAULT NULL,
  `end2` int(11) DEFAULT NULL,
  `end3` int(11) DEFAULT NULL,
  `end4` int(11) DEFAULT NULL,
  `history` bit(1) NOT NULL,
  `level` bit(1) NOT NULL,
  `paiwufei` double DEFAULT NULL,
  `start1` int(11) DEFAULT NULL,
  `start2` int(11) DEFAULT NULL,
  `start3` int(11) DEFAULT NULL,
  `start4` int(11) DEFAULT NULL,
  `unit` double DEFAULT NULL,
  `unit1` double DEFAULT NULL,
  `unit2` double DEFAULT NULL,
  `unit3` double DEFAULT NULL,
  `unit4` double DEFAULT NULL,
  `weishengfei` double DEFAULT NULL,
  `zhaomingfei` double DEFAULT NULL,
  `zhina_scale` double DEFAULT NULL,
  `area_id` bigint(20) DEFAULT NULL,
  `base_id` bigint(20) DEFAULT NULL,
  `louzuo_id` bigint(20) DEFAULT NULL,
  `yongfang_xingzhi_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK50E5692A7918737` (`louzuo_id`),
  KEY `FK50E569222E24297` (`area_id`),
  KEY `FK50E56923B8D6C17` (`base_id`),
  KEY `FK50E5692A452916B` (`yongfang_xingzhi_id`),
  CONSTRAINT `FK50E569222E24297` FOREIGN KEY (`area_id`) REFERENCES `hps_area` (`id`),
  CONSTRAINT `FK50E56923B8D6C17` FOREIGN KEY (`base_id`) REFERENCES `hps_base` (`id`),
  CONSTRAINT `FK50E5692A452916B` FOREIGN KEY (`yongfang_xingzhi_id`) REFERENCES `hps_dictitem` (`id`),
  CONSTRAINT `FK50E5692A7918737` FOREIGN KEY (`louzuo_id`) REFERENCES `hps_louzuo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hps_electric_payment_date`
--

DROP TABLE IF EXISTS `hps_electric_payment_date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hps_electric_payment_date` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_user_name` varchar(50) DEFAULT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  `last_update_user_id` bigint(20) DEFAULT NULL,
  `last_update_user_name` varchar(50) DEFAULT NULL,
  `chaobiaos_initialized` bit(1) NOT NULL,
  `end_date` datetime NOT NULL,
  `month` datetime NOT NULL,
  `start_date` datetime NOT NULL,
  `base_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKA62259553B8D6C17` (`base_id`),
  CONSTRAINT `FKA62259553B8D6C17` FOREIGN KEY (`base_id`) REFERENCES `hps_base` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hps_electric_chaobiao`
--

DROP TABLE IF EXISTS `hps_electric_chaobiao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hps_electric_chaobiao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_user_name` varchar(50) DEFAULT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  `last_update_user_id` bigint(20) DEFAULT NULL,
  `last_update_user_name` varchar(50) DEFAULT NULL,
  `electricCharge` double DEFAULT NULL,
  `electricCount` bigint(20) DEFAULT NULL,
  `newReadoutsElectric` bigint(20) DEFAULT NULL,
  `paiwuCharge` double DEFAULT NULL,
  `provReadoutsElectric` bigint(20) DEFAULT NULL,
  `readMeterDate` datetime DEFAULT NULL,
  `readoutsElectric` bigint(20) DEFAULT NULL,
  `weishengCharge` double DEFAULT NULL,
  `zhaomingCharge` double DEFAULT NULL,
  `zhinajin` double DEFAULT NULL,
  `zhinajin_day_count` int(11) DEFAULT NULL,
  `charge_record_id` bigint(20) DEFAULT NULL,
  `house_id` bigint(20) NOT NULL,
  `house_owner_id` bigint(20) NOT NULL,
  `payment_date_id` bigint(20) NOT NULL,
  `unit_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF9031A9649B00DED` (`payment_date_id`),
  KEY `FKF9031A9650AFC216` (`house_owner_id`),
  KEY `FKF9031A96497290F4` (`unit_id`),
  KEY `FKF9031A966519ABDD` (`charge_record_id`),
  KEY `FKF9031A968E8AE55D` (`house_id`),
  CONSTRAINT `FKF9031A96497290F4` FOREIGN KEY (`unit_id`) REFERENCES `hps_electric_unit` (`id`),
  CONSTRAINT `FKF9031A9649B00DED` FOREIGN KEY (`payment_date_id`) REFERENCES `hps_electric_payment_date` (`id`),
  CONSTRAINT `FKF9031A9650AFC216` FOREIGN KEY (`house_owner_id`) REFERENCES `hps_house_owner` (`id`),
  CONSTRAINT `FKF9031A966519ABDD` FOREIGN KEY (`charge_record_id`) REFERENCES `hps_electric_charge_record` (`id`),
  CONSTRAINT `FKF9031A968E8AE55D` FOREIGN KEY (`house_id`) REFERENCES `hps_house` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25747 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hps_electric_charge_record`
--

DROP TABLE IF EXISTS `hps_electric_charge_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hps_electric_charge_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `create_user_name` varchar(50) DEFAULT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  `last_update_user_id` bigint(20) DEFAULT NULL,
  `last_update_user_name` varchar(50) DEFAULT NULL,
  `actual_charge` double NOT NULL,
  `cancelled` bit(1) NOT NULL,
  `cancelled_cause` varchar(500) DEFAULT NULL,
  `charge_date` datetime NOT NULL,
  `current_surplus` double NOT NULL,
  `must_charge` double NOT NULL,
  `previous_surplus` double NOT NULL,
  `zhinajin_on` bit(1) NOT NULL,
  `oper_user_id` bigint(20) DEFAULT NULL,
  `house_id` bigint(20) DEFAULT NULL,
  `house_owner_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5291E6EE50AFC216` (`house_owner_id`),
  KEY `FK5291E6EEB4BC30E6` (`oper_user_id`),
  KEY `FK5291E6EE8E8AE55D` (`house_id`),
  CONSTRAINT `FK5291E6EE50AFC216` FOREIGN KEY (`house_owner_id`) REFERENCES `hps_house_owner` (`id`),
  CONSTRAINT `FK5291E6EE8E8AE55D` FOREIGN KEY (`house_id`) REFERENCES `hps_house` (`id`),
  CONSTRAINT `FK5291E6EEB4BC30E6` FOREIGN KEY (`oper_user_id`) REFERENCES `hps_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18223 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-18 23:15:48
-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2017 at 01:43 PM
-- Server version: 10.1.22-MariaDB
-- PHP Version: 7.1.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fudnow`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `procedure_checkauth` (IN `_auth` VARCHAR(120), IN `expired_minites` BIGINT)  BEGIN

DECLARE time_created timestamp;
DECLARE user_id varchar(120);
DECLARE difference BIGINT;
DECLARE flag INT;

set flag = 0;



SELECT fud_user_auths.user_id,fud_user_auths.time_created into user_id,time_created FROM `fud_user_auths` WHERE auth=_auth OR old_auth=_auth;

SELECT TIMESTAMPDIFF(MINUTE,time_created,NOW()) into difference;


IF difference < expired_minites THEN 
set flag = 1;
select flag,user_id;
ELSE
select flag,user_id;
END IF; 


END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `procedure_fcms` (IN `_user_id` VARCHAR(120), IN `_device_id` VARCHAR(30), IN `_fcm_token` VARCHAR(225), IN `_platform` VARCHAR(1), IN `_version` VARCHAR(20))  BEGIN

INSERT INTO fud_user_fcms(user_id,device_id,fcm_token,platform,version) VALUES (_user_id,_device_id,_fcm_token,_platform,_version)
ON DUPLICATE KEY UPDATE 
  user_id=VALUES(user_id)
, fcm_token=VALUES(fcm_token)
, platform= VALUES(platform)
, version=VALUES(version)
, time_updated=NOW();

SELECT `fcm_id`, `user_id`, `device_id`, `fcm_token`, `platform`, `version`, `time_created`, `time_updated` FROM `fud_user_fcms` WHERE device_id=_device_id;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `procedure_refreshauth` (IN `_auth` VARCHAR(120), IN `_refresh_token` VARCHAR(120), IN `_new_auth` VARCHAR(120), IN `_new_refresh_token` VARCHAR(120))  BEGIN


DECLARE _user_id varchar(120);

SELECT fud_user_auths.user_id into _user_id FROM `fud_user_auths` WHERE auth=_auth OR old_auth=_auth;

INSERT INTO fud_user_auths(user_id,auth, old_auth, refresh_token, old_refresh_token) VALUES (_user_id,_new_auth,_auth,_new_refresh_token,_refresh_token)
ON DUPLICATE KEY UPDATE 
  auth=VALUES(auth)
, old_auth=VALUES(old_auth)
, refresh_token=VALUES(refresh_token)
, old_refresh_token=VALUES(old_refresh_token)
, time_created=NOW();

Select auth,refresh_token from fud_user_auths where user_id=_user_id;

END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `procedure_users` (IN `_phone_number` VARCHAR(25), IN `_country_prefix` VARCHAR(10))  BEGIN

DECLARE count_value INT;
DECLARE userid_value varchar(120);

INSERT INTO fud_users(phone_number,country_prefix) VALUES (_phone_number,_country_prefix)
ON DUPLICATE KEY UPDATE 
  phone_number=VALUES(phone_number)
, country_prefix=VALUES(country_prefix)
, time_updated=NOW();

SELECT user_id into userid_value FROM fud_users WHERE phone_number=_phone_number;

SELECT COUNT(user_id) into count_value  FROM fud_user_details WHERE user_id=userid_value;

IF count_value = 1 THEN 
select fud_user_details.user_id,fud_user_details.details_id, fud_user_details.first_name, fud_user_details.last_name, fud_user_details.email, fud_users.phone_number, fud_users.country_prefix, fud_users.time_created, fud_users.time_updated from fud_users INNER JOIN fud_user_details WHERE fud_users.user_id=userid_value AND fud_user_details.user_id=userid_value;
ELSE
SELECT user_id, phone_number, country_prefix, time_created, time_updated FROM fud_users WHERE user_id=userid_value;
END IF; 
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `procedure_user_details` (IN `_user_id` VARCHAR(120), IN `_first_name` VARCHAR(120), IN `_last_name` VARCHAR(120), IN `_email` VARCHAR(120))  BEGIN
   INSERT INTO fud_user_details(user_id, first_name, last_name, email) VALUES (_user_id,_first_name,_last_name,_email)
ON DUPLICATE KEY UPDATE 
  first_name=VALUES(first_name)
, last_name=VALUES(last_name)
, email=VALUES(email)
, time_updated=NOW();

select fud_user_details.user_id,fud_user_details.details_id, fud_user_details.first_name, fud_user_details.last_name,  fud_user_details.email, fud_users.phone_number, fud_users.country_prefix, fud_users.time_created, fud_users.time_updated from fud_users INNER JOIN fud_user_details ON fud_users.user_id=_user_id;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_bakeries`
--

CREATE TABLE `fud_bakeries` (
  `bakeries_id` varchar(120) NOT NULL,
  `bakery_name` varchar(120) NOT NULL,
  `location_id` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fud_bakeries`
--
DELIMITER $$
CREATE TRIGGER `insert_bakery_id` BEFORE INSERT ON `fud_bakeries` FOR EACH ROW SET new.bakeries_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_bakery_vendors`
--

CREATE TABLE `fud_bakery_vendors` (
  `vendor_id` varchar(120) NOT NULL,
  `vendor_name` varchar(120) NOT NULL,
  `vendor_user_name` varchar(120) NOT NULL,
  `vendor_password` varchar(120) NOT NULL,
  `bakeries_id` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fud_bakery_vendors`
--
DELIMITER $$
CREATE TRIGGER `insert_bakery_vendor_id` BEFORE INSERT ON `fud_bakery_vendors` FOR EACH ROW SET new.vendor_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_cake_menu`
--

CREATE TABLE `fud_cake_menu` (
  `cake_id` varchar(120) NOT NULL,
  `cake_name` varchar(120) NOT NULL,
  `cake_price` double NOT NULL,
  `cake_quantity` double NOT NULL,
  `cake_accompaniments` text,
  `bakeries_id` varchar(120) NOT NULL,
  `cake_type_id` int(11) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fud_cake_menu`
--
DELIMITER $$
CREATE TRIGGER `insert_cake_id` BEFORE INSERT ON `fud_cake_menu` FOR EACH ROW SET new.cake_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_cake_type`
--

CREATE TABLE `fud_cake_type` (
  `cake_type_id` int(11) NOT NULL,
  `cake_type_name` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_cake_type`
--

INSERT INTO `fud_cake_type` (`cake_type_id`, `cake_type_name`, `time_created`, `time_updated`) VALUES
(0, 'egg', '2017-10-15 11:28:17', '0000-00-00 00:00:00'),
(1, 'egg less', '2017-10-15 11:28:07', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `fud_companies`
--

CREATE TABLE `fud_companies` (
  `company_id` varchar(120) NOT NULL,
  `company_name` varchar(100) NOT NULL,
  `reference_id` varchar(20) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_companies`
--

INSERT INTO `fud_companies` (`company_id`, `company_name`, `reference_id`, `time_created`, `time_updated`) VALUES
('9F02027302B85BCB703690195E1EAF5E', 'infosys', 'inf', '2017-10-15 11:06:58', '0000-00-00 00:00:00'),
('EA91B965E68EB1072328D174F3643238', 'hsbc', 'hsb', '2017-10-15 11:07:12', '0000-00-00 00:00:00');

--
-- Triggers `fud_companies`
--
DELIMITER $$
CREATE TRIGGER `insert_company_id` BEFORE INSERT ON `fud_companies` FOR EACH ROW SET new.company_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_food_categories`
--

CREATE TABLE `fud_food_categories` (
  `categories_id` varchar(120) NOT NULL,
  `category_name` varchar(120) NOT NULL,
  `reference_id` varchar(20) NOT NULL,
  `allow` int(11) NOT NULL DEFAULT '0',
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_food_categories`
--

INSERT INTO `fud_food_categories` (`categories_id`, `category_name`, `reference_id`, `allow`, `time_created`, `time_updated`) VALUES
('120D3AFCCF7932DA22E727EB4A8BF155', 'italian', 'BBB', 1, '2017-10-15 10:07:20', '0000-00-00 00:00:00'),
('3228E471F426FECB0BDE1DAA2711D9CA', 'chinese', 'AAA', 1, '2017-10-15 10:06:57', '0000-00-00 00:00:00');

--
-- Triggers `fud_food_categories`
--
DELIMITER $$
CREATE TRIGGER `insert_food_category_id` BEFORE INSERT ON `fud_food_categories` FOR EACH ROW SET new.categories_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_food_courts`
--

CREATE TABLE `fud_food_courts` (
  `food_court_id` varchar(120) NOT NULL,
  `food_court_name` varchar(120) NOT NULL,
  `location_id` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_food_courts`
--

INSERT INTO `fud_food_courts` (`food_court_id`, `food_court_name`, `location_id`, `time_created`, `time_updated`) VALUES
('41E004E1C0A650A969748A82C9A1235E', 'old food court', 'FF36ACC578A8B48745534FE5431F8CCC', '2017-10-15 11:20:17', '0000-00-00 00:00:00'),
('DEF9A21B79161DE8690FD9BB8AEEA379', 'new food court', 'FF36ACC578A8B48745534FE5431F8CCC', '2017-10-15 11:19:52', '0000-00-00 00:00:00');

--
-- Triggers `fud_food_courts`
--
DELIMITER $$
CREATE TRIGGER `insert_food_court_id` BEFORE INSERT ON `fud_food_courts` FOR EACH ROW SET new.food_court_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_food_menu`
--

CREATE TABLE `fud_food_menu` (
  `item_id` varchar(120) NOT NULL,
  `item_name` varchar(120) NOT NULL,
  `item_price` double NOT NULL,
  `item_accompaniments` text,
  `restaurant_id` varchar(120) NOT NULL,
  `categories_id` varchar(120) NOT NULL,
  `food_type_id` int(11) NOT NULL,
  `time_zone_id` int(11) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fud_food_menu`
--
DELIMITER $$
CREATE TRIGGER `insert_food_item_id` BEFORE INSERT ON `fud_food_menu` FOR EACH ROW SET new.item_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_food_type`
--

CREATE TABLE `fud_food_type` (
  `food_type_id` int(11) NOT NULL,
  `food_type_name` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_food_type`
--

INSERT INTO `fud_food_type` (`food_type_id`, `food_type_name`, `time_created`, `time_updated`) VALUES
(0, 'non-veg', '2017-10-15 10:10:18', '0000-00-00 00:00:00'),
(1, 'veg', '2017-10-15 10:10:18', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `fud_locations`
--

CREATE TABLE `fud_locations` (
  `location_id` varchar(120) NOT NULL,
  `location_name` varchar(120) NOT NULL,
  `allow` int(11) DEFAULT '1',
  `company_id` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_locations`
--

INSERT INTO `fud_locations` (`location_id`, `location_name`, `allow`, `company_id`, `time_created`, `time_updated`) VALUES
('1264345F8869BC80C98A1815B8C9A98F', 'mysore', 1, '9F02027302B85BCB703690195E1EAF5E', '2017-10-15 11:13:57', '0000-00-00 00:00:00'),
('4368E0FDAF0AD5DB3D2A3C923277FDB4', 'mangalore', 1, '9F02027302B85BCB703690195E1EAF5E', '2017-10-15 11:14:33', '0000-00-00 00:00:00'),
('85528888FBB7000A1694DC5148E1A99D', 'pune', 1, '9F02027302B85BCB703690195E1EAF5E', '2017-10-15 11:14:19', '0000-00-00 00:00:00'),
('D99EA64C199426AB1340EDAB662F9CF8', 'kondapur-glt3', 1, 'EA91B965E68EB1072328D174F3643238', '2017-10-15 12:22:24', '0000-00-00 00:00:00'),
('ECD9A8D0001A346CC4BCD3629EAD3550', 'chennai', 1, '9F02027302B85BCB703690195E1EAF5E', '2017-10-15 11:14:44', '0000-00-00 00:00:00'),
('FF36ACC578A8B48745534FE5431F8CCC', 'hyderabad', 1, '9F02027302B85BCB703690195E1EAF5E', '2017-10-15 11:13:36', '0000-00-00 00:00:00');

--
-- Triggers `fud_locations`
--
DELIMITER $$
CREATE TRIGGER `insert_location_id` BEFORE INSERT ON `fud_locations` FOR EACH ROW SET new.location_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_managers`
--

CREATE TABLE `fud_managers` (
  `manager_id` varchar(120) NOT NULL,
  `manager_name` varchar(120) NOT NULL,
  `manager_phone_json_array` varchar(120) NOT NULL,
  `restaurant_id` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fud_managers`
--
DELIMITER $$
CREATE TRIGGER `insert_manager_id` BEFORE INSERT ON `fud_managers` FOR EACH ROW SET new.manager_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_restaurants`
--

CREATE TABLE `fud_restaurants` (
  `restaurant_id` varchar(120) NOT NULL,
  `restaurant_name` varchar(120) NOT NULL,
  `allow` int(11) DEFAULT '1',
  `food_court_id` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fud_restaurants`
--
DELIMITER $$
CREATE TRIGGER `insert_restaurant_id` BEFORE INSERT ON `fud_restaurants` FOR EACH ROW SET new.restaurant_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_restaurants_timmings`
--

CREATE TABLE `fud_restaurants_timmings` (
  `restaurant_id` varchar(120) NOT NULL,
  `binary_week` varchar(10) NOT NULL DEFAULT '0111111',
  `breakfast_start` time DEFAULT '00:00:00',
  `breakfast_end` time DEFAULT '00:00:00',
  `lunch_start` time DEFAULT '00:00:00',
  `lunch_end` time DEFAULT '00:00:00',
  `snacks_start` time DEFAULT '00:00:00',
  `snacks_end` time DEFAULT '00:00:00',
  `dinner_start` time DEFAULT '00:00:00',
  `dinner_end` time DEFAULT '00:00:00',
  `midnight_start` time DEFAULT '00:00:00',
  `midnight_end` time DEFAULT '00:00:00',
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `fud_time_sections`
--

CREATE TABLE `fud_time_sections` (
  `section_id` varchar(120) NOT NULL,
  `section_name` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `fud_time_zones`
--

CREATE TABLE `fud_time_zones` (
  `time_zone_id` int(11) NOT NULL,
  `time_zone_name` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_time_zones`
--

INSERT INTO `fud_time_zones` (`time_zone_id`, `time_zone_name`, `time_created`, `time_updated`) VALUES
(100, 'breakfast', '2017-10-15 10:14:50', '0000-00-00 00:00:00'),
(101, 'lunch', '2017-10-15 10:14:50', '0000-00-00 00:00:00'),
(102, 'snacks', '2017-10-15 10:14:50', '0000-00-00 00:00:00'),
(103, 'dinner', '2017-10-15 10:14:50', '0000-00-00 00:00:00'),
(104, 'mid night', '2017-10-15 10:14:50', '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `fud_users`
--

CREATE TABLE `fud_users` (
  `user_id` varchar(120) NOT NULL,
  `phone_number` varchar(25) NOT NULL,
  `country_prefix` varchar(10) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_users`
--

INSERT INTO `fud_users` (`user_id`, `phone_number`, `country_prefix`, `time_created`, `time_updated`) VALUES
('32588957C85C4B8294E348628FEB272F', '8977605703', '91', '2017-09-20 14:04:57', '2017-09-22 07:15:38'),
('53EA3402D6206EC06E5919B8ED52A637', '9666966132', '91', '2017-12-01 11:53:40', '2017-12-01 11:54:16'),
('5D4EB8BBE335FF882DC5BCD98CFFD440', '7207825050', '91', '2017-09-22 07:08:18', '2017-09-22 13:57:06'),
('8E54F5456F502E95C52F465019B71C1D', '7207824353', '91', '2017-09-19 13:49:05', '2017-12-01 12:20:27'),
('92386E45E7DAE528AC63383E7573B0FC', '8500546854', '91', '2017-12-01 12:20:13', '0000-00-00 00:00:00'),
('AF2EF24641426B5751B491EDC8EC4A63', '9441177447', '91', '2017-10-15 12:27:47', '0000-00-00 00:00:00'),
('B2BBA457B64C759A56170422716F63F7', '8500618545', '91', '2017-09-20 13:19:57', '2017-09-22 07:25:44'),
('BEB1794CC17B3C5AD0BA893FCFBBBF32', '7207841153', '91', '2017-10-01 10:35:29', '2017-10-15 12:26:14');

--
-- Triggers `fud_users`
--
DELIMITER $$
CREATE TRIGGER `trigger_user_id` BEFORE INSERT ON `fud_users` FOR EACH ROW SET new.user_id= HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(),'FUDN@WM#Y#$!4'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_user_auths`
--

CREATE TABLE `fud_user_auths` (
  `user_id` varchar(120) NOT NULL,
  `auth` varchar(120) NOT NULL,
  `old_auth` varchar(120) NOT NULL,
  `refresh_token` varchar(120) NOT NULL,
  `old_refresh_token` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_user_auths`
--

INSERT INTO `fud_user_auths` (`user_id`, `auth`, `old_auth`, `refresh_token`, `old_refresh_token`, `time_created`) VALUES
('8E54F5456F502E95C52F465019B71C1D', 'O0XN3AsOVr8FFlrUsQF53sYpipO16dpm', '20nbggyPsYl9Pz7LB25RVuf1egx9uKA6', '6wbKRirwqs1k1w.d', '5S2gH1km2CG1i1pD', '2017-10-15 12:30:21');

-- --------------------------------------------------------

--
-- Table structure for table `fud_user_details`
--

CREATE TABLE `fud_user_details` (
  `details_id` varchar(120) NOT NULL,
  `user_id` varchar(120) NOT NULL,
  `first_name` varchar(120) NOT NULL,
  `last_name` varchar(120) NOT NULL,
  `email` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_user_details`
--

INSERT INTO `fud_user_details` (`details_id`, `user_id`, `first_name`, `last_name`, `email`, `time_created`, `time_updated`) VALUES
('92CC70C0343FDEC1FFD5DB29FAAE4E93', 'B2BBA457B64C759A56170422716F63F7', 'murali', 'katragadda', 'muralikatragadda786@gmail.com', '2017-09-20 13:49:40', '2017-09-22 13:47:55'),
('B61F66088C346F5608FB8518003B0707', '8E54F5456F502E95C52F465019B71C1D', 'gokul', 'kalagara', 'gokulkalagara@gmail.com', '2017-09-19 14:36:15', '2017-09-20 13:48:53');

--
-- Triggers `fud_user_details`
--
DELIMITER $$
CREATE TRIGGER `trigger_details_id` BEFORE INSERT ON `fud_user_details` FOR EACH ROW SET new.details_id= HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(),'FUDN@WM#Y#$!4'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_user_fcms`
--

CREATE TABLE `fud_user_fcms` (
  `fcm_id` varchar(120) NOT NULL,
  `user_id` varchar(120) NOT NULL,
  `device_id` varchar(30) NOT NULL,
  `fcm_token` varchar(225) NOT NULL,
  `platform` varchar(1) NOT NULL,
  `version` varchar(10) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `fud_user_fcms`
--

INSERT INTO `fud_user_fcms` (`fcm_id`, `user_id`, `device_id`, `fcm_token`, `platform`, `version`, `time_created`, `time_updated`) VALUES
('974BEB88579A3BBE51D4B966BCE9B725', '8E54F5456F502E95C52F465019B71C1D', '1234567890', 'maya fcm token is king', 'A', '1.0', '2017-09-20 12:38:05', '2017-09-23 07:22:59'),
('C332AEB8BEF5C3F4C5D58B9E0AF763D5', 'B2BBA457B64C759A56170422716F63F7', '123456789', 'maya fcm token', 'A', '1.0', '2017-09-20 11:32:56', '2017-09-20 13:59:59');

--
-- Triggers `fud_user_fcms`
--
DELIMITER $$
CREATE TRIGGER `trigger_user_fcms` BEFORE INSERT ON `fud_user_fcms` FOR EACH ROW SET new.fcm_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'FUDN@WM#Y#$!4'))
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `fud_vendors`
--

CREATE TABLE `fud_vendors` (
  `vendor_id` varchar(120) DEFAULT NULL,
  `vendor_name` varchar(120) NOT NULL,
  `vendor_user_name` varchar(120) NOT NULL,
  `vendor_password` varchar(120) NOT NULL,
  `restaurant_id` varchar(120) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `fud_vendors`
--
DELIMITER $$
CREATE TRIGGER `insert_vendor_id` BEFORE INSERT ON `fud_vendors` FOR EACH ROW SET new.vendor_id = HEX(AES_ENCRYPT(NOW()+UNIX_TIMESTAMP(), 'N@NUl#514'))
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `fud_bakeries`
--
ALTER TABLE `fud_bakeries`
  ADD PRIMARY KEY (`bakeries_id`),
  ADD KEY `location_id` (`location_id`);

--
-- Indexes for table `fud_bakery_vendors`
--
ALTER TABLE `fud_bakery_vendors`
  ADD PRIMARY KEY (`vendor_id`),
  ADD UNIQUE KEY `vendor_user_name` (`vendor_user_name`),
  ADD KEY `bakeries_id` (`bakeries_id`);

--
-- Indexes for table `fud_cake_menu`
--
ALTER TABLE `fud_cake_menu`
  ADD PRIMARY KEY (`cake_id`),
  ADD KEY `bakeries_id` (`bakeries_id`);

--
-- Indexes for table `fud_cake_type`
--
ALTER TABLE `fud_cake_type`
  ADD PRIMARY KEY (`cake_type_id`);

--
-- Indexes for table `fud_companies`
--
ALTER TABLE `fud_companies`
  ADD PRIMARY KEY (`company_id`),
  ADD UNIQUE KEY `reference_id` (`reference_id`);

--
-- Indexes for table `fud_food_categories`
--
ALTER TABLE `fud_food_categories`
  ADD PRIMARY KEY (`categories_id`),
  ADD UNIQUE KEY `reference_id` (`reference_id`);

--
-- Indexes for table `fud_food_courts`
--
ALTER TABLE `fud_food_courts`
  ADD PRIMARY KEY (`food_court_id`),
  ADD KEY `location_id` (`location_id`);

--
-- Indexes for table `fud_food_menu`
--
ALTER TABLE `fud_food_menu`
  ADD PRIMARY KEY (`item_id`),
  ADD KEY `restaurant_id` (`restaurant_id`),
  ADD KEY `categories_id` (`categories_id`),
  ADD KEY `food_type_id` (`food_type_id`),
  ADD KEY `time_zone_id` (`time_zone_id`);

--
-- Indexes for table `fud_food_type`
--
ALTER TABLE `fud_food_type`
  ADD PRIMARY KEY (`food_type_id`);

--
-- Indexes for table `fud_locations`
--
ALTER TABLE `fud_locations`
  ADD PRIMARY KEY (`location_id`),
  ADD KEY `company_id` (`company_id`);

--
-- Indexes for table `fud_managers`
--
ALTER TABLE `fud_managers`
  ADD PRIMARY KEY (`manager_id`),
  ADD KEY `restaurant_id` (`restaurant_id`);

--
-- Indexes for table `fud_restaurants`
--
ALTER TABLE `fud_restaurants`
  ADD PRIMARY KEY (`restaurant_id`),
  ADD KEY `food_court_id` (`food_court_id`);

--
-- Indexes for table `fud_restaurants_timmings`
--
ALTER TABLE `fud_restaurants_timmings`
  ADD KEY `restaurant_id` (`restaurant_id`);

--
-- Indexes for table `fud_time_sections`
--
ALTER TABLE `fud_time_sections`
  ADD PRIMARY KEY (`section_id`);

--
-- Indexes for table `fud_time_zones`
--
ALTER TABLE `fud_time_zones`
  ADD PRIMARY KEY (`time_zone_id`);

--
-- Indexes for table `fud_users`
--
ALTER TABLE `fud_users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `phone_number` (`phone_number`);

--
-- Indexes for table `fud_user_auths`
--
ALTER TABLE `fud_user_auths`
  ADD UNIQUE KEY `auth` (`auth`),
  ADD UNIQUE KEY `old_auth` (`old_auth`),
  ADD UNIQUE KEY `refresh_token` (`refresh_token`),
  ADD UNIQUE KEY `old_refresh_token` (`old_refresh_token`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `fud_user_details`
--
ALTER TABLE `fud_user_details`
  ADD PRIMARY KEY (`details_id`),
  ADD UNIQUE KEY `user_id` (`user_id`);

--
-- Indexes for table `fud_user_fcms`
--
ALTER TABLE `fud_user_fcms`
  ADD PRIMARY KEY (`fcm_id`),
  ADD UNIQUE KEY `device_id` (`device_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `fud_vendors`
--
ALTER TABLE `fud_vendors`
  ADD UNIQUE KEY `vendor_user_name` (`vendor_user_name`),
  ADD KEY `restaurant_id` (`restaurant_id`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `fud_bakeries`
--
ALTER TABLE `fud_bakeries`
  ADD CONSTRAINT `fud_bakeries_ibfk_1` FOREIGN KEY (`location_id`) REFERENCES `fud_locations` (`location_id`);

--
-- Constraints for table `fud_bakery_vendors`
--
ALTER TABLE `fud_bakery_vendors`
  ADD CONSTRAINT `fud_bakery_vendors_ibfk_1` FOREIGN KEY (`bakeries_id`) REFERENCES `fud_bakeries` (`bakeries_id`);

--
-- Constraints for table `fud_cake_menu`
--
ALTER TABLE `fud_cake_menu`
  ADD CONSTRAINT `fud_cake_menu_ibfk_1` FOREIGN KEY (`bakeries_id`) REFERENCES `fud_bakeries` (`bakeries_id`);

--
-- Constraints for table `fud_food_courts`
--
ALTER TABLE `fud_food_courts`
  ADD CONSTRAINT `fud_food_courts_ibfk_1` FOREIGN KEY (`location_id`) REFERENCES `fud_locations` (`location_id`);

--
-- Constraints for table `fud_food_menu`
--
ALTER TABLE `fud_food_menu`
  ADD CONSTRAINT `fud_food_menu_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `fud_restaurants` (`restaurant_id`),
  ADD CONSTRAINT `fud_food_menu_ibfk_2` FOREIGN KEY (`categories_id`) REFERENCES `fud_food_categories` (`categories_id`),
  ADD CONSTRAINT `fud_food_menu_ibfk_3` FOREIGN KEY (`food_type_id`) REFERENCES `fud_food_type` (`food_type_id`),
  ADD CONSTRAINT `fud_food_menu_ibfk_4` FOREIGN KEY (`time_zone_id`) REFERENCES `fud_time_zones` (`time_zone_id`);

--
-- Constraints for table `fud_locations`
--
ALTER TABLE `fud_locations`
  ADD CONSTRAINT `fud_locations_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `fud_companies` (`company_id`);

--
-- Constraints for table `fud_managers`
--
ALTER TABLE `fud_managers`
  ADD CONSTRAINT `fud_managers_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `fud_restaurants` (`restaurant_id`);

--
-- Constraints for table `fud_restaurants`
--
ALTER TABLE `fud_restaurants`
  ADD CONSTRAINT `fud_restaurants_ibfk_1` FOREIGN KEY (`food_court_id`) REFERENCES `fud_food_courts` (`food_court_id`);

--
-- Constraints for table `fud_restaurants_timmings`
--
ALTER TABLE `fud_restaurants_timmings`
  ADD CONSTRAINT `fud_restaurants_timmings_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `fud_restaurants` (`restaurant_id`);

--
-- Constraints for table `fud_user_auths`
--
ALTER TABLE `fud_user_auths`
  ADD CONSTRAINT `fud_user_auths_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `fud_users` (`user_id`);

--
-- Constraints for table `fud_user_details`
--
ALTER TABLE `fud_user_details`
  ADD CONSTRAINT `fud_user_details_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `fud_users` (`user_id`);

--
-- Constraints for table `fud_user_fcms`
--
ALTER TABLE `fud_user_fcms`
  ADD CONSTRAINT `fud_user_fcms_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `fud_users` (`user_id`);

--
-- Constraints for table `fud_vendors`
--
ALTER TABLE `fud_vendors`
  ADD CONSTRAINT `fud_vendors_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `fud_restaurants` (`restaurant_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

DROP DATABASE IF EXISTS `satellite_tracker`;
CREATE DATABASE `satellite_tracker`;
USE `satellite_tracker`;

CREATE TABLE accounts(
	`id` INT UNIQUE NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`username` VARCHAR(255) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL
);

CREATE TABLE satellites(
	`id` INT UNIQUE NOT NULL PRIMARY KEY,
    `name` VARCHAR(255)
);

CREATE TABLE favorites(
    `account_id` INT NOT NULL,
    `satellite_id` INT NOT NULL,
    PRIMARY KEY(`account_id`, `satellite_id`)
);

DELIMITER $$

CREATE PROCEDURE get_id_password(IN `_username` varchar(255))
BEGIN
	SELECT `id`, password FROM `accounts` WHERE `username` = `_username` LIMIT 1;
END$$


DELIMITER ;